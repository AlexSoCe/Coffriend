package com.alexsotocepas.coffriend.communications

import com.alexsotocepas.coffriend.data.User

/**
 * Representació de la resposta retornada pel servidor després d'una petició.
 * Aquesta classe estén de [EndPointData] per permetre gestionar tant un codi d'estat
 * numèric com objectes o dades addicionals serialitzades (com missatges d'error
 * o dades de l'usuari). S'utilitza per interpretar el resultat de les operacions de xarxa
 * dins de la capa de comunicacions.
 */
class ReturnValues : EndPointData {
    /**
     * El codi de retorn numèric de l'operació.
     * @return El codi d'estat actual de la resposta.
     */
    var returnCode: Int = 0
        private set

    /**
     * Constructor per defecte.
     * Inicialitza una resposta amb el codi de retorn a 0 per defecte.
     * Les dades addicionals es poden afegir o extreure posteriorment.
     */
    constructor() : super()

    /**
     * Constructor que inicialitza l'objecte amb un codi de retorn específic.
     * @param returnCode El codi numèric que representa el resultat de la petició al servidor.
     */
    constructor(returnCode: Int) : super() {
        this.returnCode = returnCode
    }

    /**
     * Estructura de dades per al mapeig de la resposta d'inici de sessió.
     * @property token El token d'autenticació (JWT) proporcionat pel servidor per a futures peticions.
     * @property user L'objecte [User] amb tota la informació del perfil de l'usuari que ha fet login.
     */
    data class LoginResponse(
        val token: String,
        val user: User
    )

    /**
     * Segell de classe (Sealed Class) per representar els possibles resultats d'una crida a l'API.
     * * Permet una gestió exhaustiva i segura dels estats d'èxit i error.
     */
    sealed class ApiResult {
        /**
         * Representa un resultat satisfactori de l'operació.
         * @property data Objecte de tipus [LoginResponse] que conté les dades retornades.
         */
        data class Success(val data: LoginResponse) : ApiResult()

        /**
         * Representa un error en l'operació o la comunicació.
         * @property message Descripció textual de l'error per ser mostrada o registrada.
         */
        data class Error(val message: String) : ApiResult()
    }
}