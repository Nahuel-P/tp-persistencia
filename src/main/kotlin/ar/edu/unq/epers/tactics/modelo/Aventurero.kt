package ar.edu.unq.epers.tactics.modelo

import ar.edu.unq.epers.tactics.modelo.habilidades.Habilidad
import ar.edu.unq.epers.tactics.modelo.tacticas.Tactica
import org.hibernate.annotations.Fetch
import org.hibernate.annotations.FetchMode
import javax.persistence.*

@Entity
class Aventurero() {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private var id: Long? = null
    @OneToMany(cascade = [CascadeType.ALL], fetch = FetchType.EAGER)
    @Fetch(value = FetchMode.SUBSELECT)
    @JoinColumn(name = "aventurero_id")
    private var tacticas: MutableList<Tactica> = mutableListOf()

    @ManyToOne
    var party: Party? = null
    var nombre: String? = null

    @OneToOne
    private var defensor: Aventurero? = null
    private var turnosConDefensa = 0
    private var dineroDisponible: Int = 0

    @ManyToMany(cascade = [CascadeType.ALL], fetch = FetchType.EAGER)
    @Fetch(value = FetchMode.SUBSELECT)
    var clases: MutableList<Clase> = mutableListOf(Clase("Aventurero"))

//    @OneToMany(cascade = [CascadeType.ALL], fetch = FetchType.EAGER)
//    @Fetch(value = FetchMode.SUBSELECT)
//    var equipo: MutableList<Item> = mutableListOf()

    @OneToMany(cascade = [CascadeType.ALL], fetch = FetchType.EAGER)
    @Fetch(value = FetchMode.SUBSELECT)
    //var cofre: Cofre = Cofre()
    var inventario: MutableList<Item> = mutableListOf()

    private var nivel = 1
    private var experiencia : Int = 0
    private var imagenURL: String = ""
    private var vidaBase: Int = 0
    private var manaBase: Int = 0
    private var vidaActual : Int = 0
    private var manaActual : Int = 0

    private var inteligencia: Int = 1
        set(nuevoValor){
            this.verificarNuevoAtributo("inteligencia",nuevoValor)
            field = nuevoValor
        }
    private var destreza: Int = 1
        set(nuevoValor){
            this.verificarNuevoAtributo("destreza",nuevoValor)
            field = nuevoValor
        }

    private var constitucion: Int = 1
        set(nuevoValor){
            this.verificarNuevoAtributo("constitucion",nuevoValor)
            field = nuevoValor
        }

    private var fuerza: Int = 1
        set(nuevoValor){
            this.verificarNuevoAtributo("fuerza",nuevoValor)
            field = nuevoValor
        }

    private fun verificarNuevoAtributo(atributo: String, valor: Int) {
        if (valor > 100 || valor < 1) throw RuntimeException("El valor $valor no es valido para el atributo $atributo. El valor debe ser entre 1 y 100")
    }

    private var dañoInflingido: Int = 0
    private var dañoRecibido: Int = 0
    private var poderTotal: Int =0

    constructor(nombre: String?, imagenURL: String, fuerza: Int, destreza: Int, constitucion: Int, inteligencia: Int) : this() {
        this.nombre = nombre
        this.nivel = 1
        this.imagenURL = imagenURL
        this.inteligencia = inteligencia
        this.destreza = destreza
        this.constitucion = constitucion
        this.fuerza = fuerza
        this.vidaBase = (5 * nivel) + (2 * constitucion) + fuerza
        this.vidaActual = this.vidaBase
        this.manaBase = nivel + inteligencia
        this.manaActual = this.manaBase
        this.poderTotal = calcularPoderTotal()
    }
    // GETTERS BASICOS
    fun id() = id
    fun nivel() = nivel
    fun nombre() = nombre
    fun fuerza() = fuerza
    fun destreza() = destreza
    fun constitucion() = constitucion
    fun inteligencia() = inteligencia
    fun vidaBase() = vidaBase
    fun manaBase() = manaBase
    fun defensor() = defensor
    fun party() = party
    fun clases() = clases
    fun experiencia() = experiencia
    fun dineroDisponible() = dineroDisponible
    fun inventario() = inventario

    // DE COMBATE
    fun vidaActual() = vidaActual
    fun manaActual() = manaActual
    fun dañoRecibido() = dañoRecibido
    fun dañoInflingido() = dañoInflingido
    fun turnosConDefensa() = turnosConDefensa
    fun estaVivo() = this.vidaActual() >= 1

    fun armadura() = nivel + constitucion
    fun velocidad() = nivel + destreza
    fun dañoFisico() = nivel + fuerza + (destreza/2)
    fun poderMagico() = nivel + inteligencia
    fun precisionFisica() = nivel + fuerza + destreza
    fun tacticas() = tacticas


    private fun calcularPoderTotal(): Int {
        return dañoFisico() + precisionFisica() + poderMagico()
    }

    //SETTERS Y CAMBIOS DE ESTADO
    fun asignarParty(party: Party){
        this.party = party
    }
    fun salirDeLaParty(){
        this.party=null
    }

    fun agregarTactica(nuevaTactica: Tactica) {
        this.tacticas.add(nuevaTactica)
    }

    fun llenarMana(){
        this.manaActual = manaBase
    }

    fun utilizarMana() {
        this.manaActual = manaActual() - 5
    }

    fun recibirDefensor(defensor: Aventurero) {
        this.defensor=defensor
        this.turnosConDefensa=3
    }

