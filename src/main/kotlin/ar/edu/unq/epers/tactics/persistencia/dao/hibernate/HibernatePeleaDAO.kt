package ar.edu.unq.epers.tactics.persistencia.dao.hibernate

import ar.edu.unq.epers.tactics.modelo.Pelea
import ar.edu.unq.epers.tactics.persistencia.dao.PeleaDAO
import ar.edu.unq.epers.tactics.service.PeleasPaginadas
import ar.edu.unq.epers.tactics.service.runner.HibernateTransactionRunner
import javassist.NotFoundException
import kotlin.math.ceil

class HibernatePeleaDAO: HibernateDAO<Pelea>(Pelea::class.java), PeleaDAO {

    override fun recuperarPeleasOrdenadas(partyId: Long, pagina: Int?): PeleasPaginadas{
        val session = HibernateTransactionRunner.currentSession
        val consulta =
            "SELECT pelea FROM Pelea pelea WHERE $partyId = partyA OR $partyId = partyB ORDER BY id ASC"
        val query = session.createQuery(consulta, Pelea::class.java)
        val peleas = query.list() as List<Pelea>
        query.firstResult = pagina!! * 10
        query.maxResults = 10
        val ultimaPagina = ceil((peleas.size / 10).toDouble()).toInt()
        val peleaEnPaginaBuscada = query.list() as List<Pelea>

        if(peleas.isNotEmpty() && pagina in (0..ultimaPagina)){
            return PeleasPaginadas(peleaEnPaginaBuscada, peleas.size)
        }
        else if (peleas.isEmpty()){
            throw NotFoundException("El número de party $partyId no tiene peleas o no existe")
        }
        else{
            throw NotFoundException("El número de pagina $pagina no existe")
        }
    }

}
