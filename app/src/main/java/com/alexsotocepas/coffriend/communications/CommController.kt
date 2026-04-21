package com.alexsotocepas.coffriend.communications

import android.util.Log
import com.alexsotocepas.coffriend.data.IOUiState
import com.alexsotocepas.coffriend.data.User
import com.google.gson.Gson
import java.net.HttpURLConnection
import java.net.URL

/**
 * Controlador de comunicacions encarregat de gestionar les peticions HTTP amb el servidor.
 * Aquest objecte centralitza les crides a l'API de Coffriend, gestionant l'autenticació
 * mitjançant tokens de sessió i la serialització de dades amb Gson.
 */
object CommController {
    /** URL base del servidor allotjat a Render. */
    private const val BASE_URL = "https://special-barnacle-production.up.railway.app/api"

    /** Instància de Gson per a la conversió d'objectes a JSON i viceversa. */
    private val gson = Gson()

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
        val response = makePostRequest("/auth/login", mapOf("email" to email, "password" to pass))

        return if (response != null) {
            try {
                val map = gson.fromJson(response, Map::class.java)
                sessionToken = map["token"]?.toString()

                // Extraiem l'objecte "usuari" que ve dins de la resposta
                val usuariJson = gson.toJson(map["usuari"])
                User.current = gson.fromJson(usuariJson, User::class.java)

                sessionToken != null
            } catch (e: Exception) {
                Log.e("CommController", "Error parsejant login: ${e.message}")
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
     * Realitza una petició DELETE per eliminar un usuari.
     * @param id L'identificador de l'usuari a eliminar.
     * @return [Boolean] true si el servidor respon 200/201, false en cas contrari.
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
}
