package com.alexsotocepas.coffriend.ui

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.alexsotocepas.coffriend.communications.ServerRequests
import com.alexsotocepas.coffriend.data.User
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

/**
 * ViewModel encarregat de gestionar la lògica del perfil d'usuari i la baixa del sistema.
 * Connecta la interfície d'usuari de perfil amb les operacions de xarxa protegides
 * per garantir que les dades de l'usuari actual s'actualitzin o s'eliminin correctament.
 */
class ProfileUserViewModel : ViewModel() {
    /**
     * Flux d'estat que indica si l'operació d'eliminació del compte s'ha completat amb èxit.
     */
    private val _deleteSuccess = MutableStateFlow(false)
    val deleteSuccess = _deleteSuccess.asStateFlow()

    /**
     * Executa el procés d'eliminació definitiva del compte de l'usuari actual.
     * Aquesta funció recupera la ID de l'usuari des de l'objecte global [User],
     * realitza la crida asíncrona a través de [ServerRequests] en el context IO i,
     * si l'operació és exitosa, executa el callback de navegació en el fil principal.
     * @param onDeleted Funció callback que s'executa per navegar fora de la pantalla un cop eliminat el compte.
     */
    fun deleteAccount(onDeleted: () -> Unit) {
        viewModelScope.launch {
            val userId = User.current?.id ?: return@launch

            // Ara passem pel Mutex i el fil IO
            val result = withContext(Dispatchers.IO) {
                ServerRequests.deleteUser(userId)
            }

            if (result) {
                withContext(Dispatchers.Main) {
                    onDeleted() // Naveguem al Login
                }
            } else {
                // Aquí podries gestionar un error (per exemple, un 403 de permisos)
                Log.e("ProfileVM", "No s'ha pogut eliminar l'usuari")
            }
        }
    }
}