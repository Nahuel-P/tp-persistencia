package ar.edu.unq.epers.tactics.modelo

import com.google.cloud.firestore.annotation.DocumentId
import javax.persistence.*


class Cofre() {
    var nombre: String = ""
    var items: MutableList<Item> = mutableListOf()

    constructor(nombre: String) : this() {
        this.nombre = nombre
    }

    fun addItem(item: Item){
        this.items.add(item)
    }

    fun contiene(itemAEquipar: Item): Boolean {
        return items.contains(itemAEquipar)
    }

}