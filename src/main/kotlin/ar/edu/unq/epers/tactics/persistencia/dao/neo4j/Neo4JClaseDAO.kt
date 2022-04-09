package ar.edu.unq.epers.tactics.persistencia.dao.neo4j

import ar.edu.unq.epers.tactics.modelo.Atributo
import ar.edu.unq.epers.tactics.modelo.Clase
import ar.edu.unq.epers.tactics.modelo.Mejora
import ar.edu.unq.epers.tactics.persistencia.dao.ClaseDAO
import ar.edu.unq.epers.tactics.service.runner.Neo4JTransactionRunner
import org.neo4j.driver.Record
import org.neo4j.driver.Value
import org.neo4j.driver.Values

class Neo4JClaseDAO : ClaseDAO{

    override fun crear(clase: Clase): Clase {
        Neo4JTransactionRunner().runTrx { session ->
            session.writeTransaction {
                val query = "MERGE (n:Clase {nombre: ${'$'}nombre})"
                it.run(
                    query, Values.parameters(
                        "nombre", clase.nombre
                    )
                )
            }
        }

        return clase
    }

    override fun crearMejora(mejora: Mejora) : Mejora{
        Neo4JTransactionRunner().runTrx() { session ->
            session.writeTransaction {
                val query = "MATCH (ClaseInferior: Clase { nombre: ${'$'}elnombreCI }) "+
                        "MATCH (ClaseSuperior: Clase { nombre: ${'$'}elnombreCS }) "+
                        "MERGE (ClaseInferior)-[:Habilita { FUERZA: ${'$'}cantFuerza , DESTREZA: ${'$'}cantDestreza , "+
                        "CONSTITUCION: ${'$'}cantConstitucion , INTELIGENCIA: ${'$'}cantInteligencia }]->(ClaseSuperior)"
                it.run(query, Values.parameters(
                    "elnombreCI", mejora.clasePrevia.nombre,
                    "elnombreCS", mejora.clasePosterior.nombre,
                    "cantFuerza", cantidadParaAtributo(mejora, Atributo.FUERZA),
                    "cantDestreza", cantidadParaAtributo(mejora, Atributo.DESTREZA),
                    "cantConstitucion", cantidadParaAtributo(mejora, Atributo.CONSTITUCION),
                    "cantInteligencia", cantidadParaAtributo(mejora, Atributo.INTELIGENCIA)
                ))
            }
        }
        return mejora
    }

    private fun cantidadParaAtributo(mejora: Mejora, atributo: Atributo): Int {
        var valor = 0
        if (mejora.atributos.contains(atributo)) {
            return mejora.cantidadDeAtributos
        }
        return valor
    }

    override fun requerir(claseSiguiente: String, claseAnterior: String) {
        Neo4JTransactionRunner().runTrx { session ->
            val query = """
               MATCH (claseHabilitada:Clase {nombre: ${'$'}nombreClaseHabilitada})
               MATCH (claseRequerida:Clase {nombre: ${'$'}nombreClaseRequerida})
               MERGE (claseHabilitada)-[:requiere]->(claseRequerida)
            """
            session.run(
                query,
                Values.parameters(
                    "nombreClaseHabilitada", claseSiguiente,
                    "nombreClaseRequerida", claseAnterior
                )
            )
        }
    }

    override fun requeridasDe(clase: Clase): MutableList<Clase> {
        return Neo4JTransactionRunner().runTrx { session ->
            val query = """
               MATCH (clase:Clase {nombre: ${'$'}nombreClase})  
               MATCH (clase)-[:requiere]->(requisito) 
               RETURN requisito.nombre
            """
            val result = session.run(query, Values.parameters("nombreClase", clase.nombre))
            result.list {
                Clase(it[0].asString())
            }
        }
    }

