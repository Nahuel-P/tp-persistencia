package ar.edu.unq.epers.tactics.service

import ar.edu.unq.epers.tactics.modelo.Atributo
import ar.edu.unq.epers.tactics.modelo.Aventurero
import ar.edu.unq.epers.tactics.modelo.Formaciones.AtributoDeFormacion
import ar.edu.unq.epers.tactics.modelo.Formaciones.Formacion
import ar.edu.unq.epers.tactics.modelo.Formaciones.Requerimiento
import ar.edu.unq.epers.tactics.modelo.Party
import ar.edu.unq.epers.tactics.modelo.tacticas.Tactica
import ar.edu.unq.epers.tactics.modelo.tacticas.enums.Accion
import ar.edu.unq.epers.tactics.modelo.tacticas.enums.Criterio
import ar.edu.unq.epers.tactics.modelo.tacticas.enums.TipoDeEstadistica
import ar.edu.unq.epers.tactics.modelo.tacticas.enums.TipoDeReceptor
import ar.edu.unq.epers.tactics.persistencia.dao.AventureroDAO
import ar.edu.unq.epers.tactics.persistencia.dao.PartyDAO
import ar.edu.unq.epers.tactics.persistencia.dao.hibernate.HibernateAventureroDAO
import ar.edu.unq.epers.tactics.persistencia.dao.hibernate.HibernatePartyDAO
import ar.edu.unq.epers.tactics.persistencia.dao.mongodb.FormacionDAO
import ar.edu.unq.epers.tactics.persistencia.dao.neo4j.Neo4JClaseDAO
import ar.edu.unq.epers.tactics.service.impl.AventureroServiceImpl
import ar.edu.unq.epers.tactics.service.impl.ClaseServiceImpl
import ar.edu.unq.epers.tactics.service.impl.FormacionServiceImpl
import ar.edu.unq.epers.tactics.service.impl.PartyServiceImpl
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.recursive.comparison.RecursiveComparisonConfiguration
import org.junit.Assert
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test


class FormacionServiceTest {
    lateinit var formacionDAO: FormacionDAO
    private var partyDao: PartyDAO = HibernatePartyDAO()
    private var partyService: PartyServiceImpl = PartyServiceImpl(partyDao)
    private val claseDAO = Neo4JClaseDAO()
    private val claseService: ClaseServiceImpl = ClaseServiceImpl(claseDAO, HibernateAventureroDAO())
    private var aventureroDao: AventureroDAO = HibernateAventureroDAO()
    private var aventureroService: AventureroServiceImpl = AventureroServiceImpl(aventureroDao, partyDao)
    private var idParty=0
    var listaDeRequerimientos : MutableList<Requerimiento> = mutableListOf()
    lateinit var formacionService: FormacionService

    @BeforeEach
    fun setup() {
        formacionDAO = FormacionDAO()
        formacionService = FormacionServiceImpl(formacionDAO, HibernatePartyDAO())
        formacionDAO.deleteAll()
    }


        @Test
    fun obtenerTodasSinFormacionCreada() {
        var service = FormacionServiceImpl(formacionDAO,partyDao)
        var formaciones = service.todasLasFormaciones().size
        Assert.assertEquals(0, formaciones)
    }
    @Test
    fun crearFormacion() {
        var service = FormacionServiceImpl(formacionDAO, partyDao)
        val aventurerocla = claseService.crearClase("Aventurero")
        val caballero = claseService.crearClase("Caballero")
        val requerimientos = listOf(
            Requerimiento(aventurerocla, 1),
            Requerimiento(caballero, 2)
        )
        val atributos = listOf(
            AtributoDeFormacion(1, Atributo.FUERZA),
            AtributoDeFormacion(2, Atributo.CONSTITUCION)
        )
        val formacion = service.crearFormacion("nombre", requerimientos, atributos)

        Assert.assertEquals(formacion.nombre, "nombre")
        Assert.assertEquals(formacion.requisitos, requerimientos)
        Assert.assertEquals(formacion.atributosDeFormacion, atributos)
    }


    @Test
    fun obtenerTodasConUnaFormacionCreada() {
        var service = FormacionServiceImpl(formacionDAO,partyDao)
        val aventureroclase = claseService.crearClase("Aventurero")
        val caballero = claseService.crearClase("Caballero")
        val requerimientos = listOf(
            Requerimiento(aventureroclase,1),
            Requerimiento(caballero,2)
        )
        val atributos = listOf(
            AtributoDeFormacion(1,Atributo.FUERZA),
            AtributoDeFormacion(2, Atributo.CONSTITUCION)
        )
        service.crearFormacion("nombreDeOtraFormacionNdaaaah",requerimientos,atributos)
        var formaciones = service.todasLasFormaciones().size
        Assert.assertEquals(1, formaciones)
    }



