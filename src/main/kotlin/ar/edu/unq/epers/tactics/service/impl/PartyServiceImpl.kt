package ar.edu.unq.epers.tactics.service.impl

import ar.edu.unq.epers.tactics.modelo.Aventurero
import ar.edu.unq.epers.tactics.modelo.Party
import ar.edu.unq.epers.tactics.persistencia.dao.PartyDAO
import ar.edu.unq.epers.tactics.service.Direccion
import ar.edu.unq.epers.tactics.service.Orden
import ar.edu.unq.epers.tactics.service.PartyPaginadas
import ar.edu.unq.epers.tactics.service.PartyService
import ar.edu.unq.epers.tactics.service.runner.HibernateTransactionRunner.runTrx

class PartyServiceImpl(val partyDAO: PartyDAO): PartyService {

    override fun crear(party: Party): Party {
        return runTrx { partyDAO.crear(party) }
    }

    override fun actualizar(party: Party): Party {
        return runTrx { partyDAO.actualizar(party) }
    }

    override fun recuperar(idDeLaParty: Long): Party {
        return runTrx { partyDAO.recuperar(idDeLaParty)}
    }

    override fun recuperarTodas(): List<Party> {
        return runTrx { partyDAO.recuperarTodas()}
    }

    override fun agregarAventureroAParty(idDeLaParty: Long, aventurero: Aventurero): Aventurero {
        return  runTrx {
            val party = partyDAO.recuperar(idDeLaParty)
            party.sumarAventurero(aventurero)
            partyDAO.actualizar(party)
            aventurero
        }
    }

    override fun eliminarTodo() {
        runTrx { partyDAO.eliminarTodo() }
    }

    override fun recuperarOrdenadas(orden: Orden, direccion: Direccion, pagina: Int?): PartyPaginadas {
        return runTrx { partyDAO.recuperarOrdenadas(orden, direccion, pagina) }
    }
}