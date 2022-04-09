package ar.edu.unq.epers.tactics.service

import ar.edu.unq.epers.tactics.modelo.Aventurero
import ar.edu.unq.epers.tactics.modelo.Party
import ar.edu.unq.epers.tactics.modelo.Pelea
import ar.edu.unq.epers.tactics.modelo.habilidades.AtaqueFisico
import ar.edu.unq.epers.tactics.modelo.habilidades.AtaqueMagico
import ar.edu.unq.epers.tactics.modelo.habilidades.Curar
import ar.edu.unq.epers.tactics.modelo.habilidades.Habilidad
import ar.edu.unq.epers.tactics.persistencia.dao.hibernate.HibernateAventureroDAO
import ar.edu.unq.epers.tactics.persistencia.dao.hibernate.HibernatePartyDAO
import ar.edu.unq.epers.tactics.persistencia.dao.hibernate.HibernatePeleaDAO
import ar.edu.unq.epers.tactics.service.impl.AventureroLeaderboardServiceImpl
import ar.edu.unq.epers.tactics.service.impl.AventureroServiceImpl
import ar.edu.unq.epers.tactics.service.impl.PartyServiceImpl
import ar.edu.unq.epers.tactics.service.impl.PeleaServiceImpl
import org.junit.Assert
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class AventureroLeaderboardServiceTest {
    private val aventureroDAO = HibernateAventureroDAO()
    private val partyDAO = HibernatePartyDAO()
    private val peleaDAO = HibernatePeleaDAO()

    private val aventureroLeaderboardService = AventureroLeaderboardServiceImpl(aventureroDAO)
    private var aventureroService: AventureroServiceImpl = AventureroServiceImpl(aventureroDAO, partyDAO)
    private val partyService: PartyServiceImpl = PartyServiceImpl(partyDAO)
    private val peleaService = PeleaServiceImpl(peleaDAO, partyDAO, aventureroDAO)

    lateinit var pelea : Pelea
    private var aventurero1: Aventurero = Aventurero("Moe","",30,70,20,90)
    private var aventurero2: Aventurero = Aventurero("Number One","",70,1,20,10)
    private var party1: Party = Party("Moe Szyslak Experience,featuring Homer","")
    private var party2: Party  =Party("Stonecutters","")

    @BeforeEach
    fun setUp() {
        peleaDAO.eliminarTodo()
        partyDAO.eliminarTodo()
        aventureroDAO.eliminarTodo()

        party1 = partyService.crear(party1)
        party2 = partyService.crear(party2)

        partyService.agregarAventureroAParty(party1.id!!, aventurero1)
        partyService.agregarAventureroAParty(party2.id!!, aventurero2)

        pelea = peleaService.iniciarPelea(party1.id()!!,party2.id()!!)
    }

    @Test
    fun elMejorGuerero(){

        var aliado = pelea.partyA()!!.obtenerAventurero(aventurero1.id()!!)
        var enemigo = pelea.partyB()!!.obtenerAventurero(aventurero2.id()!!)

        var ataqueFisico1= AtaqueFisico(aliado!!,enemigo!!).conValores(aliado!!,enemigo!!)
        var ataqueFisico2= AtaqueFisico(enemigo!!,aliado!!).conValores(enemigo!!,aliado!!)

        var pelea = peleaService.recuperarPelea(pelea.id()!!)

        peleaService.recibirHabilidad(pelea.id()!!, ataqueFisico1.receptor().id()!!,ataqueFisico1)
        peleaService.recibirHabilidad(pelea.id()!!, ataqueFisico2.receptor().id()!!,ataqueFisico2)

        var mejorGuerrero = aventureroLeaderboardService.mejorGuerrero()

        Assert.assertEquals(enemigo.id()!!,mejorGuerrero.id()!!)
    }

    @Test
    fun elMejorMago(){

        var peleaRecuperada = peleaService.recuperarPelea(pelea.id()!!)
        var aliado = peleaRecuperada.partyA()!!.obtenerAventurero(aventurero1.id()!!)
        var enemigo = peleaRecuperada.partyB()!!.obtenerAventurero(aventurero2.id()!!)

        var ataqueMagico1= AtaqueMagico(enemigo!!,aliado!!).conValores(enemigo!!,aliado!!)
        var ataqueMagico2= AtaqueMagico(aliado!!,enemigo!!).conValores(aliado!!,enemigo!!)
        ataqueMagico1.volverlaInfalible()
        ataqueMagico2.volverlaInfalible()

        peleaService.recibirHabilidad(peleaRecuperada.id()!!, ataqueMagico1.receptor().id()!!,ataqueMagico1)
        peleaService.recibirHabilidad(peleaRecuperada.id()!!, ataqueMagico2.receptor().id()!!,ataqueMagico2)

        var mejorMago = aventureroLeaderboardService.mejorMago()

        Assert.assertEquals(enemigo.id()!!,mejorMago.id()!!)
    }

    @Test
    fun elMejorSanador(){

        var peleaRecuperada = peleaService.recuperarPelea(pelea.id()!!)
        var aliado = peleaRecuperada.partyA()!!.obtenerAventurero(aventurero1.id()!!)
        var enemigo = peleaRecuperada.partyB()!!.obtenerAventurero(aventurero2.id()!!)

        var ataqueFisico1= AtaqueFisico(aliado!!,enemigo!!).conValores(aliado!!,enemigo!!)
        var ataqueFisico2= AtaqueFisico(enemigo!!,aliado!!).conValores(enemigo!!,aliado!!)
        ataqueFisico1.volverlaInfalible()
        ataqueFisico2.volverlaInfalible()

        peleaService.recibirHabilidad(peleaRecuperada.id()!!, ataqueFisico1.receptor().id()!!,ataqueFisico1)
        peleaService.recibirHabilidad(peleaRecuperada.id()!!, ataqueFisico2.receptor().id()!!,ataqueFisico2)

        var peleaLuegoDeAtaques = peleaService.recuperarPelea(peleaRecuperada.id()!!)
        var aliadoHerido = peleaLuegoDeAtaques.partyA()!!.obtenerAventurero(aventurero1.id()!!)
        var enemigoHerido = peleaLuegoDeAtaques.partyB()!!.obtenerAventurero(aventurero2.id()!!)

        var cuaracion1= Curar(aliadoHerido!!,aliadoHerido!!).conValores(aliadoHerido!!,aliadoHerido!!)
        var curacion2= Curar(enemigoHerido!!,enemigoHerido!!).conValores(enemigoHerido!!,enemigoHerido!!)

        peleaService.recibirHabilidad(peleaLuegoDeAtaques.id()!!, cuaracion1.receptor().id()!!,cuaracion1)
        peleaService.recibirHabilidad(peleaLuegoDeAtaques.id()!!, curacion2.receptor().id()!!,curacion2)

        var mejorCurador = aventureroLeaderboardService.mejorCurandero()

        Assert.assertEquals(aliadoHerido.id()!!,mejorCurador.id()!!)
    }
}