    @Test
    fun unaPartyNoTieneUnaDeLasFormaciones(){
        var service = FormacionServiceImpl(formacionDAO,partyDao)
        val aventureroclase = claseService.crearClase("Aventurero")
        val paladin = claseService.crearClase("Paladin")
        val requerimientos = listOf(
            Requerimiento(aventureroclase,4)
        )
        val atributos = listOf(
            AtributoDeFormacion(1,Atributo.FUERZA),
            AtributoDeFormacion(2, Atributo.CONSTITUCION)
        )

        val requerimientos2 = listOf(
            Requerimiento(paladin,1)
        )
        val atributos2 = listOf(
            AtributoDeFormacion(1,Atributo.FUERZA)
        )

        val formacion = service.crearFormacion("FormacionDeChadAventurero",requerimientos,atributos)
        val formacion2 = service.crearFormacion("FormacionOtra",requerimientos2,atributos2)

        var party = Party("Partiparty", "")
        var partyCreada = partyService.crear(party)

        var aventurero1 = Aventurero()
        aventurero1.nombre = "aventurero1"

        partyService.agregarAventureroAParty(partyCreada.id()!!,aventurero1)


        var formaciones = service.formacionesQuePosee(partyCreada.id()!!)
        Assert.assertEquals(0, formaciones.size)

    }

    @Test
    fun cuandoUnaPartyNoTieneAventurerosNoTieneAtributosDeFormacion(){
        val partyId = partyNuevaPersistida()
        val atributosQueCorresponden = formacionService.atributosQueCorresponden(partyId)

        Assert.assertTrue(atributosQueCorresponden.isEmpty())
    }

    @Test
    fun sePersisteUnaFormacion() {
        val caballero = claseService.crearClase("Caballero")
        val caballeroReq = Requerimiento(caballero , 1)
        listaDeRequerimientos.add(caballeroReq)

        val atributos = listOf(
            AtributoDeFormacion(1,Atributo.FUERZA),
            AtributoDeFormacion(2, Atributo.CONSTITUCION)
        )

        val formacionCreada = formacionService.crearFormacion("Formacion1", listaDeRequerimientos, atributos)

        val formacionEsperada = Formacion("Formacion1", listaDeRequerimientos, atributos)
        assertThat(formacionCreada).usingRecursiveComparison().ignoringFields("id").isEqualTo(formacionEsperada)
    }

//    @Test
//    fun cuandoLosAventurerosDePartyCumpleConRequisitosDeFormacionLeCorrespondenAtributos() {
//        val caballero = claseService.crearClase("Caballero")
//        val partyId = partyNuevaPersistida()
//        crearAventureroEnParty(partyId)
//
//        val aventureroReq = Requerimiento(caballero , 1)
//        listaDeRequerimientos.add(aventureroReq)
//        val atributos = listOf(
//            AtributoDeFormacion(1,Atributo.FUERZA)
//        )
//        val formacionCreada = formacionService.crearFormacion("Nombre de formacion", listaDeRequerimientos, atributos)
//        partyDao.recuperar(partyId).
//        val atributosQueCorresponden = formacionService.atributosQueCorresponden(partyId)
//
//        Assert.assertEquals(atributosQueCorresponden.size, atributos.size)
//    }



    @AfterEach
    fun dropAll() {
        formacionDAO.deleteAll()
    }

    fun partyNuevaPersistida(): Long {
        val party = Party("Nombre de party${idParty}", "")
        val partyId = partyService.crear(party).id()!!
        this.idParty+=1
        return partyId
    }

    fun crearAventureroEnParty(partyId: Long): Aventurero{
        val aventurero = Aventurero("Aventurero", "", 10, 10, 100, 10)
        val ataqueFisico = this.tacticaDeAtaqueFisico()
        aventurero.agregarTactica(ataqueFisico)
        partyService.agregarAventureroAParty(partyId, aventurero)
        return aventurero
    }

    fun tacticaDeAtaqueFisico(): Tactica {
        return Tactica(1, TipoDeReceptor.ENEMIGO, TipoDeEstadistica.VIDA, Criterio.MAYOR_QUE, 0, Accion.ATAQUE_FISICO)
    }
}