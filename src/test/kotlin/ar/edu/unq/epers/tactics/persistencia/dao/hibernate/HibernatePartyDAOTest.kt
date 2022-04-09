package ar.edu.unq.epers.tactics.persistencia.dao.hibernate

import ar.edu.unq.epers.tactics.modelo.Aventurero
import ar.edu.unq.epers.tactics.modelo.Party
import ar.edu.unq.epers.tactics.service.runner.HibernateTransactionRunner
import org.junit.Assert
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class HibernatePartyDAOTest {
    private val partyDAO = HibernatePartyDAO()
    lateinit var unaParty: Party

    @BeforeEach
    fun setUp() {
        unaParty = Party("Aventureros","")
    }

    @Test
    fun seCreaUnaPartyYSeRecuperaSiendoEstasEquivalentes() {
        HibernateTransactionRunner.runTrx {
            val idParty = partyDAO.crear(unaParty).id!!
            val partyRecuperada = partyDAO.recuperar(idParty)

            Assert.assertEquals(idParty, partyRecuperada.id)
            Assert.assertEquals(unaParty.nombre, partyRecuperada.nombre)
            Assert.assertEquals(0, partyRecuperada.numeroDeAventureros())
            Assert.assertEquals(0, partyRecuperada.aventureros().size)
        }
    }

    @Test
    fun seCreaUnaPartyLuegoSeActualizaYSeObtieneEsaPartyActualizada(){
        HibernateTransactionRunner.runTrx {
            val partyTest = Party("PartyTest","")
            val idParty = partyDAO.crear(partyTest).id!!
            val aventurero = Aventurero("Saul","",30,70,20,10)

            partyTest.sumarAventurero(aventurero)
            partyDAO.actualizar(partyTest)
            val partyActualizadaRecuperada = partyDAO.recuperar(idParty)

            Assert.assertEquals(1, partyActualizadaRecuperada.numeroDeAventureros())
        }
    }

    @Test
    fun lasPartysSeRecuperanConOrdenAlfabetico(){
        HibernateTransactionRunner.runTrx {
            val partyA = Party("A","")
            val partyB = Party("B","")

            partyDAO.crear(partyA)
            partyDAO.crear(partyB)

            val partysRecuperadas = partyDAO.recuperarTodas()

            Assert.assertEquals(2, partysRecuperadas.size)
            Assert.assertEquals(partysRecuperadas[0].nombre(), partyA.nombre)
            Assert.assertEquals(partysRecuperadas[1].nombre(), partyB.nombre)
        }
    }

    @AfterEach
    fun eliminarDatos() {
        partyDAO.eliminarTodo()
    }
}
