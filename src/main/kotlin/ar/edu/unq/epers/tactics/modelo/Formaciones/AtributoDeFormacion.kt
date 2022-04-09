package ar.edu.unq.epers.tactics.modelo.Formaciones

import ar.edu.unq.epers.tactics.modelo.Atributo

class AtributoDeFormacion {
    val id: String? = null
    var cantidad : Int = 0;
    lateinit var atributo : Atributo;

    //Se necesita un constructor vacio para que jackson pueda
    //convertir de JSON a este objeto.
    protected constructor() {
    }

    constructor(cantidad : Int, atributo : Atributo){
        this.cantidad= cantidad;
        this.atributo = atributo
    }
}