package ar.edu.unq.epers.tactics.service

import ar.edu.unq.epers.tactics.modelo.*
import ar.edu.unq.epers.tactics.modelo.tacticas.Tactica
import ar.edu.unq.epers.tactics.modelo.tacticas.enums.Accion
import ar.edu.unq.epers.tactics.modelo.tacticas.enums.Criterio
import ar.edu.unq.epers.tactics.modelo.tacticas.enums.TipoDeEstadistica
import ar.edu.unq.epers.tactics.modelo.tacticas.enums.TipoDeReceptor
import ar.edu.unq.epers.tactics.persistencia.dao.AventureroDAO
import ar.edu.unq.epers.tactics.persistencia.dao.PartyDAO
import ar.edu.unq.epers.tactics.persistencia.dao.hibernate.HibernateAventureroDAO
import ar.edu.unq.epers.tactics.persistencia.dao.hibernate.HibernatePartyDAO
import ar.edu.unq.epers.tactics.persistencia.dao.neo4j.Neo4JClaseDAO
import ar.edu.unq.epers.tactics.service.impl.AventureroServiceImpl
import ar.edu.unq.epers.tactics.service.impl.ClaseServiceImpl
import ar.edu.unq.epers.tactics.service.impl.PartyServiceImpl
import javassist.NotFoundException
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatThrownBy
import org.junit.Assert
import org.junit.jupiter.api.*

class ClaseServiceTest {
    private val claseDAO = Neo4JClaseDAO()
    private val claseService: ClaseServiceImpl = ClaseServiceImpl(claseDAO, HibernateAventureroDAO())

    private var aventureroDao: AventureroDAO = HibernateAventureroDAO()
    private var partyDao: PartyDAO = HibernatePartyDAO()
    private var aventureroService: AventureroServiceImpl = AventureroServiceImpl(aventureroDao, partyDao)
    private var partyService: PartyServiceImpl = PartyServiceImpl(partyDao)

    @BeforeEach
    fun setUp() {
        PartyServiceImpl(HibernatePartyDAO()).eliminarTodo()
        claseDAO.clear()
    }

    @Test
    fun cuandoSeCreaUnaClase() {
        val nombre = "Aventurero"
        val nuevaClase = claseService.crearClase(nombre)
        Assert.assertEquals(nuevaClase.nombre,nombre)
    }

    @Test
    fun seCreaUnaMejora() {
        val clasePredecesora = claseService.crearClase("Aventurero")
        val claseSucesora = claseService.crearClase("Guerrero")
        val atributos = mutableListOf<Atributo>(Atributo.FUERZA, Atributo.CONSTITUCION)

        var mejora = claseService.crearMejora("Aventurero", "Guerrero", atributos, 2)
        Assert.assertEquals(mejora.clasePrevia.nombre,"Aventurero")
        Assert.assertEquals(mejora.clasePosterior.nombre,"Guerrero")
        Assert.assertEquals(mejora.cantidadDeAtributos,2)
        Assert.assertEquals(mejora.atributos.size,2)
    }

    @Test
    fun unBrujoEsUnaMejoraQueRequiereDeLaClaseMago() {
        claseService.crearClase("Aventurero")
        claseService.crearClase("Mago")
        val atributos = mutableListOf<Atributo>(Atributo.INTELIGENCIA)

        claseService.crearMejora("Aventurero", "Mago", atributos, 3)
        var brujo = claseService.crearClase("Brujo")
        claseService.requerir("Brujo", "Mago")
        var requeridas = claseService.requeridasDe(brujo)
        Assert.assertEquals(requeridas.size,1)
        Assert.assertEquals(requeridas.get(0).nombre,"Mago")
    }

    @Test
    fun elRequerimientoEsUnidireccionalPorqueElMagoNoRequiereBrujo() {
        claseService.crearClase("Aventurero")
        var mago =claseService.crearClase("Mago")
        val atributos = mutableListOf<Atributo>(Atributo.INTELIGENCIA)

        claseService.crearMejora("Aventurero", "Mago", atributos, 3)
        claseService.crearClase("Brujo")
        claseService.requerir("Brujo", "Mago")
        var requeridas = claseService.requeridasDe(mago)
        Assert.assertEquals(requeridas.size,0)
    }

    @Test
    fun unAventureroNoPuedeMejorarseAFisicoSinExperiencia(){
        var aventurero = Aventurero("DamSinPila","",30,70,20,10)
        var party = Party("ElQuinto","")
        partyService.crear(party)
        partyService.agregarAventureroAParty(party.id()!!, aventurero)
        var aventureroRecuperado = aventureroService.recuperar(aventurero.id()!!)
        val atributos = mutableListOf<Atributo>(Atributo.FUERZA)

        claseService.crearClase("Aventurero")
        claseService.crearClase("Fisico")
        var mejora = claseService.crearMejora("Aventurero", "Fisico", atributos, 2)
        val result = claseService.puedeMejorar(aventureroRecuperado.id()!!,mejora)
        Assert.assertFalse(result)
    }

