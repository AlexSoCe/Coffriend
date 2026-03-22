package com.alexsotocepas.coffriend.communications

import android.util.Log
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
    private const val BASE_URL = "https://coffriend-servidor.onrender.com/api"

    /** Instància de Gson per a la conversió d'objectes a JSON i viceversa. */
    private val gson = Gson()

    /** Token de sessió actual obtingut després d'un login correcte.
     * S'utilitza per identificar l'usuari en peticions posteriors.
     */
    var sessionToken: String? = null

    /**
     * Realitza una petició d'inici de sessió al servidor.
     *
     * Envia les credencials de l'usuari en format JSON i, si són vàlides,
     * emmagatzema el token de sessió retornat pel servidor.
     *
     * @param user El nom d'usuari (username) que intenta fer login.
     * @param pass La contrasenya de l'usuari.
     * @return [Boolean] Retorna `true` si el login ha estat acceptat pel servidor i s'ha rebut un token; `false` en cas contrari.
     * @throws Exception Captura internament qualsevol error de xarxa o de parseig, retornant `false`.
     */
    suspend fun doLogin(user: String, pass: String): Boolean {
        return try {
            val url = URL("$BASE_URL/login")
            val conn = url.openConnection() as HttpURLConnection
            conn.requestMethod = "POST"
            conn.setRequestProperty("Content-Type", "application/json")
            conn.doOutput = true

            // Enviem el JSON que el servidor espera
            val requestBody = gson.toJson(mapOf("username" to user, "password" to pass))
            conn.outputStream.use { it.write(requestBody.toByteArray()) }

            if (conn.responseCode == 200) {
                val response = conn.inputStream.bufferedReader().use { it.readText() }
                Log.d("DEBUG_LOGIN", "Resposta del servidor: $response")

                // Mapegem la resposta per comprovar l'èxit i el token
                val map = gson.fromJson(response, Map::class.java)
                val isSuccess = map["success"]?.toString()?.toBoolean() ?: false

                Log.d("DEBUG_LOGIN", "¿Success es true?: $isSuccess") // <--- AÑADE ESTO

                return if (isSuccess) {
                    sessionToken = map["token"]?.toString()
                    true
                } else {
                    Log.w("DEBUG_LOGIN", "Login fallit segons el servidor")
                    false
                }
            } else {
                Log.e("DEBUG_LOGIN", "Error de connexió. Codi: ${conn.responseCode}")
                false
            }
        } catch (e: Exception) {
            Log.e("CommController", "Error en login: ${e.message}")
            false
        }
    }

    /**
     * Tanca la sessió de l'usuari actual al servidor.
     *
     * Envia el [sessionToken] actual per invalidar-lo al servidor i,
     * si la resposta és correcta, esborra el token localment.
     *
     * @return [Boolean] Retorna `true` si el tancament de sessió s'ha completat correctament (Codi 200).
     */
    suspend fun doLogout(): Boolean {
        return try {
            val url = URL("$BASE_URL/logout")
            val conn = url.openConnection() as HttpURLConnection
            conn.requestMethod = "POST"
            conn.setRequestProperty("Content-Type", "application/json")
            conn.doOutput = true


            val requestBody = gson.toJson(mapOf("token" to sessionToken))
            conn.outputStream.use { it.write(requestBody.toByteArray()) }

            if (conn.responseCode == 200) {
                sessionToken = null
                true
            } else false
        } catch (e: Exception) {
            false
        }
    }
}
