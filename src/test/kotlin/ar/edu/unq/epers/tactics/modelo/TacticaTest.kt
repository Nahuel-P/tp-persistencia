package ar.edu.unq.epers.tactics.modelo

import ar.edu.unq.epers.tactics.modelo.tacticas.Tactica
import ar.edu.unq.epers.tactics.modelo.tacticas.enums.Accion
import ar.edu.unq.epers.tactics.modelo.tacticas.enums.Criterio
import ar.edu.unq.epers.tactics.modelo.tacticas.enums.TipoDeEstadistica
import ar.edu.unq.epers.tactics.modelo.tacticas.enums.TipoDeReceptor
import org.junit.Assert
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class TacticaTest {
    lateinit var emisor: Aventurero
    lateinit var receptor: Aventurero
    lateinit var tactica1: Tactica
    lateinit var tactica2: Tactica


    @BeforeEach
    fun setUp() {
        emisor = Aventurero("partyEmisor", "emisor", 15, 25, 10, 11)
        receptor = Aventurero("partyReceptor", "receptor", 10, 10, 10, 10)
        tactica1 = Tactica(1,TipoDeReceptor.ENEMIGO, TipoDeEstadistica.VIDA, Criterio.IGUAL, 35, Accion.ATAQUE_FISICO)
    }

    @Test
    fun tacticaVerificaSiPuedeUtilizar() {
        Assert.assertTrue(tactica1.sePuedeUtilizar(emisor,receptor))
    }

    @Test
    fun tacticaVerificaNoPuedeUtilizar() {
        Assert.assertFalse(tactica1.sePuedeUtilizar(receptor,emisor))
    }
}