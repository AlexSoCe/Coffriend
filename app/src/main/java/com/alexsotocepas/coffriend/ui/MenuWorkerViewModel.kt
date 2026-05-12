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
 * ViewModel encarregat de gestionar la lògica de negoci de la pantalla del menú per a treballadors.
 * Aquesta classe hereta de [IOViewModel] per aprofitar la gestió d'estats de connexió i
 * s'encarrega de subministrar les dades de l'usuari actual i gestionar el flux de tancament de sessió.
 */
class MenuWorkerViewModel : IOViewModel() {
    /**
     * Estat reactiu de Compose que emmagatzema les dades de l'usuari [User] que ha iniciat sessió.
     * S'inicialitza amb el valor de [User.current]. El setter és privat per garantir
     * la integritat de les dades des de la vista.
     */
    var currentUser by mutableStateOf(User.current)
        private set
    /**
     * Gestiona el procés de tancament de sessió per al perfil de treballador.
     * L'operació s'executa en una corrutina dins del [Dispatchers.IO] per a la crida
     * a [ServerRequests.logout]. Posteriorment, es netegen les dades locals mitjançant [User.logout]
     * i es retorna al fil principal ([Dispatchers.Main]) per executar el callback de navegació.
     * @param onSuccess Callback que s'executa a la capa de presentació un cop la sessió s'ha tancat correctament.
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