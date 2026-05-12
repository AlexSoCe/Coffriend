package com.alexsotocepas.coffriend.ui

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.alexsotocepas.coffriend.communications.ServerRequests
import com.alexsotocepas.coffriend.data.User
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import androidx.compose.runtime.getValue

/**
 * ViewModel encarregat de gestionar la lògica del perfil d'usuari i la baixa del sistema.
 * Connecta la interfície d'usuari de perfil amb les operacions de xarxa protegides
 * per garantir que les dades de l'usuari actual s'actualitzin o s'eliminin correctament
 * mitjançant [ServerRequests].
 */
class ProfileUserViewModel : ViewModel() {

    /**
     * Missatge d'error reactiu que es mostra a la UI quan una operació falla.
     * Si és `null`, no hi ha cap error pendent de visualització.
     */
    var errorMessage by mutableStateOf<String?>(null)

    /**
     * Indicador d'èxit per a les operacions d'actualització de dades.
     * Es posa a `true` quan el servidor confirma que els canvis s'han aplicat correctament.
     */
    var isSuccess by mutableStateOf(false)

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
                Log.e("ProfileVM", "No s'ha pogut eliminar l'usuari")
            }
        }
    }

    /**
     * Sol·licita l'actualització de l'adreça de correu electrònic de l'usuari.
     * Gestiona els estats de [isSuccess] i [errorMessage] basant-se en la resposta
     * obtinguda de [ServerRequests.updateEmail]. Si l'operació és satisfactòria,
     * les dades de l'usuari en memòria s'actualitzen automàticament.
     * @param nuevoEmail La nova adreça de correu electrònic que l'usuari vol vincular al seu compte.
     */
    fun updateEmail(nuevoEmail: String) {
        viewModelScope.launch {
            errorMessage = null
            isSuccess = false
            val resultado = ServerRequests.updateEmail(nuevoEmail)

            if (resultado == null) {
                isSuccess = true
            } else {
                errorMessage = resultado
            }
        }
    }
}