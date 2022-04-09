package ar.edu.unq.epers.tactics.modelo.habilidades

import ar.edu.unq.epers.tactics.modelo.Aventurero
import javax.persistence.*

@Entity
@Inheritance(strategy= InheritanceType.JOINED)
abstract class Habilidad(@ManyToOne val emisor: Aventurero, @ManyToOne val receptor: Aventurero) {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null

    var ejecutada = false
    var dañoFisico: Int = 0
    var dañoMagico: Int = 0
    var curacion: Int = 0
    var meditacion: Int = 0
    var defiende = false
    var acierta = false

    fun receptor() : Aventurero = this.receptor
    fun emisor() : Aventurero = this.emisor

    //funcion de testing
    fun volverlaInfalible(){
        this.acierta=true
    }

    abstract fun resolverHabilidadContra(receptor: Aventurero,emisor: Aventurero)

    abstract  fun conValores(emisor: Aventurero,receptor: Aventurero) : Habilidad
}
