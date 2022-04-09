package ar.edu.unq.epers.tactics.daoFirebase

import ar.edu.unq.epers.tactics.modelo.Atributo
import ar.edu.unq.epers.tactics.modelo.Item
import ar.edu.unq.epers.tactics.persistencia.dao.firebase.ItemDAO
import org.junit.jupiter.api.Test

class FirebaseItemDaoTest {

    @Test
    fun seCreaUnItemYSePersisteEnFirebase() {
        val firebasedao = ItemDAO()
        val item = Item("Hacha",Atributo.CONSTITUCION,10,1,100)
        firebasedao.crearItem(item)
    }

    @Test
    fun seEliminaUnItem() {
        val firebasedao = ItemDAO()
        val item = Item("Bota",Atributo.CONSTITUCION,10,1,100)
        val item2 = Item("Anillo",Atributo.CONSTITUCION,10,1,100)
        var itemcreado1= firebasedao.crearItem(item)
        var itemcreado2= firebasedao.crearItem(item2)
        firebasedao.eliminar(itemcreado1.nombre)
    }

    @Test
    fun seActualizaUnItem() {
        val firebasedao = ItemDAO()
        val item = Item("Daga",Atributo.CONSTITUCION,10,1,100)
        firebasedao.crearItem(item)
        firebasedao.actualizar(item.nombre,Atributo.FUERZA,5,3,5)
    }

    @Test
    fun seBuscaYEncuentraUnasPolainas(){
        val firebasedao = ItemDAO()
        val item = Item("Polainas",Atributo.CONSTITUCION,10,1,100)
        firebasedao.crearItem(item)
        firebasedao.actualizar(item.nombre,Atributo.FUERZA,5,3,5)
        val itemRecuperado = firebasedao.buscarItem(item.nombre)
    }

}