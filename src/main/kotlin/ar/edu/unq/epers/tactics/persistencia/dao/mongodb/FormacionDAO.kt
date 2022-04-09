package ar.edu.unq.epers.tactics.persistencia.dao.mongodb

import ar.edu.unq.epers.tactics.modelo.Clase
import ar.edu.unq.epers.tactics.modelo.Formaciones.AtributoDeFormacion
import ar.edu.unq.epers.tactics.modelo.Formaciones.Formacion
import ar.edu.unq.epers.tactics.modelo.Party
import ar.edu.unq.epers.tactics.persistencia.dao.FormacionDAO
import com.mongodb.client.model.Accumulators
import com.mongodb.client.model.Aggregates
import com.mongodb.client.model.Filters.*
import com.mongodb.client.model.Projections
import org.bson.conversions.Bson

class FormacionDAO() : MongoDAO<Formacion>(Formacion::class.java), FormacionDAO {

   override fun crearFormacion(formacion: Formacion) : Formacion{
        save(formacion)
        return formacion
    }

   override fun obtenerTodos(): List<Formacion>{
        return getAll()
    }

    override fun atributosQueCorresponden(clasesDeSusPersonajes: List<Clase>): List<AtributoDeFormacion> {
        val match = Aggregates.match(`in`("clases", clasesDeSusPersonajes))
        val unwind = Aggregates.unwind("\$atributo")
        val project = Aggregates.project(Projections.fields(Projections.include("Atributo"),Projections.include("clases")))
        val group = Aggregates.group("\$Atributo", Accumulators.sum("valor", "\$atributo"))

        return aggregate(listOf(match,unwind,project,group), AtributoDeFormacion::class.java)
    }

    override fun formacionesQuePosee(party: Party): List<Formacion> {
        val clases = party.aventureros().map { it.clases() }.flatten().groupingBy { it.nombre }.eachCount()

        var bsonlist : MutableList<Bson> = mutableListOf()
        clases.forEach { (clase, cant) ->
            bsonlist.add( and( eq("clase", clase), lte("cantidad", cant) ) )
        }

        val match = Aggregates.match(elemMatch("requisitos", or( bsonlist )))
        var result =  aggregate(listOf(match), Formacion::class.java)
        return result
    }
}