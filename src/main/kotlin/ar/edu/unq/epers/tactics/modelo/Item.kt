package ar.edu.unq.epers.tactics.modelo

import com.google.cloud.firestore.annotation.DocumentId
import javax.persistence.*

@Entity
class Item() {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null

    var nombre: String = ""
    var atributo: Atributo? = null
    var cantidadDeAtributo: Int = 0
    var nivelRequerido: Int = 0
    var precio : Int = 0

    @ManyToOne
    @JoinColumn(name="Aventurero_ID")
    var aventurero: Aventurero? = null


    constructor(nombreDeClase: String, atributo:Atributo, cantidadDeAtributo: Int, nivelRequerido : Int, precio : Int): this() {
        this.nombre = nombreDeClase
        this.atributo = atributo
        this.cantidadDeAtributo= cantidadDeAtributo
        this.nivelRequerido= nivelRequerido
        this.precio= precio
    }

}