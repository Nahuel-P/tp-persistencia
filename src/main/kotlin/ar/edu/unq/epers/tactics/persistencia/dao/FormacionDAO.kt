package ar.edu.unq.epers.tactics.persistencia.dao

import ar.edu.unq.epers.tactics.modelo.Clase
import ar.edu.unq.epers.tactics.modelo.Formaciones.AtributoDeFormacion
import ar.edu.unq.epers.tactics.modelo.Formaciones.Formacion
import ar.edu.unq.epers.tactics.modelo.Party

interface FormacionDAO {
    //fun guardar(formacion: Formacion):Formacion
    fun crearFormacion(formacion : Formacion): Formacion
    fun obtenerTodos():List<Formacion>
    fun atributosQueCorresponden(clasesDeSusPersonajes: List<Clase>):List<AtributoDeFormacion>
    fun formacionesQuePosee(party: Party): List<Formacion>

}