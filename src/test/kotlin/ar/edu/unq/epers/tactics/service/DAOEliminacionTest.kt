package ar.edu.unq.epers.tactics.service

import ar.edu.unq.epers.tactics.modelo.Aventurero
import ar.edu.unq.epers.tactics.modelo.Party
import ar.edu.unq.epers.tactics.persistencia.dao.AventureroDAO
import ar.edu.unq.epers.tactics.persistencia.dao.PartyDAO
import ar.edu.unq.epers.tactics.persistencia.dao.hibernate.HibernateAventureroDAO
import ar.edu.unq.epers.tactics.persistencia.dao.hibernate.HibernatePartyDAO
import ar.edu.unq.epers.tactics.service.runner.HibernateTransactionRunner
import org.assertj.core.api.Assertions.assertThat
import org.junit.Assert
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

class DAOEliminacionTest {

//        lateinit private var party: Party
//        lateinit private var aventurero: Aventurero
//        lateinit var aventureroDao: AventureroDAO
//        lateinit var partyDao: PartyDAO
//
//        @BeforeEach
//        fun setUp() {
//            aventureroDao = HibernateAventureroDAO()
//            partyDao = HibernatePartyDAO()
//
//            party = Party("Party","")
//            aventurero = Aventurero("Saul","",30,70,20,10)
//        }
//
//        @Test
//        fun unAventureroSeAgregaAUnaPartyYEsteEsPersistido() {
//            HibernateTransactionRunner.runTrx {
//                partyDao.crear(party)
//                party.sumarAventurero(aventurero)
//                partyDao.actualizar(party)
//            }
//
//            HibernateTransactionRunner.runTrx {
//                val partyRecuperada = partyDao.recuperar(party.id()!!)
//                val aventureroRecuperado = aventureroDao.recuperar(aventurero.id()!!)
//
//                Assert.assertEquals(1, partyRecuperada.numeroDeAventureros())
//                assertThat(aventureroRecuperado).usingRecursiveComparison().isEqualTo(aventurero)
//            }
//        }
//
//        @Test
//        fun seSacaUnAventureroDeLaPartyYseLaActualizaEsteDejaDeEstarPersistido() {
//            HibernateTransactionRunner.runTrx {
//                partyDao.crear(party)
//            }
//
//            HibernateTransactionRunner.runTrx {
//                party.sumarAventurero(aventurero)
//                partyDao.actualizar(party)
//            }
//
//            HibernateTransactionRunner.runTrx {
//                party.sacarA(aventurero)
//                partyDao.actualizar(party)
//            }
//
//            HibernateTransactionRunner.runTrx {
//                val partyRecuperada = partyDao.recuperar(party.id()!!)
//
//                Assert.assertEquals(0, partyRecuperada.numeroDeAventureros())
//                val exception = assertThrows<RuntimeException> { aventureroDao.recuperar(aventurero.id()!!) }
//                Assert.assertEquals("No existe entidad con id provisto", exception.message)
//            }
//        }
//
//        @AfterEach
//        fun limpiar() {
//            partyDao.eliminarTodo()
//        }
}