    @Test
    fun unAventureroPuedeMejorarseAFisicoConExperiencia(){
        var aventurero = Aventurero("DamSinPila","",30,70,20,10)
        aventurero.darExperiencia(2)
        var party = Party("ElQuinto","")
        partyService.crear(party)
        partyService.agregarAventureroAParty(party.id()!!, aventurero)
        var aventureroRecuperado = aventureroService.recuperar(aventurero.id()!!)
        val atributos = mutableListOf<Atributo>(Atributo.FUERZA)

        claseService.crearClase("Aventurero")
        claseService.crearClase("Fisico")
        var mejora = claseService.crearMejora("Aventurero", "Fisico", atributos, 2)
        val result = claseService.puedeMejorar(aventureroRecuperado.id()!!,mejora)
        Assert.assertTrue(result)
    }

    @Test
    fun unAventureroGanaProficienciaAGuerrero() {
        claseService.crearClase("Aventurero")
        claseService.crearClase("Guerrero")
        val atributos = mutableListOf<Atributo>(Atributo.FUERZA)
        var mejora = claseService.crearMejora("Aventurero", "Guerrero", atributos, 2)

        var aventurero = Aventurero("SordoGony","",30,70,20,10)
        aventurero.darExperiencia(2)

        var party = Party("ElCampito","")
        partyService.crear(party)
        partyService.agregarAventureroAParty(party.id()!!, aventurero)

        var aventureroProficiente = claseService.ganarProficiencia(aventurero.id()!!, "Aventurero", "Guerrero")
        Assert.assertEquals(aventureroProficiente.clases().size,2)
        Assert.assertEquals(aventureroProficiente.clases().last().nombre,"Guerrero")
    }

    @Test
    fun unAventureroNoGanaProficienciaSiNoTieneExperiencia() {
        claseService.crearClase("Aventurero")
        claseService.crearClase("Guerrero")
        val atributos = mutableListOf<Atributo>(Atributo.FUERZA)
        claseService.crearMejora("Aventurero", "Guerrero", atributos, 2)

        var aventurero = Aventurero("SordoGony","",30,70,20,10)

        var party = Party("ElCampito","")
        partyService.crear(party)
        partyService.agregarAventureroAParty(party.id()!!, aventurero)
        val exception = assertThrows<RuntimeException> {
            claseService.ganarProficiencia(aventurero.id()!!, "Aventurero", "Guerrero")
        }
        assertThat(exception).hasMessage("No es posible realizar la mejora ")
    }

    @Test
    fun unAventureroConsultaSusPosiblesMejoras() {
        claseService.crearClase("Aventurero")
        claseService.crearClase("Guerrero")
        val atributos = mutableListOf<Atributo>(Atributo.FUERZA)
        claseService.crearMejora("Aventurero", "Guerrero", atributos, 2)


        var aventurero = Aventurero("ElDillom","",30,70,20,10)
        aventurero.darExperiencia(1)

        var party = Party("ElCampito","")
        partyService.crear(party)
        partyService.agregarAventureroAParty(party.id()!!, aventurero)

        Assert.assertEquals(claseService.posiblesMejoras(aventurero.id()!!).size, 1)
    }

    @Test
    fun unAventureroSoloPuedeMejorarATresClasesBasicasPeroNoAUnaClaseConRequisito() {
        claseService.crearClase("Aventurero")
        claseService.crearClase("Guerrero")
        claseService.crearClase("Mago")
        claseService.crearClase("Sacedote")
        val atributosGuerrero = mutableListOf<Atributo>(Atributo.FUERZA)
        claseService.crearMejora("Aventurero", "Guerrero", atributosGuerrero, 3)
        val atributosMago = mutableListOf<Atributo>(Atributo.INTELIGENCIA)
        claseService.crearMejora("Aventurero", "Mago", atributosGuerrero, 3)
        val atributosSacerdote = mutableListOf<Atributo>(Atributo.INTELIGENCIA)
        claseService.crearMejora("Aventurero", "Sacedote", atributosGuerrero, 2)

        claseService.crearClase("Brujo")
        claseService.crearMejora("Mago", "Brujo", atributosMago, 4)
        claseService.requerir("Brujo", "Mago")

        var aventurero = Aventurero("YSY-B","",30,70,20,10)
        aventurero.darExperiencia(1)

        var party = Party("ElCampito","")
        partyService.crear(party)
        partyService.agregarAventureroAParty(party.id()!!, aventurero)

        Assert.assertEquals(claseService.posiblesMejoras(aventurero.id()!!).size, 3)
    }
}