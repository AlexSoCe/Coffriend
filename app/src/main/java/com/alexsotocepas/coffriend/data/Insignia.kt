package com.alexsotocepas.coffriend.data

/**
 * Representa una insígnia o assoliment obtingut per l'usuari dins de l'aplicació.
 * Les insígnies serveixen com a sistema de recompensa (gamificació) per premiar
 * la fidelitat o accions específiques del client. Aquesta classe s'utilitza per
 * mostrar els èxits a la pantalla de perfil.
 * @property id Identificador únic de la insígnia a la base de dades. Pot ser nul si encara no s'ha persistit.
 * @property nom Nom representatiu de l'assoliment (ex: "Primer Cafè").
 * @property descripcio Text que explica el motiu pel qual s'atorga la insígnia.
 * @property imatgeUrl Enllaç URL a la icona o imatge gràfica de la insígnia.
 * @property dataObtencio Data en què l'usuari va guanyar la insígnia, normalment en format ISO 8601 ("yyyy-MM-dd").
 */
data class Insignia(
    val id: Int? = null,
    val nom: String? = null,
    val descripcio: String? = null,
    val imatgeUrl: String? = null,
    val dataObtencio: String? = null // Format "yyyy-MM-dd"
)