package com.alexsotocepas.coffriend.data

/**
 * Representació de les dades de l'usuari autenticat a l'aplicació.
 * Aquesta [data class] emmagatzema el perfil de l'usuari i el seu nivell de permisos (rol).
 * La informació es manté de forma global mitjançant un objecte de companyia ([companion object])
 * per facilitar l'accés des de qualsevol punt de l'arquitectura.
 * @property id Identificador únic de l'usuari a la base de dades.
 * @property nom El nom de l'usuari.
 * @property email Correu electrònic utilitzat com a credencial principal d'accés.
 * @property rol El nivell de permisos que determina la navegació ("client", "treballador", "admin").
 * @property nivell Grau d'experiència de l'usuari dins del sistema de gamificació.
 * @property punts Quantitat de punts acumulats per l'usuari.
 * @property idBotiga Identificador de la botiga física vinculada (principalment per a treballadors).
 */
data class User (
    val id: Int,
    val nom: String,
    val email: String,
    val rol: String = "client",
    val nivell: Int? = 0,
    val punts: Int? = 0,
    val idBotiga: Int? = null
) {
    /**
     * Membres estàtics per a la gestió global de la sessió de l'usuari.
     */
    companion object {
        /**
         * L'instància de l'usuari actualment autenticat en l'aplicació.
         * Si és `null`, l'aplicació interpreta que no hi ha cap sessió activa.
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