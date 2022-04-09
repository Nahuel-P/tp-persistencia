package ar.edu.unq.epers.tactics.persistencia.dao.firebase

import ar.edu.unq.epers.tactics.modelo.Cofre

class CofreDAO : ItemDAO() {

    fun crearCofre(cofre: Cofre): Cofre{
        val documentID = cofre.nombre
        val docData: MutableMap<String, Any> = HashMap()
        docData["nombre"] = cofre.nombre
        docData["items"] = cofre.items
        db.collection("Cofres").document(documentID.toString()).set(docData).get()
        return cofre
    }

    fun crearMultiplesCofres(cofres: MutableList<Cofre>): MutableList<Cofre>{
        db.runTransaction {
            cofres.forEach {
                this.crearCofre(it)
            }
        }.get()

        return cofres
    }


}