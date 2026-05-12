package com.alexsotocepas.coffriend.communications

import android.util.Log
import com.alexsotocepas.coffriend.data.IOUiState
import com.alexsotocepas.coffriend.data.Insignia
import com.alexsotocepas.coffriend.data.Product
import com.alexsotocepas.coffriend.data.User
import com.google.gson.reflect.TypeToken
import java.net.HttpURLConnection
import java.net.URL
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

/**
 * Controlador de comunicacions encarregat de gestionar les peticions HTTP amb el servidor.
 * Aquest objecte centralitza les crides a l'API de Coffriend, gestionant l'autenticació
 * mitjançant tokens de sessió i la serialització de dades amb Gson.
 */
object CommController {
    /** URL base del servidor allotjat a Render. */
    private const val BASE_URL = "https://special-barnacle-production.up.railway.app/api"

    /** Instància de Gson per a la conversió d'objectes a JSON i viceversa. */
    private val gson = com.google.gson.GsonBuilder()
        .serializeNulls()
        .create()

    /** Token de sessió actual obtingut després d'un login correcte.
     * S'utilitza per identificar l'usuari en peticions posteriors.
     */
    var sessionToken: String? = null

    /**
     * Funció privada genèrica per realitzar peticions POST al servidor.
     * Centralitza la configuració de la connexió HttpURLConnection per evitar duplicar codi.
     * @param endpoint El camí final de l'URL (ex: "/login", "/register").
     * @param bodyMap El mapa de dades que es convertirà a JSON.
     * @return [String] La resposta del servidor en format text o null si hi ha hagut un error.
     */
    private fun makePostRequest(endpoint: String, bodyMap: Map<String, Any?>): String? {
        return try {
            val url = URL("$BASE_URL$endpoint")
            Log.d("CommController", "Connectant a: ${url.toString()}")
            val conn = url.openConnection() as HttpURLConnection
            conn.requestMethod = "POST"
            conn.setRequestProperty("Content-Type", "application/json")
            conn.doOutput = true

            // Serialització i enviament del cos de la petició
            val requestBody = gson.toJson(bodyMap)
            conn.outputStream.use { it.write(requestBody.toByteArray()) }

            if (conn.responseCode in 200..299) {
                conn.inputStream.bufferedReader().use { it.readText() }
            } else {
                val errorResponse = conn.errorStream?.bufferedReader()?.readText()
                Log.e("CommController", "Error: Codi de resposta ${conn.responseCode} a $endpoint")
                null
            }
        } catch (e: Exception) {
            Log.e("CommController", "Excepció a $endpoint", e)
            null
        }
    }

    /**
     * Realitza una petició d'inici de sessió al servidor.
     * Envia les credencials de l'usuari en format JSON i, si són vàlides,
     * emmagatzema el token de sessió retornat pel servidor.
     * @param email L'adreça de correu de l'usuari.
     * @param pass La contrasenya de l'usuari.
     * @return [Boolean] Retorna `true` si el login ha estat acceptat pel servidor i s'ha rebut un token; `false` en cas contrari.
     */
    suspend fun doLogin(email: String, pass: String): Boolean {
        val peticio = EndPointValues("/auth/login")
        peticio.addPrimitiveData(email)
        peticio.addPrimitiveData(pass)

        val response = makePostRequest(peticio.order!!, mapOf("email" to email, "password" to pass))

        return if (response != null) {
            try {
                val loginData = gson.fromJson(response, LoginResponse::class.java)
                sessionToken = loginData.token

                val rawMap = gson.fromJson(response, Map::class.java)
                val userMap = rawMap["user"] as? Map<*, *>
                val insigniasJson = gson.toJson(userMap?.get("insignies"))

                val tipoLista = object : com.google.gson.reflect.TypeToken<List<Insignia>>() {}.type
                val listaManual: List<Insignia>? = gson.fromJson(insigniasJson, tipoLista)

                User.current = loginData.user.copy(llistaInsignies = loginData.user.llistaInsignies ?: listaManual)

                Log.d("LOGIN_FINAL", "Insignias finales: ${User.current?.llistaInsignies}")
                true
            } catch (e: Exception) {
                Log.e("LOGIN_FINAL", "Error: ${e.message}")
                false
            }
        } else false
    }

    /**
     * Tanca la sessió de l'usuari actual al servidor.
     * Envia el [sessionToken] actual per invalidar-lo al servidor i,
     * si la resposta és correcta, esborra el token localment.
     * @return [Boolean] Retorna `true` si el tancament de sessió s'ha completat correctament (Codi 200).
     */
    suspend fun doLogout(): Boolean {
        val response = makePostRequest("/auth/logout", mapOf("token" to sessionToken))
        return if (response != null) {
            sessionToken = null
            true
        } else false
    }

    /**
     * Registra un nou usuari a la base de dades del servidor.
     * @param email Correu electrònic del nou usuari.
     * @param nom Nom complet o nom d'usuari.
     * @param pass Contrasenya per al nou compte.
     * @return [IOUiState] Estat de la operació (èxit o error per usuari existent).
     */
    suspend fun doRegister(email: String, nom: String, pass: String): IOUiState {
        val response = makePostRequest("/usuaris", mapOf(
            "nom" to nom,
            "email" to email,
            "password" to pass
        ))

        return if (response != null) {
            // El server respon 201 amb l'objecte, així que detectem èxit
            IOUiState(result = "ok", goodResult = true)
        } else {
            // Si el server dona 400 (email duplicat) el makePostRequest tornarà null
            IOUiState(result = "user_exists", goodResult = false)
        }
    }

