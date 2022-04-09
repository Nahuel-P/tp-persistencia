package ar.edu.unq.epers.tactics.modelo

import ar.edu.unq.epers.tactics.modelo.habilidades.Habilidad
import javax.persistence.*

@Entity
class Pelea() {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null

    @OneToMany(cascade = [CascadeType.ALL], orphanRemoval = true, fetch = FetchType.LAZY)
    @Column(name="habilidadesUtilizadas")
    @JoinColumn(name = "pelea_id")
    private var habilidadesUtilizadas: MutableList<Habilidad> = mutableListOf()

    //@OneToMany(mappedBy = "pelea",cascade = [CascadeType.ALL], fetch = FetchType.EAGER)
    //@Fetch(value = FetchMode.SUBSELECT)
    //var habilidadesUtilizadas: MutableList<Habilidad> = mutableListOf()
    @OneToOne
    private  var partyA: Party? = null

    @OneToOne
    private var partyB: Party? = null

    private var estaFinalizada = false

    @OneToOne
    lateinit var partyGanadora : Party


    constructor(partyA : Party, partyB :Party) : this() {
        this.partyA = partyA
        this.partyB = partyB
    }

    fun id() = id
    fun partyA() = partyA
    fun partyB() = partyB

    fun finalizar() {
        if (estaFinalizada) throw RuntimeException("La pelea ya ha terminado antes.")
            estaFinalizada = true
            partyGanadora = this.obtenerPartGanadora()
            partyA?.salirDePelea()
            partyB?.salirDePelea()
    }

    fun estaFinalizada() : Boolean {
        return this.estaFinalizada
    }

    fun obtenerPartGanadora() : Party {
        //establecemos un criterio, el ganador es si una party sobrevive y la otra no, sino que tengas mas vivos, y si no, que haga mas daño
        if(!this.estaFinalizada()){
            throw RuntimeException("La pelea sigue en curso")
        }
        var ganaPartyA =    (partyA!!.tieneAventurerosVivos() && !partyB!!.tieneAventurerosVivos()) ||
                            (partyA!!.cantidadDeAventurerosVivos() > partyB!!.cantidadDeAventurerosVivos()) ||
                            (partyA!!.sumaDeDañoInflingido() > partyB!!.sumaDeDañoInflingido())
        if(ganaPartyA){
            partyA!!.ganarPelea()
            return partyA!!
        }
        else if(!ganaPartyA && partyB!!.tieneAventurerosVivos()){
            partyA!!.ganarPelea()
            return partyB!!
        }else{
            throw RuntimeException("No hubo una party ganadora")
        }
    }

    fun obtenerAventurero(idAventurero : Long) : Aventurero? {
        var aventurero = partyA!!.obtenerAventurero(idAventurero)
        return if (aventurero != null) aventurero else partyB!!.obtenerAventurero(idAventurero)
    }

    fun habilidadesUsadas() = this.habilidadesUtilizadas

    fun agregarHabilidad(habilidad: Habilidad) {
        if (!habilidadesUtilizadas.contains(habilidad)) {
            habilidadesUtilizadas.add(habilidad)
        }
    }

}