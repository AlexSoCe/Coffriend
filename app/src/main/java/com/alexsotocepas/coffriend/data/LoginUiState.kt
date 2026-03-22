package com.alexsotocepas.coffriend.data

/**
 * Classe de dades que representa l'estat de la interfície d'usuari (UI) durant el procés de login.
 * Aquesta classe s'utilitza en combinació amb un [StateFlow] dins del ViewModel per gestionar
 * de forma reactiva el que l'usuari veu a la pantalla. Permet controlar si s'ha d'executar
 * la lògica de navegació o mostrar missatges d'error un cop s'ha completat l'intent de connexió.
 * @property loginTried Indica si s'ha premut el botó d'inici de sessió i s'ha rebut una resposta
 * del servidor (ja sigui positiva o negativa). S'utilitza per evitar que la UI reaccioni a estats
 * inicials buits.
 */
data class LoginUiState (
    /** Determina si l'usuari ja ha intentat realitzar una acció de login.
     * Per defecte és `false` fins que finalitza la primera crida al servidor.
     */
    var loginTried:Boolean=false
)