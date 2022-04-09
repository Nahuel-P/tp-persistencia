package ar.edu.unq.epers.tactics.service.impl

import ar.edu.unq.epers.tactics.modelo.Aventurero
import ar.edu.unq.epers.tactics.modelo.Party
import ar.edu.unq.epers.tactics.modelo.Pelea
import ar.edu.unq.epers.tactics.modelo.habilidades.AtaqueFisico
import ar.edu.unq.epers.tactics.modelo.habilidades.Curar
import ar.edu.unq.epers.tactics.modelo.tacticas.Tactica
import ar.edu.unq.epers.tactics.modelo.tacticas.enums.Accion
import ar.edu.unq.epers.tactics.modelo.tacticas.enums.Criterio
import ar.edu.unq.epers.tactics.modelo.tacticas.enums.TipoDeEstadistica
import ar.edu.unq.epers.tactics.modelo.tacticas.enums.TipoDeReceptor
import ar.edu.unq.epers.tactics.persistencia.dao.hibernate.HibernateAventureroDAO
import ar.edu.unq.epers.tactics.persistencia.dao.hibernate.HibernatePartyDAO
import ar.edu.unq.epers.tactics.persistencia.dao.hibernate.HibernatePeleaDAO
import javassist.NotFoundException
import org.assertj.core.api.Assertions.assertThat
import org.junit.Assert
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

class PeleaServiceTest {

    val peleaDAO = HibernatePeleaDAO()
    val partyDAO = HibernatePartyDAO()
    val aventureroDAO = HibernateAventureroDAO()

    val peleaService = PeleaServiceImpl(peleaDAO, partyDAO, aventureroDAO)
    val partyService = PartyServiceImpl(partyDAO)
    val aventureroService = AventureroServiceImpl(aventureroDAO, partyDAO)

    private var partyA: Party = Party("Me and the boys","")
    private var partyB: Party = Party("Yo y los pibes","")
    private var partyC: Party = Party("Ich und die Jungs","")

    private var enemigo = Aventurero("DonRamon777","",20,20,20,20)
    private var aventurero = Aventurero("Chavoneitor","",20,20,20,20)


    @BeforeEach
    fun setUp() {
        peleaDAO.eliminarTodo()
        partyDAO.eliminarTodo()

        partyService.crear(partyA)
        partyService.crear(partyB)
        partyService.crear(partyC)
        partyService.agregarAventureroAParty(partyA.id()!!,aventurero)
        partyService.agregarAventureroAParty(partyC.id()!!,enemigo)
    }

    @Test
    fun unaPartyAlPrincipioNoTieneUnaPelea(){
        val enPelea = peleaService.estaEnPelea(partyA.id()!!.toLong())
        Assert.assertFalse(enPelea)
    }

    @Test
    fun noSePuedeEntrarEnPeleaSiTieneUnaEnCurso(){
        peleaService.iniciarPelea(partyA.id()!!.toLong(),partyB.id()!!.toLong())
        val exception = assertThrows<RuntimeException> {
            peleaService.iniciarPelea(partyA.id()!!.toLong(),partyC.id()!!.toLong())
        }
        Assert.assertEquals("No se puede iniciar una pelea: la party ya esta peleando",exception.message)
    }

    @Test
    fun finalizarUnaPelea(){
        val pelea = peleaService.iniciarPelea(partyA.id()!!, partyB.id()!!)
        peleaService.terminarPelea(pelea.id()!!)
        Assert.assertFalse(peleaService.estaEnPelea(partyA.id()!!))
        Assert.assertFalse(peleaService.estaEnPelea(partyB.id()!!))
    }

    @Test
    fun resolverTurnoDeAtaqueParaUnAventurero(){

        val partyEnemiga = partyService.recuperar(partyC.id()!!)
        val nuestraParty = partyService.recuperar(partyA.id()!!)

        val aliado = nuestraParty.obtenerAventurero(aventurero.id()!!)
        val tactica = Tactica(1, TipoDeReceptor.ENEMIGO, TipoDeEstadistica.VIDA, Criterio.MENOR_QUE, 9999, Accion.ATAQUE_FISICO)
        aliado!!.agregarTactica(tactica)

        aventureroService.actualizar(aliado)
        val pelea= peleaService.iniciarPelea(partyA.id()!!,partyEnemiga.id()!!)
        val habilidadUtilizada=peleaService.resolverTurno(pelea.id()!!,aliado.id()!!)

        Assert.assertTrue(habilidadUtilizada is AtaqueFisico)
    }

