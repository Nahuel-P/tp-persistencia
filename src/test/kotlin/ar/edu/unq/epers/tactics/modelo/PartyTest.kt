package ar.edu.unq.epers.tactics.modelo

import org.junit.Assert
import org.junit.Assert.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

class PartyTest {
    private lateinit var party: Party
    private lateinit var otraParty: Party
    private lateinit var aventurero1: Aventurero

    @BeforeEach
    fun setUp() {
        party = Party("Buenos Pibes","")
        otraParty = Party("Pibes Malitos","")
        aventurero1 = Aventurero("Juan Carlos","",30,70,20,10)
    }

    @Test
    fun unaPartyComienzaSinAventureros() {
        Assert.assertEquals(0, party.numeroDeAventureros())
    }

    @Test
    fun cuandoComienzaUnaPartySePuedeAgregarUnAventurero() {
        party.sumarAventurero(aventurero1)
        Assert.assertEquals(1, party.numeroDeAventureros())
    }

    @Test
    fun noSePuedenAgregarAventurerosAUnaPartyQueNoPertenece() {
        val aventureroDeOtraParty = Aventurero("Carlitoxxxx","",30,70,20,10)
        otraParty.sumarAventurero(aventureroDeOtraParty)

        val exception = assertThrows<RuntimeException> { party.sumarAventurero(aventureroDeOtraParty) }
        Assert.assertEquals(exception.message, "${aventureroDeOtraParty.nombre()} no pertenece a ${party.nombre()}.")
        Assert.assertEquals(0, party.numeroDeAventureros())
    }

    @Test
    fun noSeAgregaUnAventureroQueYaEstaEnLaParty() {
        party.sumarAventurero(aventurero1)

        val exception = assertThrows<RuntimeException> { party.sumarAventurero(aventurero1) }
        Assert.assertEquals(exception.message, "${aventurero1.nombre()} ya forma parte de la party ${party.nombre()}.")
        Assert.assertTrue(party.aventureros().contains(aventurero1))
        Assert.assertEquals(1, party.numeroDeAventureros())
    }

    @Test
    fun seSacaUnAventureroDeUnaParty() {

        party.sumarAventurero(aventurero1)
        party.sacarA(aventurero1)

        Assert.assertEquals(null, aventurero1.party)
        Assert.assertEquals(0, party.numeroDeAventureros())
    }

    @Test
    fun noSePuedeRemoverUnAventureroDeUnaPartyALaQueNoPertenece() {
        otraParty.sumarAventurero(aventurero1)

        val exception = assertThrows<java.lang.RuntimeException> { party.sacarA(aventurero1) }
        assertEquals("${aventurero1.nombre()} no pertenece a ${party.nombre()}.", exception.message)
    }
}