package ar.edu.unq.epers.tactics.service

import ar.edu.unq.epers.tactics.modelo.Atributo
import ar.edu.unq.epers.tactics.modelo.Aventurero
import ar.edu.unq.epers.tactics.modelo.Clase
import ar.edu.unq.epers.tactics.modelo.Mejora

interface ClaseService {
    fun crearClase(nombreDeLaClase: String): Clase
    fun crearMejora(nombreClaseInicio:String, nombreClaseAMejorar:String, atributos:MutableList<Atributo>, valorAAumentar:Int): Mejora
    fun requerir(clasePredecesora: String, claseSucesora: String)
    fun puedeMejorar(aventureroID: Long, mejora: Mejora): Boolean
    fun ganarProficiencia(aventureroId:Long, nombreClaseActual:String,nombreClaseAMejorar:String): Aventurero
    fun posiblesMejoras(aventureroID: Long): Set<Mejora>
    fun requeridasDe(clase : Clase) : List<Clase>
    fun clear()
}