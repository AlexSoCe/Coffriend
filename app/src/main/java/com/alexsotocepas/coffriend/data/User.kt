package com.alexsotocepas.coffriend.data

import com.google.gson.annotations.SerializedName

/**
 * Representació de les dades de l'usuari autenticat a l'aplicació Coffriend.
 * Aquesta [data class] emmagatzema el perfil complet de l'usuari, el seu nivell de permisos (rol)
 * i el seu progrés dins del sistema de gamificació. La informació es manté de forma global
 * mitjançant un [companion object] per facilitar l'accés a la sessió des de qualsevol punt
 * de l'arquitectura (ViewModels, Controllers, etc.).
 * @property id Identificador únic de l'usuari a la base de dades.
 * @property nom El nom complet o àlies de l'usuari.
 * @property email Correu electrònic utilitzat com a identificador i credencial d'accés.
 * @property rol Determina els permisos de navegació i accés ("client", "treballador", "admin").
 * @property nivell Grau d'experiència assolit per l'usuari en el sistema.
 * @property punts Quantitat de punts acumulats bescanviables o de rànquing.
 * @property idBotiga Identificador de la botiga física vinculada (especialment rellevant per a treballadors).
 * @property llistaInsignies Col·lecció d'objectes [Insignia] que l'usuari ha col·leccionat.
 * Es mapeja des del camp "insignies" del JSON mitjançant [SerializedName].
 */
data class User (
    val id: Int,
    val nom: String,
    val email: String,
    val rol: String = "client",
    val nivell: Int? = 0,
    val punts: Int? = 0,
    val idBotiga: Int? = null,
    @SerializedName("insignies")
    val llistaInsignies: List<Insignia>? = null

) {
    /**
     * Membres estàtics per a la gestió global de la sessió de l'usuari.
     */
    companion object {
        /**
         * L'instància de l'usuari [User] actualment autenticat.
         * Si el seu valor és `null`, es considera que no existeix cap sessió activa
         * i l'aplicació hauria de redirigir a la pantalla de Login.
         */
        var current: User? = null

        /**
         * Finalitza la sessió de l'usuari localment.
         * Aquest mètode restableix l'usuari [current] a `null`, forçant l'aplicació
         * a demanar credencials de nou en les pantalles protegides.
         */
        fun logout() { current = null }
    }
}