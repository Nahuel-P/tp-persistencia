package ar.edu.unq.epers.tactics.modelo.tacticas

import ar.edu.unq.epers.tactics.modelo.Aventurero
import ar.edu.unq.epers.tactics.modelo.habilidades.Habilidad
import ar.edu.unq.epers.tactics.modelo.tacticas.enums.*
import javax.persistence.*

@Entity
class Tactica() {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    internal var id: Long? = null

    @ManyToOne
    @JoinColumn(name="Aventurero_ID")
    var aventurero: Aventurero? = null

    var prioridad: Int? = null
    var tipoDelreceptor: TipoDeReceptor? = null
    var tipoDeEstadistica: TipoDeEstadistica? = null
    var criterio: Criterio? = null
    var valor: Int? = null
    var accion: Accion ? = null

    constructor(prioridad: Int, tipoDeReceptor: TipoDeReceptor, tipoDeEstadistica: TipoDeEstadistica,
                criterio: Criterio, valor: Int, accion: Accion): this() {
        this.prioridad = prioridad
        this.tipoDelreceptor = tipoDeReceptor
        this.tipoDeEstadistica = tipoDeEstadistica
        this.criterio = criterio
        this.valor = valor
        this.accion = accion
    }

    fun sePuedeUtilizar(emisor: Aventurero, receptor: Aventurero) =
        this.tipoDelreceptor!!.verificar(emisor, receptor) && criterio!!.compararCriterios(tipoDeEstadistica!!.valorDeEstadistica(receptor), valor!!)

    fun utilizarTactica(emisor: Aventurero, receptor: Aventurero) : Habilidad {
        return accion!!.generarHabilidad(emisor,receptor)

    }
}