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
import com.alexsotocepas.coffriend.data.Product

/**
 * ViewModel encarregat de gestionar la lògica de negoci per a la pantalla de menú de l'usuari client.
 * Aquesta classe manté l'estat de l'usuari actual, gestiona el catàleg de productes
 * i coordina les operacions de tancament de sessió mitjançant comunicacions amb el servidor.
 * Hereta de [IOViewModel] per al control d'estats de càrrega i connexió.
 */
class MenuUserViewModel : IOViewModel() {
    /**
     * Estat reactiu que conté la informació de l'usuari [User] autenticat actualment.
     * S'utilitza un setter privat per garantir que la informació del perfil només
     * pugui ser modificada des d'aquest ViewModel.
     */
    var currentUser by mutableStateOf(User.current)
    private set

    /**
     * Llista reactiva de productes [Product] disponibles al catàleg.
     * S'actualitza automàticament un cop finalitza la petició a [loadProducts].
     */
    var products by mutableStateOf<List<Product>>(emptyList())
        private set

    /**
     * Indicador d'estat de càrrega per a operacions asíncrones.
     * S'estableix a `true` durant la recuperació de dades del servidor per permetre
     * que la interfície d'usuari mostri indicadors de progrés.
     */
    var isLoading by mutableStateOf(false)
        private set

    init {
        loadProducts()
    }

    /**
     * Recupera el llistat de productes des del servidor.
     * L'operació es realitza en el fil [Dispatchers.IO] per evitar bloquejar la UI.
     * Un cop obtinguts els resultats mitjançant el controlador de comunicacions,
     * s'actualitza l'estat de [products] al fil principal.
     */
    fun loadProducts() {
        viewModelScope.launch {
            isLoading = true
            val result = withContext(Dispatchers.IO) {
                com.alexsotocepas.coffriend.communications.CommController.doGetProducts()
            }
            products = result
            isLoading = false
        }
    }

    /**
     * Realitza el procés de tancament de sessió de l'usuari.
     * L'operació es divideix en dues fases: una crida de xarxa en el fil [Dispatchers.IO]
     * per invalidar el token al servidor i una neteja de dades locals mitjançant [User.logout].
     * @param onSuccess Callback que es dispara des del fil principal [Dispatchers.Main]
     * per notificar a la vista que s'ha de procedir a la navegació cap al Login.
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