package ar.edu.unq.epers.tactics.service.impl

import ar.edu.unq.epers.tactics.modelo.Atributo
import ar.edu.unq.epers.tactics.modelo.Aventurero
import ar.edu.unq.epers.tactics.modelo.Clase
import ar.edu.unq.epers.tactics.modelo.Mejora
import ar.edu.unq.epers.tactics.persistencia.dao.AventureroDAO
import ar.edu.unq.epers.tactics.persistencia.dao.ClaseDAO
import ar.edu.unq.epers.tactics.persistencia.dao.PartyDAO
import ar.edu.unq.epers.tactics.persistencia.dao.hibernate.HibernatePartyDAO
import ar.edu.unq.epers.tactics.service.AventureroService
import ar.edu.unq.epers.tactics.service.ClaseService
import ar.edu.unq.epers.tactics.service.runner.HibernateTransactionRunner

class ClaseServiceImpl(private val claseDAO: ClaseDAO, val aventureroDAO: AventureroDAO) : ClaseService {

    private var partyDAO: PartyDAO = HibernatePartyDAO()
    private var aventureroService: AventureroService = AventureroServiceImpl(aventureroDAO, partyDAO)


    override fun crearClase(nombreDeLaClase: String): Clase {
        val clase = Clase(nombreDeLaClase)
        return claseDAO.crear(clase)
    }

    override fun crearMejora(nombreClaseInicio: String, nombreClaseAMejorar: String, atributos: MutableList<Atributo>, valorAAumentar: Int): Mejora {
        var mejora = Mejora( Clase(nombreClaseInicio), Clase(nombreClaseAMejorar), atributos, valorAAumentar)
        return claseDAO.crearMejora(mejora)
    }

    override fun requerir(claseSiguiente: String, claseAnterior: String) {
        claseDAO.requerir(claseSiguiente, claseAnterior)
    }

    override fun requeridasDe(clase: Clase): List<Clase> {
        return  claseDAO.requeridasDe(clase)
    }

    override fun puedeMejorar(aventureroID: Long, mejora: Mejora): Boolean {
        var puedeMejorarse: Boolean = HibernateTransactionRunner.runTrx {
            var aventurero = aventureroDAO.recuperar(aventureroID)
            var clasesDeAventurero = aventurero.clases.map { it.nombre }
            (aventurero.experiencia() >= 1) && claseDAO.puedeMejorarse(mejora.clasePosterior, clasesDeAventurero)
        }
        return puedeMejorarse
    }

    override fun ganarProficiencia(aventureroId: Long, nombreClaseActual: String, nombreClaseAMejorar: String): Aventurero {
        var mejora = claseDAO.mejoraEntreClases(nombreClaseActual, nombreClaseAMejorar)
        if (!this.puedeMejorar(aventureroId, mejora)) {
            throw RuntimeException("No es posible realizar la mejora ")
        }
        return HibernateTransactionRunner.runTrx {
            var aventurero = aventureroDAO.recuperar(aventureroId)
            aventurero.ganarProficiencia(mejora)
            aventureroDAO.actualizar(aventurero)
        }
    }

    override fun posiblesMejoras(aventureroID: Long): Set<Mejora> {
        var aventurero = HibernateTransactionRunner.runTrx { aventureroDAO.recuperar(aventureroID) }
        if (aventurero.experiencia() == 0){
            return emptySet()
        }
        else{
            var clases = aventurero.clases.map { it.nombre }
            var mejoras = claseDAO.mejorasParaClases(clases)
            return mejoras
        }
    }

    override fun clear() {
        claseDAO.clear()
    }


}