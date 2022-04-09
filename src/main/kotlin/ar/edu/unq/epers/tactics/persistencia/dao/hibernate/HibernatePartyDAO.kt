package ar.edu.unq.epers.tactics.persistencia.dao.hibernate

import ar.edu.unq.epers.tactics.modelo.Party
import ar.edu.unq.epers.tactics.persistencia.dao.PartyDAO
import ar.edu.unq.epers.tactics.service.Direccion
import ar.edu.unq.epers.tactics.service.Orden
import ar.edu.unq.epers.tactics.service.PartyPaginadas
import ar.edu.unq.epers.tactics.service.runner.HibernateTransactionRunner
import javassist.NotFoundException
import kotlin.math.ceil


class HibernatePartyDAO : HibernateDAO<Party>(Party::class.java), PartyDAO {

    override fun recuperarTodas(): List<Party> {
        val session = HibernateTransactionRunner.currentSession
        return session.createQuery("SELECT p FROM Party p ORDER BY p.nombre ASC", Party::class.java).resultList
    }

    override fun recuperarOrdenadas(orden: Orden, direccion: Direccion, pagina: Int?): PartyPaginadas {
        val session = HibernateTransactionRunner.currentSession
        val enumorden = when (orden) {
            Orden.PODER -> "case when SUM(a.poderTotal) is not NULL then SUM(a.poderTotal) else 0 end "
            Orden.VICTORIAS -> "COUNT(p.partyGanadora_id) "
            Orden.DERROTAS -> "COUNT(p2.partyGanadora_id) "
        }
        val enumdir = when (direccion) {
            Direccion.ASCENDENTE -> "asc"
            Direccion.DESCENDENTE -> "desc"
        }

        val consulta = "SELECT party FROM Party party  " +
                        "LEFT JOIN Aventurero a ON party.id = a.party " +
                        "LEFT JOIN Pelea p ON party.id = p.partyGanadora " +
                        "LEFT JOIN Pelea p2 ON (party.id = p2.partyA OR " +
                        "party.id = p2.partyB) AND party.id <> p2.partyGanadora " +
                        "GROUP BY party.id " +
                        "ORDER BY $enumorden $enumdir "
        val query = session.createQuery(consulta, Party::class.java)
        val partiesTotal = query.list() as List<Party>
        query.firstResult = pagina!! * 10
        query.maxResults = 10
        val ultimaPagina = ceil((partiesTotal.size / 10).toDouble()).toInt()
        val partiesEnPaginaBuscada = query.list() as List<Party>

        if (pagina in (0..ultimaPagina)) {
            return PartyPaginadas(partiesEnPaginaBuscada, partiesTotal.size)
        } else {
            throw NotFoundException("El número de página $pagina no existe")
        }
    }
}