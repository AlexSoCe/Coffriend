package com.alexsotocepas.coffriend.communications

/**
 * Representació d'una petició específica (ordre) que s'envia al servidor.
 * Aquesta classe estèn de [EndPointData] per afegir un identificador d'ordre
 * que indica al servidor quina acció s'ha d'executar amb les dades adjuntes.
 * S'utilitza com a estructura de missatgeria estàndard en tota l'aplicació.
 */
class EndPointValues : EndPointData {
    /**
     * L'identificador de l'ordre o acció a realitzar pel servidor.
     * @return El nom de l'ordre actual.
     */
    var order: String? = null
        private set

    /**
     * Constructor per defecte.
     * Crea una instància sense una ordre inicial assignada.
     * Les dades es poden omplir posteriorment mitjançant els mètodes de la classe pare.
     */
    constructor() : super()

    /**
     * Constructor que inicialitza l'objecte amb una ordre específica.
     * @param order El nom de l'ordre representada (per exemple, la ruta o acció del servidor).
     */
    constructor(order: String?) : super() {
        this.order = order
    }
}