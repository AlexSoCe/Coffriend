package com.alexsotocepas.coffriend.data

/**
 * Representació de les dades de l'usuari autenticat a l'aplicació.
 * Aquesta [data class] emmagatzema el perfil de l'usuari i el seu nivell de permisos (rol).
 * La informació es manté de forma global mitjançant un objecte de companyia ([companion object])
 * per facilitar l'accés des de qualsevol punt de l'arquitectura.
 * @property username El nom d'usuari únic que identifica el compte.
 * @property role El tipus d'usuari que determina els permisos d'accés ("user", "worker", "admin").
 * Per defecte s'assigna "user".
 */
data class User (
    val username:String,
    val role: String = "user" // "user", "worker", "admin"
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