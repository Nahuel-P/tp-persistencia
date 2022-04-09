package ar.edu.unq.epers.tactics.service.impl

import ar.edu.unq.epers.tactics.modelo.Cofre
import ar.edu.unq.epers.tactics.persistencia.dao.AventureroDAO
import ar.edu.unq.epers.tactics.persistencia.dao.firebase.CofreDAO
import ar.edu.unq.epers.tactics.service.CofreService

class CofreServiceImpl(private val itemDAO: CofreDAO, val aventureroDAO: AventureroDAO)  : CofreService {
    override fun crearCofre(cofre: Cofre): Cofre {
        return itemDAO.crearCofre(cofre)
    }

    override fun crearMultiplesCofres(cofres: MutableList<Cofre>): MutableList<Cofre> {
        return itemDAO.crearMultiplesCofres(cofres)
    }
}