package com.alexsotocepas.coffriend.ui

import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import com.alexsotocepas.coffriend.data.LoginUiState
import com.alexsotocepas.coffriend.communications.ServerRequests
import kotlinx.coroutines.withContext

/**
 * ViewModel encarregat de gestionar la lògica d'inici de sessió (Login).
 * Aquesta classe controla l'estat de la interfície durant l'autenticació,
 * gestiona les crides al servidor i notifica els resultats a la pantalla
 * mitjançant fluxos de dades reactius (StateFlow).
 */
class LoginViewModel : IOViewModel() {
    /** * Flux d'estat privat que conté la informació de la interfície de Login.
     */
    private val _LoginUiState = MutableStateFlow(LoginUiState())
    /** * Flux d'estat públic per ser observat per la composable [LoginScreen].
     */
    val LoginUiState: StateFlow<LoginUiState> = _LoginUiState.asStateFlow()

    /**
     * Actualitza l'estat que indica si s'ha realitzat un intent de login.
     * @param tried Valor booleà que marca si s'ha executat l'acció d'inici de sessió.
     */
    fun setLoginTried(tried: Boolean) {
        _LoginUiState.update { currentState ->
            currentState.copy(
                loginTried=tried
            )
        }
    }

    /**
     * Executa el procés d'autenticació contra el servidor.
     * La funció canvia al context de xarxa (IO) per realitzar la crida al servidor
     * i torna al fil principal (Main) per actualitzar l'estat de la UI i
     * gestionar la navegació en cas d'èxit.
     * @param username Nom d'usuari introduït a la interfície.
     * @param password Contrasenya introduïda a la interfície.
     * @param onSuccess Callback que s'executa si l'autenticació és satisfactòria per navegar al Menú.
     */
    fun doLogin(username: String, password: String, onSuccess: () -> Unit) {
        viewModelScope.launch (Dispatchers.IO){
            withContext(Dispatchers.Main) {
                setLoginTried(false)
            }

            val result = ServerRequests.login(username, password)

            withContext(Dispatchers.Main) {
                setGoodResult(result)
                setLoginTried(true)

                if (result) {
                    onSuccess()
                }
            }
        }
    }
}