    override fun puedeMejorarse(claseSiguiente: Clase, clases: List<String>): Boolean {
        var stringDeClases: String = stringDeClases(clases)

        return Neo4JTransactionRunner().runTrx() { session ->
            val queryStatement = "MATCH (clActual:Clase)-[hab:Habilita]->(clBuscada:Clase)-[req:Requiere]->(clRequerida:Clase) " +
                    "WHERE clBuscada.nombre = ${'$'}nombreClaseBuscada AND NOT clBuscada.nombre IN [ " + stringDeClases + " ] " +
                    "AND clActual.nombre IN [" + stringDeClases + "] " +
                    "RETURN (size(collect(clRequerida.nombre)) = 0 ) OR all(n IN collect(clRequerida.nombre) WHERE n IN [ " + stringDeClases + " ]) "
            val result = session.run(queryStatement, Values.parameters(
                "nombreClaseBuscada", claseSiguiente.nombre
            ))
            val record = result.single()
            record[0].asBoolean()
        }
    }

    override fun mejoraEntreClases(nombreClaseActual: String, nombreClaseAMejorar: String): Mejora {
        return Neo4JTransactionRunner().runTrx() { session ->
            val query = "MATCH (claseSuperior: Clase)-[mejora: Habilita]->(claseInferior: Clase) " +
                    "WHERE claseSuperior.nombre = ${'$'}nombreClSuperior " +
                    "AND claseInferior.nombre = ${'$'}nombreClInferior " +
                    "RETURN claseSuperior, mejora, claseInferior "
            val result = session.run(query, Values.parameters("nombreClSuperior", nombreClaseActual, "nombreClInferior", nombreClaseAMejorar))

            val record = result.single()
            val mejora = cosntructorMejoraReccord(record[0], record[2], record[1])
            mejora
        }
    }

    override fun mejorasParaClases(clases: List<String>): Set<Mejora> {
        var stringDeClases: String = stringDeClases(clases)

        return Neo4JTransactionRunner().runTrx() { session ->
            val query = "MATCH (claseSuperior: Clase)-[mejora: Habilita]->(claseInferior: Clase) " +
                    "WHERE claseSuperior.nombre IN [ " + stringDeClases +
                    " ] AND NOT claseInferior.nombre IN [ " + stringDeClases +
                    " ] RETURN claseSuperior, mejora, claseInferior "
            val result = session.run(query, Values.parameters())

            var lista = result.list {
                    record: Record ->

                val mejora = cosntructorMejoraReccord(record[0], record[2], record[1])
                mejora
            }
            lista.toSet()
        }
    }

    override fun clear() {
        return Neo4JTransactionRunner().runTrx() { session ->
            session.run("MATCH (n) DETACH DELETE n")
        }
    }

    private fun stringDeClases(clases: List<String>) : String {
        var clasesListStr: String = ""
        clasesListStr += "\'" + clases.first() + "\'"
        if (clases.size > 1) {
            clases.subList(1, clases.size).forEach { clasesListStr += ", \'" + it + "\'" }
        }
        return clasesListStr
    }

    private fun cosntructorMejoraReccord(nodoInicial: Value, nodoFinal: Value, relacion: Value): Mejora {
        val clasePrevia = Clase(nodoInicial["nombre"].asString())
        val clasePosterior = Clase(nodoFinal["nombre"].asString())

        val listAtributos = mutableListOf<Atributo>()
        var cantidadAtributos: Int = 0

        if (relacion["FUERZA"].asInt() > 0) {
            listAtributos.add(Atributo.FUERZA)
            cantidadAtributos = relacion["FUERZA"].asInt()}
        if (relacion["DESTREZA"].asInt() > 0) {
            listAtributos.add(Atributo.DESTREZA)
            cantidadAtributos = relacion["DESTREZA"].asInt()}
        if (relacion["CONSTITUCION"].asInt() > 0) {
            listAtributos.add(Atributo.CONSTITUCION)
            cantidadAtributos = relacion["CONSTITUCION"].asInt()}
        if (relacion["INTELIGENCIA"].asInt() > 0) {
            listAtributos.add(Atributo.INTELIGENCIA)
            cantidadAtributos = relacion["INTELIGENCIA"].asInt()}

        return Mejora(clasePrevia, clasePosterior, listAtributos, cantidadAtributos)
    }


}