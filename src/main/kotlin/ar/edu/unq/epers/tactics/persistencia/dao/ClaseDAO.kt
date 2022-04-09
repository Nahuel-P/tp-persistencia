package ar.edu.unq.epers.tactics.persistencia.dao

import ar.edu.unq.epers.tactics.modelo.Clase
import ar.edu.unq.epers.tactics.modelo.Mejora

interface ClaseDAO {

    fun crear(entity: Clase): Clase
    fun crearMejora(mejora: Mejora) : Mejora
    fun requerir(claseSiguiente: String, claseAnterior: String)
    fun puedeMejorarse(aventurero: Clase, mejora: List<String>): Boolean
    fun mejoraEntreClases(nombreClaseActual: String, nombreClaseAMejorar: String): Mejora
    fun mejorasParaClases(clases: List<String>): Set<Mejora>
    fun requeridasDe(clase: Clase): List<Clase>
    fun clear()


}
