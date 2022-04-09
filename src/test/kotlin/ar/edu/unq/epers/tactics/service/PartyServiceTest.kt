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

class PartyServiceTest {

    private var partyDao: PartyDAO = HibernatePartyDAO()
    private var partyService: PartyServiceImpl = PartyServiceImpl(partyDao)
    private lateinit var party: Party
    private lateinit var party2: Party


    @BeforeEach
    fun setUp() {
        partyDao.eliminarTodo()
        party = Party("Parti2","")
        party2 = Party("LoteMalote","")
    }

    @Test
    fun crearPartyYRecuperarla() {
        partyService.crear(party)
        var partyverify = partyService.recuperar(party.id!!)
        Assert.assertNotEquals(null, partyverify.id)
        Assert.assertEquals("Parti2", partyverify.nombre)
    }

    @Test
    fun noSeRecuperaUnaPartyQueNoExiste(){
        val exception = assertThrows<RuntimeException> {
            partyService.recuperar(-1)
        }
        Assert.assertEquals("No existe entidad con id provisto",exception.message)
    }

    @Test
    fun actualizarParty() {
        partyService.crear(party)
        var partyverify = partyService.recuperar(party.id!!)
        partyverify.nombre = "PartyConNombreModificadoParaElTestXd"
        partyService.actualizar(partyverify)
        party = partyService.recuperar(party.id!!)
        Assert.assertEquals("PartyConNombreModificadoParaElTestXd", party.nombre)
    }


    @Test
    fun recuperarTodasLasParties() {
        partyService.crear(party)
        partyService.crear(party2)
        val todasLasPartys = partyService.recuperarTodas()
        Assert.assertEquals(2, todasLasPartys.size)
    }

    @Test
    fun agregarUnAventureroALaParty() {
        party = partyService.crear(party)
        var aventurero = Aventurero("JuanitoUchiha97","",30,70,20,10)
        aventurero =  partyService.agregarAventureroAParty(party.id!!, aventurero)
        var partyverify = partyService.recuperar(party.id!!)
        Assert.assertEquals(1,partyverify.aventureros().size)
        Assert.assertEquals(partyverify.aventureros().get(0).id(), aventurero.id())
    }

    @Test
    fun recuperarOrdenadasLasPrimerasDiezParties(){
        this.masiveCreateParty(20)
        var partyPagiadas = partyService.recuperarOrdenadas(Orden.PODER,Direccion.ASCENDENTE,0)
        Assert.assertEquals(10,partyPagiadas.parties.size)
        Assert.assertEquals(20,partyPagiadas.total)
    }

    @Test
    fun recuperarOrdenadasLaUltimaPagina(){
        this.masiveCreateParty(35)
        var partyPagiadas = partyService.recuperarOrdenadas(Orden.PODER,Direccion.ASCENDENTE,3)
        Assert.assertEquals(5,partyPagiadas.parties.size)
        Assert.assertEquals(35,partyPagiadas.total)
    }

    private fun masiveCreateParty(cant: Int){
        val listParty = MutableList(cant) { Party() }
        for (i in 1..cant){
            var party = partyService.crear(Party("Party"+i,""))
            listParty.add(party)
            for (j in 1..5){
                var aventurero = Aventurero("Aventurero_"+i+"_"+j,
                                            "",
                                            (1..100).random(),
                                            (1..100).random(),
                                            (1..100).random(),
                                            (1..100).random())
                partyService.agregarAventureroAParty(party.id!!, aventurero)
            }

        }
    }

    @AfterEach
    fun limpiar() {
        partyService.eliminarTodo()
    }

}