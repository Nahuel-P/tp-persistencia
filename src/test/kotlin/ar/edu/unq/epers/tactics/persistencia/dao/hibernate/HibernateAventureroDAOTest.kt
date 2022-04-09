package ar.edu.unq.epers.tactics.persistencia.dao.hibernate

import ar.edu.unq.epers.tactics.modelo.Aventurero
import ar.edu.unq.epers.tactics.modelo.Party
import ar.edu.unq.epers.tactics.service.runner.HibernateTransactionRunner
import org.junit.Assert
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

class HibernateAventureroDAOTest {
    private val aventureroDAO = HibernateAventureroDAO()
    private var partyDAO = HibernatePartyDAO()

    lateinit var aventurero1 : Aventurero
    lateinit var aventurero2 : Aventurero
    lateinit var aventurero3 : Aventurero
    lateinit var party1 : Party

    @BeforeEach
    fun setUp() {

        party1 = Party("Los Super Amigos","")
        aventurero1 = Aventurero("xXvegetaXx","",30,70,20,10)
        aventurero2 = Aventurero("Mateico","",30,70,20,10)
        aventurero3 = Aventurero("Flea","",30,70,20,10)
        party1 = Party("LaPartyMenosCreativaDelMundo","")
    }

    @Test
    fun crearYRecuperarAventurerosNuevos() {
        HibernateTransactionRunner.runTrx {
            val idAventurero1= aventureroDAO.crear(aventurero1).id()!!
            val aventureroRecuperado1 = aventureroDAO.recuperar(idAventurero1)
            Assert.assertEquals(aventurero1, aventureroRecuperado1)

        }
    }

    @Test
    fun siSeEliminaUnAventureroYaNoPuedeSerRecuperado() {
        HibernateTransactionRunner.runTrx {
            val idAventurero2= aventureroDAO.crear(aventurero2).id()!!
            val aventureroRecuperado = aventureroDAO.recuperar(idAventurero2)
            aventureroDAO.eliminar(aventureroRecuperado)
            val exception = assertThrows<RuntimeException> {
                aventureroDAO.recuperar(aventureroRecuperado.id()!!)
            }
            Assert.assertEquals("No existe entidad con id provisto", exception.message)
        }
    }

    @Test
    fun seActualizaUnAventurero() {
        HibernateTransactionRunner.runTrx {
            val partyId = partyDAO.crear(party1).id()!!
            val partyRecuperada = partyDAO.recuperar(partyId)

            val idAventurero3= aventureroDAO.crear(aventurero3).id()!!
            var aventureroRecuperado = aventureroDAO.recuperar(idAventurero3)

            partyRecuperada.sumarAventurero(aventureroRecuperado)
            partyDAO.actualizar(partyRecuperada)
            aventureroDAO.actualizar(aventureroRecuperado)
            aventureroRecuperado = aventureroDAO.recuperar(idAventurero3)

            Assert.assertEquals(aventureroRecuperado.party()!!.id(), partyRecuperada.id())
        }
    }

    @AfterEach
    fun clear() {
        aventureroDAO.eliminarTodo()
    }
}
