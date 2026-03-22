package com.alexsotocepas.coffriend.communications

/**
 * Representació de la resposta retornada pel servidor després d'una petició.
 * Aquesta classe estén de [EndPointData] per permetre rebre tant un codi d'estat
 * numèric com objectes o dades addicionals serialitzades (com missatges d'error
 * o dades de l'usuari).
 * S'utilitza per interpretar el resultat de qualsevol operació de xarxa.
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
}