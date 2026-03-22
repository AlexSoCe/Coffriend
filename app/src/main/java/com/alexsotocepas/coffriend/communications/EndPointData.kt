package com.alexsotocepas.coffriend.communications

import com.google.gson.Gson
import com.google.gson.JsonSyntaxException

/**
 * Representació genèrica de les dades enviades o rebudes d'un punt final (endpoint) del servidor.
 * Aquesta classe actua com un contenidor flexible que permet emmagatzemar diversos objectes o
 * dades primitives en una llista, convertint-los a format JSON per a la seva transmissió.
 * S'utilitza principalment per normalitzar la comunicació entre l'aplicació i l'API.
 */
open class EndPointData {
    /** Llista interna que emmagatzema les dades serialitzades en format String (JSON).
     */
    private val data = ArrayList<String>()

    /**
     * Afegeix un objecte complex a la llista de dades.
     * L'objecte es serialitza automàticament a format JSON fent servir la biblioteca Gson
     * abans de ser afegit a la col·lecció.
     * @param data L'objecte que es vol afegir (pot ser qualsevol classe de dades).
     */
    fun addDataObject(data: Any?) {
        val gson = Gson()
        this.data.add(gson.toJson(data))
    }

    /**
     * Afegeix una dada de tipus primitiu a la llista de dades.
     * A diferència de [addDataObject], aquest mètode simplement converteix la dada
     * a la seva representació en cadena de text (String) sense serialització complexa.
     * @param data La dada primitiva a afegir (Int, String, Boolean, etc.).
     */
    fun addPrimitiveData(data: Any) {
        this.data.add(data.toString() + "")
    }


    /**
     * Recupera una dada de la llista en una posició específica i la deserealitza
     * al tipus d'objecte indicat.
     * @param DataNumber La posició de la dada dins de la llista (començant per 0).
     * @param clasz La classe de destí a la qual es vol convertir la dada JSON.
     * @return L'objecte convertit al tipus [clasz], o `null` si la posició no existeix
     * o si hi ha un error en el format JSON.
     * @throws JsonSyntaxException Captura l'excepció si el contingut no coincideix amb l'estructura de la classe.
     */
    fun getData(DataNumber: Int, clasz: Class<*>?): Any? {
        try {
            val gson = Gson()
            // Comprovem si l'índex és vàlid per evitar IndexOutOfBoundsException
            return gson.fromJson<Any>(data[DataNumber], clasz)
        } catch (ex: JsonSyntaxException) {
            return null
        }
    }
}