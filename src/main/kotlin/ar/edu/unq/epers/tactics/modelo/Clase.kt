package ar.edu.unq.epers.tactics.modelo

import javax.persistence.*

@Entity
class Clase() {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null

    @Column(nullable = false)
    var nombre: String = ""

    @ManyToOne
    @JoinColumn(name="Aventurero_ID")
    var aventurero: Aventurero? = null

    constructor(nombreDeClase: String): this() {
        this.nombre = nombreDeClase
    }
}