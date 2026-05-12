package com.alexsotocepas.coffriend.data

/**
 * Representa un producte comercialitzat dins de l'aplicació Coffriend.
 * Aquesta classe de dades s'utilitza per emmagatzemar la informació detallada dels articles
 * (principalment cafès i brioixeria) que els clients poden visualitzar i afegir a la seva cistella.
 * @property id Identificador únic del producte a la base de dades.
 * @property nom Nom comercial del producte.
 * @property descripcio Text detallat que descriu les característiques o ingredients del producte.
 * @property preu Preu unitari del producte en euros (€).
 * @property imatgeUrl Enllaç URL a la imatge del producte. Pot ser nul si el producte no té imatge assignada.
 * @property stock Quantitat d'unitats disponibles actualment al magatzem de la botiga.
 * @property idBotiga Identificador de la botiga física a la qual pertany o està assignat el producte.
 */
data class Product(
    val id: Int,
    val nom: String,
    val descripcio: String,
    val preu: Double,
    val imatgeUrl: String?,
    val stock: Int,
    val idBotiga: Int
)