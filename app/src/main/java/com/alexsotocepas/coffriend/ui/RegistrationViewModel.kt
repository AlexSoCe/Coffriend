package com.alexsotocepas.coffriend.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.alexsotocepas.coffriend.data.IOUiState
import com.alexsotocepas.coffriend.communications.ServerRequests
import com.alexsotocepas.coffriend.data.RegistrationUiState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

/**
 * ViewModel responsable de gestionar la lògica i l'estat de la pantalla de registre d'usuaris.
 * Manté l'estat de la UI sincronitzat amb les respostes del servidor.
 */
class RegistrationViewModel : ViewModel() {
    // Estat de la interfície de registre
    private val _registrationUiState = MutableStateFlow(RegistrationUiState())
    val registrationUiState: StateFlow<RegistrationUiState> = _registrationUiState.asStateFlow()

    // Estat de l'operació d'entrada/sortida (E/S) - Reutilitzem la teva lògica
    private val _uiIOState = MutableStateFlow(IOUiState())
    val uiIOState: StateFlow<IOUiState> = _uiIOState.asStateFlow()

    /**
     * Executa el procés de registre d'un nou usuari en una corrutina de fons.
     * Actualitza l'estat de la interfície basant-se en l'èxit o el motiu de l'error rebut.
     * @param email Correu de l'usuari.
     * @param user Nom d'usuari.
     * @param pass Contrasenya de l'usuari.
     */
    fun doRegistration(email: String, user: String, pass: String) {
        viewModelScope.launch {
            // Indiquem que hem iniciat un intent de registre
            _registrationUiState.update { it.copy(registrationTried = true, userAlreadyExists = false) }

            // Cridem al servidor (aquesta funció hauria d'estar a ServerRequests)
            val response = withContext(Dispatchers.IO) {
                ServerRequests.registerUser(email, user, pass)
            }

            if (response != null) {
                _uiIOState.update {
                    it.copy(
                        goodResult = response.goodResult,
                        result = response.result
                    )
                }
                if (response.goodResult) {
                    _registrationUiState.update { it.copy(isSuccess = true) }
                } else if (response.result == "user_exists") {
                    // Si el servidor ens diu que l'usuari ja existeix
                    _registrationUiState.update { it.copy(userAlreadyExists = true) }
                }
            }
        }
    }
}