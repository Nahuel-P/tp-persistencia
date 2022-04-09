package ar.edu.unq.epers.tactics.modelo.enums

import ar.edu.unq.epers.tactics.modelo.tacticas.enums.TipoDeEstadistica
import ar.edu.unq.epers.tactics.modelo.Aventurero
import ar.edu.unq.epers.tactics.modelo.Party
import org.junit.Assert
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class TipoDeEstadisticaTest {

    lateinit var party: Party
    lateinit var aventurero: Aventurero

    @BeforeEach
    fun setUp() {
        party = Party("Me & The Boys","")
        aventurero = Aventurero("GreenGoblin", "https://i.redd.it/mctxnzcyds431.png",30, 30, 15, 50)
    }

    @Test
    fun mismaVidaBase() {
        Assert.assertEquals(aventurero.vidaActual(), TipoDeEstadistica.VIDA.valorDeEstadistica(aventurero))
    }

    @Test
    fun mismoManaBase() {
        Assert.assertEquals(aventurero.manaBase(), TipoDeEstadistica.MANA.valorDeEstadistica(aventurero))
    }

    @Test
    fun mismaArmadura() {
        Assert.assertEquals(aventurero.armadura(), TipoDeEstadistica.ARMADURA.valorDeEstadistica(aventurero))
    }

    @Test
    fun mismaVelocidad() {
        Assert.assertEquals(aventurero.velocidad(), TipoDeEstadistica.VELOCIDAD.valorDeEstadistica(aventurero))
    }

    @Test
    fun mismoDañoFisico() {
        Assert.assertEquals(aventurero.dañoFisico(), TipoDeEstadistica.DAÑO_FISICO.valorDeEstadistica(aventurero))
    }

    @Test
    fun mismaPresicion() {
        Assert.assertEquals(aventurero.precisionFisica(), TipoDeEstadistica.PRECISION_FISICA.valorDeEstadistica(aventurero))
    }

    @Test
    fun mismoDañoMagico() {
        Assert.assertEquals(aventurero.poderMagico(), TipoDeEstadistica.DAÑO_MAGICO.valorDeEstadistica(aventurero))
    }
}