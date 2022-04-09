package ar.edu.unq.epers.tactics.service.impl

import ar.edu.unq.epers.tactics.modelo.Formaciones.AtributoDeFormacion
import ar.edu.unq.epers.tactics.modelo.Formaciones.Formacion
import ar.edu.unq.epers.tactics.modelo.Formaciones.Requerimiento
import ar.edu.unq.epers.tactics.persistencia.dao.PartyDAO
import ar.edu.unq.epers.tactics.persistencia.dao.mongodb.FormacionDAO
import ar.edu.unq.epers.tactics.service.FormacionService
import ar.edu.unq.epers.tactics.service.runner.HibernateTransactionRunner

class FormacionServiceImpl(val formacionDAO: FormacionDAO, val partyDAO: PartyDAO) : FormacionService {
    override fun crearFormacion(
        nombreFormacion: String,
        requerimientos: List<Requerimiento>,
        stats: List<AtributoDeFormacion>
    ): Formacion {
        var formacion = Formacion(nombreFormacion,requerimientos,stats)
        formacionDAO.crearFormacion(formacion)
        return formacion
    }

    override fun todasLasFormaciones(): List<Formacion> {
        return formacionDAO.obtenerTodos()
    }

    override fun atributosQueCorresponden(partyId: Long): List<AtributoDeFormacion> {
        return HibernateTransactionRunner.runTrx {
            partyDAO.resultadoDeEjecutarCon(partyId) {
                formacionDAO.atributosQueCorresponden(it.clasesDeSusPersonajes())
            }
        }
    }

    override fun formacionesQuePosee(partyId: Long): List<Formacion> {
        var party =  HibernateTransactionRunner.runTrx {
             partyDAO.recuperar(partyId)
        }
        return formacionDAO.formacionesQuePosee(party)
    }
}