    private fun tieneDefensorActivo(): Boolean {
        return this.turnosConDefensa()>0 && defensor()!=null
    }

    fun meditar(meditacion: Int){
        this.manaActual = manaActual() + meditacion
    }

    fun recibirHabilidadDeDaño(cantidadDeDaño: Int){
        if (this.tieneDefensorActivo()){
            this.defensor!!.defenderAtaque(cantidadDeDaño)
            this.turnosConDefensa--
        }
        else{
            this.vidaActual = vidaActual - cantidadDeDaño
            this.dañoRecibido = this.dañoRecibido + cantidadDeDaño
        }
    }

    fun defenderAtaque(cantidadDeDaño: Int){
        this.vidaActual = this.vidaActual - (cantidadDeDaño/2)
        this.dañoRecibido = this.dañoRecibido + cantidadDeDaño
    }

    fun curarCon(sanacionRecibida: Int){
        this.vidaActual = vidaActual+ sanacionRecibida
    }

    fun restaurarAventurero() {
        this.dañoRecibido = 0
        this.turnosConDefensa = 0
        this.defensor = null
        this.manaActual=manaBase
        this.vidaActual=vidaBase
        this.dañoInflingido=0
    }

    fun incrementarDañoInflingido(daño : Int){
        this.dañoInflingido = dañoInflingido + daño
    }

    fun esAliadoDe(otroAventurero: Aventurero) = aliados().contains(otroAventurero)

    fun esEnemigoDe(otroAventurero: Aventurero) = otroAventurero != this && !this.esAliadoDe(otroAventurero)

    fun aliados(): List<Aventurero> {
        if (party == null) return listOf()
        return party!!.aliadosDe(this)
    }

    fun resolverTurno(enemigos: MutableList<Aventurero>) : Habilidad{
        this.tacticas.sortBy { it.prioridad }
        val posiblesReceptores = this.aliados() + enemigos + this
        for (tactica in tacticas) {
            val receptor = posiblesReceptores.firstOrNull { receptor ->
                receptor.estaVivo() && tactica.sePuedeUtilizar(this, receptor) }
            if (receptor != null) {
                return tactica.utilizarTactica(this, receptor)
            }
        }
        return throw RuntimeException("No hubo tactica para aplicar")
    }

    fun subirDeNivel(){
        this.nivel = nivel + 1
    }

    fun darExperiencia(puntos : Int){
        this.experiencia = experiencia + puntos
    }

    fun obtenerDinero(sumaMonetaria : Int){
        this.dineroDisponible = dineroDisponible + sumaMonetaria
    }

    fun ganarProficiencia(mejoraAObtener: Mejora) {
        this.experiencia -= 1
        var claseAdquirida = mejoraAObtener.clasePosterior
        this.clases.add(claseAdquirida)

        mejoraAObtener.atributos.forEach {
            when(it) {
                Atributo.INTELIGENCIA -> { this.inteligencia += mejoraAObtener.cantidadDeAtributos }
                Atributo.DESTREZA -> { this.destreza += mejoraAObtener.cantidadDeAtributos }
                Atributo.FUERZA -> { this.fuerza += mejoraAObtener.cantidadDeAtributos }
                Atributo.CONSTITUCION -> { this.constitucion += mejoraAObtener.cantidadDeAtributos }
            }
        }
        this.recalcularPuntos()
        this.restaurarAventurero()
    }

    fun recalcularPuntos(){
        this.vidaBase = (5 * nivel) + (2 * constitucion) + fuerza
        this.poderTotal = calcularPoderTotal()
        this.manaBase = nivel + inteligencia
    }

    fun comprar(itemNuevo: Item){
        if (itemNuevo.precio<=this.dineroDisponible){
            this.dineroDisponible= dineroDisponible-itemNuevo.precio
            //this.cofre.addItem(itemNuevo)
            this.inventario.add(itemNuevo)
            when(itemNuevo.atributo) {
                Atributo.INTELIGENCIA -> { this.inteligencia += itemNuevo.cantidadDeAtributo }
                Atributo.DESTREZA -> { this.destreza += itemNuevo.cantidadDeAtributo }
                Atributo.FUERZA -> { this.fuerza += itemNuevo.cantidadDeAtributo }
               Atributo.CONSTITUCION -> { this.constitucion += itemNuevo.cantidadDeAtributo }
            }
            this.recalcularPuntos()

        }else{
            throw RuntimeException("El aventurero no tiene el dinero necesario para comprar el item")
        }
    }

//    fun equipar(itemAEquipar: Item){
//        //if (cofre.contiene(itemAEquipar) && itemAEquipar.nivelRequerido<= this.nivel){
//        if (inventario.contains(itemAEquipar) && itemAEquipar.nivelRequerido<= this.nivel){
//            this.equipo.add(itemAEquipar)
//            when(itemAEquipar.atributo) {
//                Atributo.INTELIGENCIA -> { this.inteligencia += itemAEquipar.cantidadDeAtributo }
//                Atributo.DESTREZA -> { this.destreza += itemAEquipar.cantidadDeAtributo }
//                Atributo.FUERZA -> { this.fuerza += itemAEquipar.cantidadDeAtributo }
//                Atributo.CONSTITUCION -> { this.constitucion += itemAEquipar.cantidadDeAtributo }
//            }
//            this.recalcularPuntos()
//        }else{
//            throw RuntimeException("El aventurero no puede equiparse con ese item")
//        }
//    }

}
