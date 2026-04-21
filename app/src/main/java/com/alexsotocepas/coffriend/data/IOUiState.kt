package com.alexsotocepas.coffriend.data

/**
 * Classe de dades que representa l'estat d'èxit o fallada de les operacions d'entrada/sortida (I/O).
 * S'utilitza de forma genèrica en els [IOViewModel] per notificar a la interfície d'usuari
 * si l'última petició al servidor s'ha completat correctament. És una peça clau per a la
 * gestió d'errors visual, permetent mostrar avisos o bloquejar accions segons el resultat.
 * @property goodResult Indica si el resultat de l'operació ha estat positiu (`true`) o
 * si s'ha produït un error de comunicació o de validació (`false`). Per defecte s'inicialitza a `true`.
 */
data class IOUiState(
    /** Marca l'èxit de l'operació.
     * S'actualitza normalment després de rebre la resposta del [ServerRequests].
     */
    var goodResult:Boolean=true,

    val result: String = ""
)
