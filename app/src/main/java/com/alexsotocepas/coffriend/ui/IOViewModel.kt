package com.alexsotocepas.coffriend.ui

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import com.alexsotocepas.coffriend.data.IOUiState

/**
 * Classe base (superclasse) per als ViewModels que realitzen operacions d'entrada/sortida (I/O).
 * Proporciona un mecanisme estandarditzat per emmagatzemar i actualitzar si una
 * operació de xarxa o de base de dades ha estat satisfactòria. Aquesta informació
 * es transmet de forma reactiva a les pantalles mitjançant un [StateFlow].
 */
open class IOViewModel : ViewModel(){
    /**
     * Flux d'estat privat que gestiona el resultat de l'operació I/O.
     */
    private val _uiState = MutableStateFlow(IOUiState())
    /**
     * Flux d'estat públic observable per la interfície d'usuari per reaccionar
     * a l'èxit o error de les peticions.
     */
    val uiIOState: StateFlow<IOUiState> = _uiState.asStateFlow()

    /**
     * Actualitza l'estat del resultat de l'operació actual.
     * Aquest mètode és utilitzat per les subclasses per notificar si la crida
     * al servidor ha finalitzat correctament.
     * @param goodResult Booleà que indica si l'operació ha tingut èxit (true) o ha fallat (false).
     */
    fun setGoodResult(goodResult: Boolean) {
        _uiState.update { currentState ->
            currentState.copy(
                goodResult = goodResult
            )
        }
    }
}