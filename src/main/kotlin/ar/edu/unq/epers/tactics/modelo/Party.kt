package ar.edu.unq.epers.tactics.modelo

import javax.persistence.*

@Entity
class Party() {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null

    @Column(nullable = false, length = 50, unique = true)
    var nombre: String? = null

    @OneToMany(cascade = [CascadeType.ALL],orphanRemoval = true, fetch = FetchType.EAGER)
    @JoinColumn(name = "party_id")
    private var aventureros: MutableList<Aventurero> = mutableListOf()

//    @OneToMany(mappedBy = "party", cascade = [CascadeType.ALL], fetch = FetchType.EAGER)
//    var aventureros: List<Aventurero> = listOf()
    private var imagenURL= ""
    private var estaEnPelea = false

    constructor(nombre:String, imagenURL: String): this() {
        this.nombre = nombre
        this.imagenURL = imagenURL
    }

    // GETTERS BASICOS
    fun nombre() = nombre
    fun id() = id
    fun aventureros() = aventureros
    fun numeroDeAventureros() = aventureros.size
    fun estaEnPelea() = estaEnPelea

    fun sumarAventurero(aventurero: Aventurero) {
        this.comprobarEsPartyCompleta()
        this.comprobarSiSeEncuentraEnParty(aventurero)
        this.comprobarSiNoPerteneceAOtraParty(aventurero)
        this.aventureros.add(aventurero)
        aventurero.asignarParty(this)
    }

    fun aliadosDe(aventurero: Aventurero): List<Aventurero> {
        val aliados = aventureros.toMutableList()
        aliados.remove(aventurero)
        return aliados
    }

    fun entrarEnPelea() {
        if(this.estaEnPelea){
            throw RuntimeException("No se puede iniciar una pelea: la party ya esta peleando")
        }
        this.estaEnPelea = true
    }

    fun salirDePelea() {
        this.aventureros.forEach {
            it.restaurarAventurero()
        }
        this.estaEnPelea = false
    }

    fun obtenerAventurero(idAventurero : Long) : Aventurero?{
        return aventureros.find { aventurero -> aventurero.id() == idAventurero }
    }

    fun sacarA(aventurero: Aventurero){
        if (aventurero.party != null && !this.estaEnLaParty(aventurero.party!!)) throw RuntimeException("${aventurero.nombre()} no pertenece a ${this.nombre}.")
        aventureros.remove(aventurero)
        aventurero.salirDeLaParty()
    }

    fun ganarPelea() {
        for(aventurero in this.aventureros){
            aventurero.subirDeNivel()
            aventurero.obtenerDinero(500)
        }
    }

    fun clasesDeSusPersonajes() = aventureros.flatMap { it.clases() }

    //***********************************************//
    //PRIVATE METHODS

    private fun maximoDeAventureros() = 5

    private fun comprobarEsPartyCompleta() {
        if (!this.puedeAgregarAventureros()) throw RuntimeException("La party $nombre está completa.")
    }

    private fun puedeAgregarAventureros() = this.numeroDeAventureros() < this.maximoDeAventureros()

    private fun comprobarSiSeEncuentraEnParty(aventurero: Aventurero) {
        if (aventureros.contains(aventurero)) throw RuntimeException("${aventurero.nombre} ya forma parte de la party ${nombre}.")
    }

    private fun comprobarSiNoPerteneceAOtraParty(aventurero: Aventurero) {
        if (aventurero.party != null && !this.estaEnLaParty(aventurero.party!!)) throw RuntimeException("${aventurero.nombre} no pertenece a ${this.nombre}.")
    }

    private fun estaEnLaParty(party: Party) = (party.id != null && party.id()==this.id) || this.nombre==party.nombre

    fun tieneAventurerosVivos(): Boolean {
        return !this.aventureros.any { aventurero -> !aventurero.estaVivo() } != null
    }

    fun cantidadDeAventurerosVivos(): Int {
        var cantVivos=0
        for(aventurero in this.aventureros){
            if (aventurero.estaVivo()){
                cantVivos++
            }
        }
        return cantVivos
    }

    fun sumaDeDañoInflingido(): Int {
        var dañoAcumulado=0
        for(aventurero in this.aventureros){
            if (aventurero.estaVivo()){
                dañoAcumulado += aventurero.dañoInflingido()
            }
        }
        return dañoAcumulado
    }



}