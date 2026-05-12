package com.alexsotocepas.coffriend.data

/**
 * Representa l'estat de la interfície d'usuari durant el procés de registre.
 * Aquesta classe s'utilitza per gestionar les diferents etapes i respostes del servidor
 * en el flux de creació d'un nou compte. Es defineix com a `data class` per facilitar
 * l'actualització d'estats concrets mitjançant el mètode `.copy()` des del ViewModel.
 * @property registrationTried Indica si l'usuari ha realitzat almenys un intent de registre prement el botó d'enviament.
 * @property userAlreadyExists Esdevé `true` si el servidor retorna un error indicant que el correu o usuari ja està registrat.
 * @property isSuccess S'estableix a `true` quan el procés de registre s'ha completat correctament i s'ha rebut una resposta positiva del servidor.
 */
data class RegistrationUiState (
    val registrationTried: Boolean = false,
    val userAlreadyExists: Boolean = false,
    val isSuccess: Boolean = false
)