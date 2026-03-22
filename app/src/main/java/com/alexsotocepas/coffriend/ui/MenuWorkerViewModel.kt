package com.alexsotocepas.coffriend.ui

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.viewModelScope
import com.alexsotocepas.coffriend.communications.ServerRequests
import com.alexsotocepas.coffriend.data.User
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

/**
 * ViewModel encarregat de gestionar la lògica de la pantalla del menú per a treballadors.
 * Aquesta classe hereta de [IOViewModel] per gestionar estats de connexió i permet
 * l'accés a les dades de l'usuari actual, així com la finalització de la sessió.
 */
class MenuWorkerViewModel : IOViewModel() {
    /**
     * Estat reactiu que emmagatzema les dades de l'usuari que ha iniciat sessió.
     * Es defineix amb un setter privat per garantir que només el ViewModel pugui modificar-lo.
     */
    var currentUser by mutableStateOf(User.current)
        private set
    /**
     * Executa el procés de tancament de sessió per al perfil de treballador.
     * Realitza la petició al servidor en un fil de xarxa (IO) i, un cop completat,
     * neteja les dades locals i executa el callback a la interfície d'usuari (Main).
     * @param onSuccess Callback que s'executa quan el tancament de sessió s'ha completat correctament.
     */
    fun logout(onSuccess: () -> Unit) {
        viewModelScope.launch (Dispatchers.IO){
            ServerRequests.logout()
            User.logout()
            currentUser = null
            withContext(Dispatchers.Main) {
                onSuccess()
            }
        }
    }
}