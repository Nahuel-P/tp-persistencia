package ar.edu.unq.epers.tactics.modelo

import ar.edu.unq.epers.tactics.modelo.Aventurero
import org.junit.Assert
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.*

class AventureroTest {
    lateinit var aventurero : Aventurero

    @BeforeEach
    fun setUp() {
        aventurero = Aventurero("Saul","",30,70,20,10)
    }

    @Test
    fun creacionDeAventureroLeOtorgaAtributosYUnNombre() {
        Assert.assertEquals("Saul",aventurero.nombre())
        Assert.assertEquals(30,aventurero.fuerza())
        Assert.assertEquals(70,aventurero.destreza())
        Assert.assertEquals(20,aventurero.constitucion())
        Assert.assertEquals(10,aventurero.inteligencia())
    }

    @Test
    fun aventureroRecienCreadoEsNivel1() {
        Assert.assertEquals(1,aventurero.nivel())
    }

    @Test
    fun aventureroRecienCreadoNoTieneParty() {
        Assert.assertNull(aventurero.party())
    }

    @Test
    fun elAtributoDeUnAventuroNoSuperaLos100Puntos() {
        val exception = assertThrows<RuntimeException> {
            Aventurero("Saul OP","",30,104,20,10)
        }
        Assert.assertEquals("El valor 104 no es valido para el atributo destreza. El valor debe ser entre 1 y 100",exception.message)
    }

    @Test
    fun elAtributoDeUnAventuroNoEsInferiorA1Punto() {
        val exception = assertThrows<RuntimeException> {
            Aventurero("Saul Nefeado","",0,70,20,10)
        }
        Assert.assertEquals("El valor 0 no es valido para el atributo fuerza. El valor debe ser entre 1 y 100",exception.message)
    }

    @Test
    fun losPuntosDeVidaDeUnAventureroSonSuLvlPorCincoMasConstitucionPorDosMasFuerza() {
        val vidaEsperada = (1*5)+(20*2)+30
        Assert.assertEquals(vidaEsperada,aventurero.vidaBase())
    }

    @Test
    fun losPuntosDeArmaduraDeUnAventureroSonSuNivelMasSuConstitucion() {
        val armaduraEsperada = 1+20
        Assert.assertEquals(armaduraEsperada,aventurero.armadura())
    }

    @Test
    fun losPuntosDeManaDeUnAventureroSonNivelMasInteligencia() {
        val manaEsperado = 1+10
        Assert.assertEquals(manaEsperado,aventurero.manaBase())
    }

    @Test
    fun losPuntosDeVelocidadDeUnAventureroSonNivelMasDestreza() {
        val velocidadEsperada = 1+70
        Assert.assertEquals(velocidadEsperada,aventurero.velocidad())
    }

    @Test
    fun elDañoFisicoDeUnAventureroEsSuNivelMasFuerzaMasLaMitadDeSuDestreza() {
        val dañoFiscoEsperado = 1+30+(70/2)
        Assert.assertEquals(dañoFiscoEsperado,aventurero.dañoFisico())
    }

    @Test
    fun elPoderMagicoDeUnAventureroEsSuNivelMasInteligencia() {
        val poderMagicoEsperado = 1+10
        Assert.assertEquals(poderMagicoEsperado,aventurero.poderMagico())
    }

    @Test
    fun laPresicionDeUnAventureroEsSuNivelMasFuerzaMasDestreza() {
        val presicionEsperada = 1+70+30
        Assert.assertEquals(presicionEsperada,aventurero.precisionFisica())
    }
}