    @Test
    fun resolverCuracionComoTacticaSiEsPrioritariaAUnaTacticaDeAtaque(){

        var partyEnemiga = partyService.recuperar(partyC.id()!!)
        var nuestraParty = partyService.recuperar(partyA.id()!!)

        var aliado = nuestraParty.obtenerAventurero(aventurero.id()!!)
        //var enemigo = partyEnemiga.obtenerAventurero(enemigo.id()!!)

        val tactica1 = Tactica(2, TipoDeReceptor.ENEMIGO, TipoDeEstadistica.VIDA, Criterio.MAYOR_QUE, 0, Accion.ATAQUE_FISICO)
        val tactica2 = Tactica(1, TipoDeReceptor.UNO_MISMO, TipoDeEstadistica.VIDA, Criterio.MAYOR_QUE, 0, Accion.CURAR)
        aliado!!.agregarTactica(tactica1)
        aliado!!.agregarTactica(tactica2)

        aventureroService.actualizar(aliado)
        val pelea= peleaService.iniciarPelea(partyA.id()!!,partyEnemiga.id()!!)
        val habilidadUtilizada=peleaService.resolverTurno(pelea.id()!!,aliado.id()!!)

        Assert.assertTrue(habilidadUtilizada is Curar)
    }

    @Test
    fun noSePuedeResolverElTurnoSiElAventureroNoTieneTacticas(){

        var nuestraParty = partyService.recuperar(partyA.id()!!)
        var partyEnemiga = partyService.recuperar(partyC.id()!!)
        var aliado = nuestraParty.obtenerAventurero(aventurero.id()!!)
        val pelea= peleaService.iniciarPelea(nuestraParty.id()!!,partyEnemiga.id()!!)
        val exception = assertThrows<RuntimeException> { peleaService.resolverTurno(pelea.id()!!,aliado!!.id()!!) }
        Assert.assertEquals("No hubo tactica para aplicar", exception.message)
    }

    @Test
    fun unaPartyPuedeEmpezarUnaPelea() {
        peleaService.iniciarPelea(partyA.id()!!,partyB.id()!!)

        Assert.assertTrue(peleaService.estaEnPelea(partyA.id()!!.toLong()))
        Assert.assertTrue(peleaService.estaEnPelea(partyB.id()!!.toLong()))
    }

    @Test
    fun unaPartyConPeleaEmpezadaNoPuedeIniciarOtra() {
        peleaService.iniciarPelea(partyA.id()!!,partyB.id()!!)
        val exception = assertThrows<RuntimeException> { peleaService.iniciarPelea(partyA.id()!!,partyB.id()!!) }
        assertThat(exception.message).isEqualTo("No se puede iniciar una pelea: la party ya esta peleando")
    }

    @Test
    fun noSePuedeSalirDeUnaPeleaQueNoEstaEnCurso() {
        val pelea = peleaService.iniciarPelea(partyA.id()!!, partyB.id()!!)
        peleaService.terminarPelea(pelea.id!!)
        val exception = assertThrows<RuntimeException> { peleaService.terminarPelea(pelea.id()!!) }
        assertThat(exception).hasMessage("La pelea ya ha terminado antes.")
        Assert.assertFalse(peleaService.estaEnPelea(partyA.id()!!))
    }

    @Test
    fun recuperarPeleasDeUnaPartyOrdenadas() {
        val pelea1 = peleaService.iniciarPelea(partyA.id()!!, partyB.id()!!)
        peleaService.terminarPelea(pelea1.id!!)
        val pelea2 = peleaService.iniciarPelea(partyA.id()!!, partyC.id()!!)
        peleaService.terminarPelea(pelea2.id!!)
        val pelea3 = peleaService.iniciarPelea(partyB.id()!!, partyA.id()!!)
        peleaService.terminarPelea(pelea3.id!!)
        val pelea4 = peleaService.iniciarPelea(partyC.id()!!, partyA.id()!!)
        peleaService.terminarPelea(pelea4.id!!)
        val peleasPaginadas = peleaService.recuperarOrdenadas(partyA.id()!!,0)
        Assert.assertEquals(4, peleasPaginadas.peleas.size)
    }

    @Test
    fun recuperarPeleasDeUnaPartyQueNoTienePeleasDaError() {
        val pelea1 = peleaService.iniciarPelea(partyA.id()!!, partyB.id()!!)
        peleaService.terminarPelea(pelea1.id!!)
        val pelea2 = peleaService.iniciarPelea(partyB.id()!!, partyA.id()!!)
        peleaService.terminarPelea(pelea2.id!!)

        val exception = assertThrows<NotFoundException> { peleaService.recuperarOrdenadas(partyC.id()!!,1) }
        assertThat(exception).hasMessage("El número de party ${partyC.id} no tiene peleas o no existe")
    }

    @Test
    fun recuperarPeleasDeUnaPartyDeUnaPaginaQueNoExisteDaError() {
        val pelea1 = peleaService.iniciarPelea(partyA.id()!!, partyB.id()!!)
        peleaService.terminarPelea(pelea1.id!!)
        val pelea2 = peleaService.iniciarPelea(partyA.id()!!, partyC.id()!!)
        peleaService.terminarPelea(pelea2.id!!)
        val pelea3 = peleaService.iniciarPelea(partyB.id()!!, partyA.id()!!)
        peleaService.terminarPelea(pelea3.id!!)
        val pelea4 = peleaService.iniciarPelea(partyC.id()!!, partyA.id()!!)
        peleaService.terminarPelea(pelea4.id!!)

        val pagina = 2

        val exception = assertThrows<NotFoundException> { peleaService.recuperarOrdenadas(partyA.id()!!,pagina) }
        assertThat(exception).hasMessage("El número de pagina $pagina no existe")
    }
}