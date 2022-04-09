package ar.edu.unq.epers.tactics.daoFirebase

import ar.edu.unq.epers.tactics.modelo.Atributo
import ar.edu.unq.epers.tactics.modelo.Cofre
import ar.edu.unq.epers.tactics.modelo.Item
import ar.edu.unq.epers.tactics.persistencia.dao.firebase.CofreDAO
import org.junit.jupiter.api.Test

class FirebaseCofreDaoTest {
    @Test
    fun seCreaUnCofreConDosItems() {
        val firebasedao = CofreDAO()
        val cofre = Cofre("Cofre_1")
        val item = Item("Gorro",Atributo.CONSTITUCION,10,1,100)
        val item2 = Item("Espada",Atributo.FUERZA,4,2,123)
        cofre.addItem(item)
        cofre.addItem(item2)
        firebasedao.crearCofre(cofre)
    }

    @Test
    fun seCreanVariosCofresConTransaction() {
        val firebasedao = CofreDAO()
        val cofre = Cofre("Cofre_A")
        val item = Item("Gorro",Atributo.CONSTITUCION,10,1,100)
        val item2 = Item("Espada",Atributo.FUERZA,4,2,123)
        cofre.addItem(item)
        cofre.addItem(item2)

        val cofre2 = Cofre("Cofre_B")
        val item3 = Item("Gorro",Atributo.CONSTITUCION,10,1,100)
        val item4 = Item("Espada",Atributo.FUERZA,4,2,123)
        cofre2.addItem(item3)
        cofre2.addItem(item4)

        var cofres: MutableList<Cofre> = mutableListOf()
        cofres.add(cofre)
        cofres.add(cofre2)

        firebasedao.crearMultiplesCofres(cofres)
    }
}