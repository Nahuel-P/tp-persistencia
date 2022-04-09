package ar.edu.unq.epers.tactics.service

import ar.edu.unq.epers.tactics.modelo.Atributo
import ar.edu.unq.epers.tactics.modelo.Clase
import ar.edu.unq.epers.tactics.modelo.Item

interface ItemService {

    fun crearItem(item: Item): Item
    fun todosLosItems() :  List<Item>
    fun eliminarTodos()
    fun eliminar(nombre: String)
    fun actualizar(nombre: String, atributo: Atributo, cantidadDeAtributo : Int, nivelRequerido : Int, precio : Int )
    fun buscarItem(nombre: String) : Item?
}