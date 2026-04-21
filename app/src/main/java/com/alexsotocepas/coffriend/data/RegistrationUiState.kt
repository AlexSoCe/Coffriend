package com.alexsotocepas.coffriend.data

/**
 * Representa l'estat de la interfície de registre.
 * Fem servir 'data class' per poder utilitzar el mètode .copy() des del ViewModel.
 */
data class RegistrationUiState (
    val registrationTried: Boolean = false,
    val userAlreadyExists: Boolean = false,
    val isSuccess: Boolean = false
)