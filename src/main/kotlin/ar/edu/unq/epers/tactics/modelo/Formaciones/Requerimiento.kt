package ar.edu.unq.epers.tactics.modelo.Formaciones

import ar.edu.unq.epers.tactics.modelo.Clase
import org.bson.codecs.pojo.annotations.BsonIgnore

class Requerimiento {
    //val id: String? = null
    private lateinit var clase: Clase;
    var cantidad: Int = 0
    var nombreClase: String = ""

    @BsonIgnore
    var formacion: Formacion? = null

    //Se necesita un constructor vacio para que jackson pueda
    //convertir de JSON a este objeto.
    protected constructor() {
    }
    constructor(clase: Clase, cantidad: Int){
        this.clase=clase;
        this.cantidad= cantidad;
        this.nombreClase = clase.nombre
    }

}