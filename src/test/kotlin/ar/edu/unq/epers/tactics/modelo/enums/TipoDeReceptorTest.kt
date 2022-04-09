package ar.edu.unq.epers.tactics.modelo.enums

import ar.edu.unq.epers.tactics.modelo.Aventurero
import ar.edu.unq.epers.tactics.modelo.Party
import ar.edu.unq.epers.tactics.modelo.tacticas.enums.TipoDeReceptor
import org.junit.Assert
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class TipoDeReceptorTest {

    private lateinit var partyEmisor : Party
    private lateinit var partyEnemiga : Party
    private lateinit var aventurero : Aventurero
    private lateinit var enemigo : Aventurero
    private lateinit var aliado : Aventurero

    @BeforeEach
    fun setUp() {
        partyEmisor = Party("Passione","")
        aventurero =  Aventurero("GreenGoblin", "https://i.redd.it/mctxnzcyds431.png",30, 30, 15, 50)
        aliado = Aventurero("Rhino", "https://cdn.drawception.com/drawings/7LfV1pKka6.png",70, 15, 35, 5)
        partyEmisor.sumarAventurero(aventurero)
        partyEmisor.sumarAventurero(aliado)

        partyEnemiga = Party("Avangers","")
        enemigo = Aventurero("Spidey", "https://acortar.link/ZUnZtx",20, 45, 15, 40)
        partyEnemiga.sumarAventurero(enemigo)
    }

    @Test
    fun unAventureroTieneComoAliadosALosDeSuParty() {
        Assert.assertTrue(TipoDeReceptor.ALIADO.verificar(aventurero, aliado))
        Assert.assertFalse(TipoDeReceptor.ALIADO.verificar(aventurero, aventurero))
        Assert.assertFalse(TipoDeReceptor.ALIADO.verificar(aventurero, enemigo))
    }

    @Test
    fun unAventureroEsUnoMismoYDistintoAlRestoDeAventureros() {
        Assert.assertTrue(TipoDeReceptor.UNO_MISMO.verificar(aventurero, aventurero))
        Assert.assertFalse(TipoDeReceptor.UNO_MISMO.verificar(aventurero, enemigo))
        Assert.assertFalse(TipoDeReceptor.UNO_MISMO.verificar(aventurero, aliado))
    }

    @Test
    fun unAventureroTieneComoEnEnemigoALosMiembrosDeOtrasPartys() {
        Assert.assertTrue(TipoDeReceptor.ENEMIGO.verificar(aventurero, enemigo))
        Assert.assertFalse(TipoDeReceptor.ENEMIGO.verificar(aventurero, aliado))
        Assert.assertFalse(TipoDeReceptor.ENEMIGO.verificar(aventurero, aventurero))
    }
}