    /**
     * Actualitza la informació d'un usuari existent.
     * @param id L'identificador de l'usuari.
     * @param user El model d'usuari amb les dades noves (nom, email, pass).
     * @return L'objecte [User] actualitzat si té èxit, o null si falla.
     */
    suspend fun doUpdateUser(id: Int, user: User): User? {
        return try {
            val url = URL("$BASE_URL/usuaris/$id")
            val conn = url.openConnection() as HttpURLConnection
            conn.requestMethod = "PUT"
            conn.setRequestProperty("Content-Type", "application/json")
            conn.setRequestProperty("Authorization", "Bearer $sessionToken")
            conn.doOutput = true

            val jsonInput = gson.toJson(user)
            conn.outputStream.use { it.write(jsonInput.toByteArray()) }

            if (conn.responseCode in 200..299) {
                val responseString = conn.inputStream.bufferedReader().readText()
                gson.fromJson(responseString, User::class.java)
            } else {
                Log.e("UPDATE_DEBUG", "Error servidor: ${conn.responseCode}")
                null
            }
        } catch (e: Exception) {
            Log.e("UPDATE_DEBUG", "Error: ${e.message}")
            null
        }
    }

    /**
     * Elimina un usuari del sistema de forma permanent.
     * @param id Identificador de l'usuari a suprimir.
     * @return `true` si l'eliminació s'ha realitzat correctament al servidor.
     */
    suspend fun deleteUser(id: Int): Boolean {
        return try {
            val url = URL("$BASE_URL/usuaris/$id")
            val conn = url.openConnection() as HttpURLConnection
            conn.requestMethod = "DELETE"
            conn.setRequestProperty("Authorization", "Bearer $sessionToken") // Per si el server demana el token

            conn.responseCode in 200..299
        } catch (e: Exception) {
            Log.e("CommController", "Error eliminant usuari", e)
            false
        }
    }

    /**
     * Inicialitza la base de dades del servidor amb dades de prova (Seed).
     * Aquesta operació només s'ha d'utilitzar en entorns de desenvolupament o demostració.
     * @return `true` si la càrrega de dades ha estat satisfactòria.
     */
    suspend fun doSystemDemoSeed(): Boolean = withContext(Dispatchers.IO) {
        try {
            val url = URL("$BASE_URL/system/seed")
            val conn = url.openConnection() as HttpURLConnection
            conn.requestMethod = "POST"
            conn.setRequestProperty("Authorization", "Bearer $sessionToken")
            val code = conn.responseCode
            println("DEBUG_SEED: Código de respuesta $code")
            if (code !in 200..299) {
                val errorMsg = conn.errorStream?.bufferedReader()?.readText()
                println("DEBUG_SEED: Error del servidor: $errorMsg")
            }
            conn.responseCode in 200..299
        } catch (e: Exception) {
            println("DEBUG_SEED: Excepción: ${e.message}")
            e.printStackTrace()
            false
        }
    }

    /**
     * Reinicia la base de dades de demostració del servidor.
     * Elimina les dades temporals per retornar el sistema a un estat net.
     * @return `true` si el reinici s'ha completat.
     */
    suspend fun doSystemResetDemo(): Boolean = withContext(Dispatchers.IO){
        try {
            val url = URL("$BASE_URL/system/resetDemo")
            val conn = url.openConnection() as HttpURLConnection
            conn.requestMethod = "DELETE"
            conn.setRequestProperty("Authorization", "Bearer $sessionToken")
            val code = conn.responseCode
            println("DEBUG_SEED: Código de respuesta $code")
            if (code !in 200..299) {
                val errorMsg = conn.errorStream?.bufferedReader()?.readText()
                println("DEBUG_SEED: Error del servidor: $errorMsg")
            }
            conn.responseCode in 200..299
        } catch (e: Exception) {
            println("DEBUG_SEED: Excepción: ${e.message}")
            e.printStackTrace()
            false
        }
    }

    /**
     * Obté el llistat complet de productes disponibles al catàleg.
     * Realitza una petició GET i deserialitza la llista de [Product] mitjançant un [TypeToken].
     * @return Una llista d'objectes [Product]. Si hi ha un error, retorna una llista buida.
     */
    suspend fun doGetProducts(): List<Product> {
        return try {
            val url = URL("$BASE_URL/productes")
            val conn = url.openConnection() as HttpURLConnection
            conn.requestMethod = "GET"
            conn.setRequestProperty("Authorization", "Bearer $sessionToken")

            if (conn.responseCode in 200..299) {
                val json = conn.inputStream.bufferedReader().readText()
                val itemType = object : TypeToken<List<Product>>() {}.type
                gson.fromJson(json, itemType)
            } else {
                emptyList()
            }
        } catch (e: Exception) {
            emptyList()
        }
    }
}

/**
 * Estructura de dades per a la resposta del servidor en l'inici de sessió.
 * @property token Cadena JWT d'autorització.
 * @property user Objecte [User] amb la informació del perfil completat.
 */
data class LoginResponse(
    val token: String,
    val user: User
)