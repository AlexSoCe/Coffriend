package com.alexsotocepas.coffriend.ui

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.viewModelScope
import com.alexsotocepas.coffriend.communications.ServerRequests
import com.alexsotocepas.coffriend.data.User
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.runtime.mutableStateOf

/**
 * Gestiona la lògica de negoci per a la pantalla de menú de l'usuari client.
 * Aquesta classe s'encarrega de mantenir l'estat de l'usuari actual i de
 * coordinar les peticions de tancament de sessió mitjançant el servidor.
 */
class MenuUserViewModel : IOViewModel() {
    /**
     * Estat reactiu que conté la informació de l'usuari autenticat actualment.
     * Utilitza un setter privat per evitar modificacions externes fora d'aquesta classe.
     */
    var currentUser by mutableStateOf(User.current)
    private set

    /**
     * Realitza el procés de tancament de sessió de l'usuari.
     * L'operació es divideix en dues fases: una crida de xarxa en el fil IO i
     * una actualització de la interfície d'usuari en el fil principal.
     * @param onSuccess Callback que es dispara per notificar a la vista que pot navegar cap al Login.
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