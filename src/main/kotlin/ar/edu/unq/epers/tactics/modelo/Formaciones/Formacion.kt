package ar.edu.unq.epers.tactics.modelo.Formaciones

import org.bson.codecs.pojo.annotations.BsonProperty


class Formacion {
    @BsonProperty("id")
    val id: String? = null
    var nombre: String = "";
    var requisitos: List<Requerimiento> = listOf()
    var atributosDeFormacion: List<AtributoDeFormacion> = listOf()

    //Se necesita un constructor vacio para que jackson pueda
    //convertir de JSON a este objeto.
    protected constructor() {
    }

    constructor(nombre: String, requisitos: List<Requerimiento>, atributosDeFormacion: List<AtributoDeFormacion>){
        this.nombre= nombre;
        this.requisitos = requisitos;
        this.atributosDeFormacion = atributosDeFormacion;
    }
}