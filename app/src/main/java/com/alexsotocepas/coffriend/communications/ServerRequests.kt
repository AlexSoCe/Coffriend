package com.alexsotocepas.coffriend.communications

import android.util.Log
import com.alexsotocepas.coffriend.data.User
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock

/**
 * Proveïdor de serveis de xarxa d'alt nivell per a l'aplicació.
 * Aquesta classe organitza les crides lògiques al servidor utilitzant [CommController].
 * Implementa un mecanisme de seguretat mitjançant un [Mutex] per evitar condicions de carrera
 * (race conditions) quan s'intenta accedir o modificar l'estat de la sessió simultàniament.
 */
object ServerRequests {

    /** * Semàfor d'exclusió mútua (Mutex) per garantir que només una operació de xarxa
     * crítica s'executi a la vegada, assegurant la integritat de les dades del fil (thread-safe).
     */
    private val accessMutex = Mutex()

    /**
     * Gestiona el procés d'autenticació de l'usuari.
     * Bloqueja el [accessMutex] durant la petició per evitar interferències. Si el login és correcte,
     * inicialitza l'objecte global [User.current] amb les dades de l'usuari.
     * @param username El nom d'identificació de l'usuari.
     * @param password La contrasenya de l'usuari.
     * @return [Boolean] `true` si l'usuari s'ha autenticat correctament; `false` en cas contrari.
     * @see CommController.doLogin
     */
    suspend fun login(username: String, password: String): Boolean {
        Log.d("MUTEX_DEBUG", "1. Intentando entrar al Mutex de LOGIN...")
        return accessMutex.withLock {
            Log.d("MUTEX_DEBUG", "2. ¡LOGIN ha entrado al Mutex!")
            val loginSuccess = CommController.doLogin(username, password)
            if (loginSuccess) {
                // Si el servidor confirma l'èxit, creem l'instància local de l'usuari
                User.current = User(username = username)
            }
            Log.d("MUTEX_DEBUG", "3. LOGIN saliendo del Mutex y liberando la llave.")
            loginSuccess
        }
    }

    /**
     * Gestiona el tancament de la sessió actual.
     * Bloqueja el [accessMutex] per assegurar que cap altra petició s'executi mentre es tanca la sessió.
     * Independentment de si la petició al servidor falla o no, es netegen les dades de l'usuari local
     * mitjançant [User.logout] per seguretat.
     * @return [Boolean] `true` si el tancament al servidor ha estat net (200 OK); `false` si hi ha hagut un error.
     * @see CommController.doLogout
     */
    suspend fun logout(): Boolean {
        Log.d("MUTEX_DEBUG", "A. Intentando entrar al Mutex de LOGOUT...")
        return accessMutex.withLock {
            Log.d("MUTEX_DEBUG", "B. ¡LOGOUT ha entrado al Mutex!")
            try {
                val success = CommController.doLogout()
                User.logout()
                Log.d("MUTEX_DEBUG", "C. LOGOUT saliendo del Mutex y liberando la llave.")
                success
            } catch (e: Exception) {
                // En cas d'error de xarxa, forcem el tancament de la sessió local igualment
                User.logout()
                false
            }
        }
    }
}