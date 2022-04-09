package ar.edu.unq.epers.tactics.service.impl

import ar.edu.unq.epers.tactics.modelo.Atributo
import ar.edu.unq.epers.tactics.modelo.Item
import ar.edu.unq.epers.tactics.persistencia.dao.AventureroDAO
import ar.edu.unq.epers.tactics.persistencia.dao.firebase.ItemDAO
import ar.edu.unq.epers.tactics.service.ItemService

class ItemServiceImpl(private val itemDAO: ItemDAO, val aventureroDAO: AventureroDAO)  : ItemService {
    override fun crearItem(item: Item): Item {
        return itemDAO.crearItem(item)
    }

    override fun eliminar(nombre: String) {
        itemDAO.eliminar(nombre)
    }

    override fun actualizar(nombre: String, atributo: Atributo, cantidadDeAtributo: Int, nivelRequerido: Int, precio: Int) {
        itemDAO.actualizar(nombre,atributo,cantidadDeAtributo,nivelRequerido,precio)
    }

    override fun buscarItem(nombre: String) : Item? {
        return itemDAO.buscarItem(nombre)
    }

    override fun eliminarTodos() {
        itemDAO.elinarTodos()
    }

    override fun todosLosItems(): List<Item> {
        TODO("Not yet implemented")
    }


}