package com.alexsotocepas.coffriend.communications

import android.util.Log
import com.alexsotocepas.coffriend.data.IOUiState
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

    /**
     * Semàfor d'exclusió mútua (Mutex) per garantir la integritat de les dades.
     * Assegura que només una operació de xarxa crítica s'executi a la vegada,
     * evitant conflictes en l'accés al token de sessió o a l'objecte [User.current].
     */
    private val accessMutex = Mutex()

    /**
     * Gestiona el procés d'autenticació de l'usuari.
     * Bloqueja el [accessMutex] durant la petició per evitar interferències. Si el login és correcte,
     * inicialitza l'objecte global [User.current] amb les dades de l'usuari.
     * @param email L'adreça de correu electrònic de l'usuari.
     * @param password La contrasenya de l'usuari.
     * @return [Boolean] `true` si l'usuari s'ha autenticat correctament; `false` en cas contrari.
     */
    suspend fun login(email: String, password: String): Boolean {
        Log.d("MUTEX_DEBUG", "1. Intentando entrar al Mutex de LOGIN...")
        return accessMutex.withLock {
            Log.d("MUTEX_DEBUG", "2. ¡LOGIN ha entrado al Mutex!")
            val loginSuccess = CommController.doLogin(email, password)
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

    /**
     * Realitza una petició POST al servidor per registrar un nou usuari.
     * @param email Correu electrònic del nou usuari.
     * @param user El nom d'usuari triat.
     * @param pass La contrasenya de l'usuari.
     * @return Un objecte [IOUiState] amb la resposta del servidor o null en cas d'error.
     */
    suspend fun registerUser(email: String, user: String, pass: String): IOUiState? {
        Log.d("MUTEX_DEBUG", "A. Intentant entrar al Mutex de REGISTER...")
        return accessMutex.withLock {
            Log.d("MUTEX_DEBUG", "B. ¡REGISTER ha entrat al Mutex!")
            try {
                // Cridem al controlador que fa la petició real
                val response = CommController.doRegister(email, user, pass)
                Log.d("MUTEX_DEBUG", "C. REGISTER sortint del Mutex correctament.")
                response
            } catch (e: Exception) {
                Log.e("MUTEX_DEBUG", "Error en el registre: ${e.message}")
                IOUiState(result = "error", goodResult = false)
            }
        }
    }

    /**
     * Actualitza l'adreça de correu electrònic de l'usuari autenticat.
     * Comprova primer la validesa de la sessió, crea una còpia de l'usuari actual
     * i sincronitza el canvi amb el servidor sota el bloqueig de [accessMutex].
     * @param newEmail La nova adreça de correu que es vol assignar.
     * @return Un missatge d'error en format [String] si l'operació falla, o `null` si s'ha actualitzat correctament.
     */
    suspend fun updateEmail(newEmail: String): String? {
        val usuariActual = User.current ?: return "Sessió no vàlida"
        val usuariModificat = usuariActual.copy(email = newEmail)

        return accessMutex.withLock {
            try {
                val resultat = CommController.doUpdateUser(usuariActual.id, usuariModificat)
                if (resultat != null) {
                    User.current = resultat
                    null // Operació sense problemes
                } else {
                    "Error: El correu ja existeix o dades no vàlides"
                }
            } catch (e: Exception){
                "No s'ha pogut connectar amb el servidor"
            }
        }
    }

    /**
     * Elimina permanentment el compte de l'usuari del sistema.
     * Si el servidor confirma l'eliminació, s'executa automàticament [User.logout]
     * per invalidar la sessió local immediatament.
     * @param id L'identificador únic de l'usuari a eliminar.
     * @return `true` si l'eliminació s'ha realitzat amb èxit.
     */
    suspend fun deleteUser(id: Int): Boolean {
        Log.d("MUTEX_DEBUG", "Iniciant DELETE amb Mutex...")
        return accessMutex.withLock {
            val success = CommController.deleteUser(id)
            if (success) {
                User.logout() // Si el servidor l'esborra, netegem localment
            }
            success
        }
    }
}