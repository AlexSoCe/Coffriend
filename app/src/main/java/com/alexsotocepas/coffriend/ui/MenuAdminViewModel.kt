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
 * ViewModel que gestiona la lògica de la pantalla de menú per a administradors.
 * Aquesta classe permet l'accés a les dades globals de l'usuari amb privilegis
 * d'administració i coordina el procés de finalització de la sessió tant al
 * servidor com de manera local.
 */
class MenuAdminViewModel : IOViewModel() {
    /**
     * Estat reactiu que manté la informació de l'administrador actual.
     * S'utilitza un setter privat per garantir que l'estat només es pugui
     * modificar internament des d'aquest ViewModel.
     */
    var currentUser by mutableStateOf(User.current)
        private set

    /**
     * Gestiona el tancament de sessió per al perfil d'administrador.
     * Executa la petició de xarxa en un fil secundari (IO) i, un cop
     * invalidada la sessió, neteja l'estat local i retorna al fil principal
     * per executar la navegació.
     * @param onSuccess Callback que s'executa quan el procés ha finalitzat i la UI ha de navegar al Login.
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