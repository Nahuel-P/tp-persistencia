package ar.edu.unq.epers.tactics.persistencia.dao.firebase

import ar.edu.unq.epers.tactics.modelo.Atributo
import ar.edu.unq.epers.tactics.modelo.Item
import ar.edu.unq.epers.tactics.modelo.Party
import ar.edu.unq.epers.tactics.service.runner.FirebaseInitializer
import com.google.api.core.ApiFuture
import com.google.cloud.firestore.*
import com.google.firebase.cloud.FirestoreClient

open class ItemDAO() {
    var db : Firestore

    init{
        FirebaseInitializer.initialize()
        db = FirestoreClient.getFirestore()
    }

    fun crearItem(item: Item): Item{
        val documentID = item.nombre
        val docData: MutableMap<String, Any> = HashMap()

        //docData["id"]= item.id.toString()
        docData["nombre"] = item.nombre
        docData["atributo"] = item.atributo.toString()
        docData["cantidadDeAtributo"] = item.cantidadDeAtributo
        docData["nivelRequerido"] = item.nivelRequerido
        docData["precio"] = item.precio
        db.collection("Items").document(documentID.toString()).set(docData).get()
        return item

    }

    fun eliminar(nombreDeItem: String) {
        var document =  db.collection("Items").document(nombreDeItem.toString())
        document.delete()
    }

    fun actualizar(nombreDeItem: String, atributo: Atributo, cantidadDeAtributos: Int, nivelRequerido: Int, precio: Int) {
        var document =  db.collection("Items").document(nombreDeItem.toString())
        document.update("atributo",atributo)
        document.update("cantidadDeAtributo",cantidadDeAtributos)
        document.update("nivelRequerido",nivelRequerido)
        document.update("precio",precio)

    }

    fun buscarItem(nombre: String) : Item? {
        //var itemEncontrado : Item? = null
        var atributo : Atributo? = null
        var nivelInt : Int
        var precioInt : Int
        var cantAtributoInt : Int

        val items: CollectionReference = db.collection("Items")
        val query: Query = items.whereEqualTo("nombre", nombre)
        val querySnapshot: ApiFuture<QuerySnapshot> = query.get()

        for (document in querySnapshot.get().documents) {
            var nombreItem = document.id

            when(document.get("atributo")) {
                "INTELIGENCIA" -> {  atributo= Atributo.INTELIGENCIA }
                "DESTREZA" -> { atributo = Atributo.DESTREZA }
                "FUERZA" -> { atributo = Atributo.FUERZA}
                "CONSTITUCION" -> { atributo = Atributo.CONSTITUCION}
            }
            var cantidadDeAtributos =document.get("cantidadDeAtributo").toString()
            var nivel =document.get("nivelRequerido").toString()
            var precio = document.get("precio").toString()

            var nivelInt = nivel.toIntOrNull()
            var precioInt = precio.toIntOrNull()
            var cantAtributoInt = cantidadDeAtributos.toIntOrNull()

            var itemEncontrado2 : Item = Item(nombreItem, atributo!!,cantAtributoInt!!,nivelInt!!,precioInt!!)

            if(itemEncontrado2==null){
                throw RuntimeException("El item no existe")
            }else{
                return itemEncontrado2
            }
        }
        return throw RuntimeException("El item no existe")

    }

    fun elinarTodos() {
        var documents = db.collection("Items").listDocuments()
        for(doc in documents){
            doc.delete()
        }
    }


//    fun todosLosItems(idItem: Long): DocumentReference {
//        var doc = db.collection("Items").document(idItem.toString())
//        return doc
//    }

//    private fun documentoReferenceToItem(document : DocumentReference) : Item{
//        var item = Item(document.)
//    }
}