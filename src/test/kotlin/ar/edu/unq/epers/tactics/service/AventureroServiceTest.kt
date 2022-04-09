package ar.edu.unq.epers.tactics.service

import ar.edu.unq.epers.tactics.modelo.Aventurero
import ar.edu.unq.epers.tactics.modelo.Party
import ar.edu.unq.epers.tactics.persistencia.dao.AventureroDAO
import ar.edu.unq.epers.tactics.persistencia.dao.PartyDAO
import ar.edu.unq.epers.tactics.persistencia.dao.hibernate.HibernateAventureroDAO
import ar.edu.unq.epers.tactics.persistencia.dao.hibernate.HibernatePartyDAO
import ar.edu.unq.epers.tactics.service.impl.AventureroServiceImpl
import ar.edu.unq.epers.tactics.service.impl.PartyServiceImpl
import org.junit.Assert
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
private var aventureroDao: AventureroDAO = HibernateAventureroDAO()
    private var partyDao: PartyDAO = HibernatePartyDAO()

class AventureroServiceTest {

    private var aventureroDao: AventureroDAO = HibernateAventureroDAO()
    private var partyDao: PartyDAO = HibernatePartyDAO()

    private var aventureroService: AventureroServiceImpl = AventureroServiceImpl(aventureroDao, partyDao)
    private var partyService: PartyServiceImpl = PartyServiceImpl(partyDao)

    private lateinit var party: Party
    private lateinit var aventurero: Aventurero

        @BeforeEach
        fun setUp() {
            aventureroDao.eliminarTodo()
            party = Party("SuicideSquad","")
            partyService.crear(party)
            aventurero = Aventurero("Queen","",30,70,20,10)

        }

        @Test
        fun seAgregaUnAventureroAUnaPartyYSeRecupera() {
            partyService.agregarAventureroAParty(party.id()!!, aventurero)
            val aventureroRecuperado = aventureroService.recuperar(aventurero.id()!!)
            Assert.assertEquals(aventureroRecuperado.id(),aventurero.id())
        }

        @Test
        fun noSePuedeRecuperarUnAventureroQueNoExiste() {
            val exception = assertThrows<RuntimeException> {
                aventureroService.recuperar(-1)
            }
            Assert.assertEquals("No existe entidad con id provisto", exception.message)
        }

        @Test
        fun seActualizaUnAventurero() {
            partyService.agregarAventureroAParty(party.id()!!, aventurero)
            var aventureroRecuperado = aventureroService.recuperar(aventurero.id()!!)
            Assert.assertEquals(aventurero.dañoInflingido(),aventureroRecuperado.dañoInflingido())

            aventurero.incrementarDañoInflingido(20)
            aventureroService.actualizar(aventurero)
            aventureroRecuperado = aventureroService.recuperar(aventurero.id()!!)
            Assert.assertEquals(aventurero.dañoInflingido(),aventureroRecuperado.dañoInflingido())

        }

        @Test
        fun seEliminaUnAventureroYNoPuedeRecuperarseNuevamente() {
            val aventureroId = partyService.agregarAventureroAParty(party.id()!!, aventurero).id()!!
            aventureroService.eliminar(aventurero)
            val partyRecuperada = partyService.recuperar(party.id()!!)
            Assert.assertEquals(0, partyRecuperada.numeroDeAventureros())
            val exception = assertThrows<RuntimeException> { aventureroService.recuperar(aventureroId) }
            Assert.assertEquals("No existe entidad con id provisto", exception.message)
        }

}