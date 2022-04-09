package ar.edu.unq.epers.tactics.service

import ar.edu.unq.epers.tactics.modelo.Atributo
import ar.edu.unq.epers.tactics.modelo.Aventurero
import ar.edu.unq.epers.tactics.modelo.Item
import ar.edu.unq.epers.tactics.modelo.Party
import ar.edu.unq.epers.tactics.persistencia.dao.AventureroDAO
import ar.edu.unq.epers.tactics.persistencia.dao.PartyDAO
import ar.edu.unq.epers.tactics.persistencia.dao.firebase.ItemDAO
import ar.edu.unq.epers.tactics.persistencia.dao.hibernate.HibernateAventureroDAO
import ar.edu.unq.epers.tactics.persistencia.dao.hibernate.HibernatePartyDAO
import ar.edu.unq.epers.tactics.persistencia.dao.neo4j.Neo4JClaseDAO
import ar.edu.unq.epers.tactics.service.impl.AventureroServiceImpl
import ar.edu.unq.epers.tactics.service.impl.ClaseServiceImpl
import ar.edu.unq.epers.tactics.service.impl.ItemServiceImpl
import ar.edu.unq.epers.tactics.service.impl.PartyServiceImpl
import org.assertj.core.api.Assertions
import org.junit.Assert
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

class ItemServiceTest {
    private var aventureroDao: AventureroDAO = HibernateAventureroDAO()
    private var itemDAO : ItemDAO = ItemDAO()
    private var itemService: ItemServiceImpl = ItemServiceImpl(itemDAO,aventureroDao)

    private val claseDAO = Neo4JClaseDAO()

    private var partyDao: PartyDAO = HibernatePartyDAO()
    private var aventureroService: AventureroServiceImpl = AventureroServiceImpl(aventureroDao, partyDao)
    private var partyService: PartyServiceImpl = PartyServiceImpl(partyDao)


    @BeforeEach
    fun setUp() {
        itemService.eliminarTodos()
        partyService.eliminarTodo()

    }

    @Test
    fun seCreaUnItemYSePersisteEnFirebase() {
        val item = Item("Gorro", Atributo.CONSTITUCION,10,1,100)
        var itemCreado = itemService.crearItem(item)
        Assert.assertEquals(itemCreado.nombre,"Gorro")
        Assert.assertEquals(itemCreado.atributo,Atributo.CONSTITUCION)
        Assert.assertEquals(itemCreado.cantidadDeAtributo,10)
        Assert.assertEquals(itemCreado.nivelRequerido,1)
        Assert.assertEquals(itemCreado.precio,100)
    }

    @Test
    fun siSeCreanDosItemsConLaMismaClaveNoSeDuplicanPeroSobreescriben() {
        val item1 = Item("Gorro", Atributo.CONSTITUCION,10,1,100)
        val item2 = Item("Gorro", Atributo.FUERZA,11,1,200)
        var itemCreado = itemService.crearItem(item1)
        Assert.assertEquals(itemCreado.nombre,"Gorro")
        Assert.assertEquals(itemCreado.atributo,Atributo.CONSTITUCION)
        Assert.assertEquals(itemCreado.cantidadDeAtributo,10)
        Assert.assertEquals(itemCreado.nivelRequerido,1)
        Assert.assertEquals(itemCreado.precio,100)

        itemCreado = itemService.crearItem(item2)
        Assert.assertEquals(itemCreado.nombre,"Gorro")
        Assert.assertEquals(itemCreado.atributo,Atributo.FUERZA)
        Assert.assertEquals(itemCreado.cantidadDeAtributo,11)
        Assert.assertEquals(itemCreado.nivelRequerido,1)
        Assert.assertEquals(itemCreado.precio,200)
    }

    @Test
    fun seEliminaUnItem() {
        val item1 = Item("Zapatos", Atributo.CONSTITUCION,10,1,100)
        val item2 = Item("Camisa", Atributo.INTELIGENCIA,10,1,100)
        var itemCreado1 = itemService.crearItem(item1)
        var itemCreado2 = itemService.crearItem(item2)
        itemService.eliminar(itemCreado1.nombre)
        val exception = assertThrows<RuntimeException> {
            itemService.buscarItem(itemCreado1.nombre)
        }
        Assertions.assertThat(exception).hasMessage("El item no existe")
    }

    @Test
    fun seActualizaUnItemCambiadoleElAtributoYCantidadDeAtributo() {
        val item = Item("Pantalones", Atributo.CONSTITUCION,10,1,100)
        var itemCreado = itemService.crearItem(item)
        itemService.actualizar(itemCreado.nombre, itemCreado.atributo!!,5,itemCreado.nivelRequerido,itemCreado.precio)
        val itemRestore = itemService.buscarItem("Pantalones")

        val nombreRestore = itemRestore?.nombre
        val cantidadRestore = itemRestore?.cantidadDeAtributo
        val atributoRestore = itemRestore?.atributo

        Assert.assertEquals(nombreRestore,"Pantalones")
        Assert.assertEquals(cantidadRestore,5)
    }

    @Test
    fun unAventureroSeCompraUnItemYLoDejaEnSuInventario() {
        val item = Item("Medias", Atributo.FUERZA,10,1,100)
        var itemCreado = itemService.crearItem(item)

        var aventurero = Aventurero("BaneadoPorBo...","",30,70,20,10)
        aventurero.obtenerDinero(200)
        aventurero.subirDeNivel()
        aventurero.comprar(itemCreado)

        var party = Party("ElTeamTwitch","")
        partyService.crear(party)
        partyService.agregarAventureroAParty(party.id()!!, aventurero)

        var aventureroRecuperado = aventureroService.recuperar(aventurero.id()!!)
        Assert.assertEquals(aventureroRecuperado.inventario.size,1)
        Assert.assertEquals(aventureroRecuperado.dineroDisponible(),100)
    }

    @Test
    fun unSeMejoraTrasComprarUnItem() {
        val item = Item("Medias", Atributo.FUERZA,10,1,100)
        var itemCreado = itemService.crearItem(item)

        var aventurero = Aventurero("elmasCringe","",30,70,20,10)
        aventurero.obtenerDinero(200)
        aventurero.subirDeNivel()
        aventurero.comprar(itemCreado)

        var party = Party("ElTeamTwitch","")
        partyService.crear(party)
        partyService.agregarAventureroAParty(party.id()!!, aventurero)

        var aventureroRecuperado = aventureroService.recuperar(aventurero.id()!!)
        Assert.assertEquals(aventureroRecuperado.inventario.size,1)
        Assert.assertEquals(aventureroRecuperado.dineroDisponible(),100)
        Assert.assertEquals(aventureroRecuperado.fuerza(),40)
    }


}