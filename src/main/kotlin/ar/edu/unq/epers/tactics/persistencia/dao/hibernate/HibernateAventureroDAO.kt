package ar.edu.unq.epers.tactics.persistencia.dao.hibernate

import ar.edu.unq.epers.tactics.modelo.Aventurero
import ar.edu.unq.epers.tactics.persistencia.dao.AventureroDAO
import ar.edu.unq.epers.tactics.service.runner.HibernateTransactionRunner

class HibernateAventureroDAO: HibernateDAO<Aventurero>(Aventurero::class.java),AventureroDAO {

    override fun eliminar(aventurero: Aventurero) {
        val session = HibernateTransactionRunner.currentSession
        session.delete(aventurero)
    }

    override fun mejorGuerrero(): Aventurero {
        val session = HibernateTransactionRunner.currentSession
        val hql =   "SELECT * FROM\n" +
                "(SELECT a.id,SUM(h.dañoFisico) as damage FROM Aventurero a JOIN Habilidad h ON a.id=h.emisor_id\n" +
                "WHERE h.ejecutada = true\n" +
                "GROUP BY a.id\n" +
                "order by damage DESC\n" +
                "LIMIT 1) AS sq1 JOIN Aventurero ON sq1.id = Aventurero.id"

        return session.createNativeQuery(hql, Aventurero::class.java).singleResult as Aventurero
    }

    override fun mejorMago(): Aventurero {
        val session = HibernateTransactionRunner.currentSession
        val hql =   "SELECT * FROM\n" +
                    "(SELECT a.id,SUM(h.dañoMagico) as damage FROM Aventurero a JOIN Habilidad h ON a.id=h.emisor_id\n" +
                    "WHERE h.ejecutada = true\n" +
                    "GROUP BY a.id\n" +
                    "order by damage DESC\n" +
                    "LIMIT 1) AS sq1 JOIN Aventurero ON sq1.id = Aventurero.id"

        return session.createNativeQuery(hql, Aventurero::class.java).singleResult as Aventurero
    }

    override fun mejorCurandero(): Aventurero {
        val session = HibernateTransactionRunner.currentSession
        val hql =   "SELECT * FROM\n" +
                    "(SELECT a.id,SUM(h.curacion) as healing FROM Aventurero a JOIN Habilidad h ON a.id=h.emisor_id\n" +
                    "WHERE h.ejecutada = true\n" +
                    "GROUP BY a.id\n" +
                    "order by healing DESC\n" +
                    "LIMIT 1) AS sq1 JOIN Aventurero ON sq1.id = Aventurero.id"

        return session.createNativeQuery(hql, Aventurero::class.java).singleResult as Aventurero
    }

    override fun buda(): Aventurero {
        val session = HibernateTransactionRunner.currentSession
        val hql =   "SELECT * FROM\n" +
                    "(SELECT a.id,COUNT(h.meditacion) as meditaciones FROM Aventurero a JOIN Habilidad h ON a.id=h.emisor_id\n" +
                    "WHERE h.ejecutada = true\n" +
                    "GROUP BY a.id\n" +
                    "order by meditaciones DESC\n" +
                    "LIMIT 1) AS sq1 JOIN Aventurero ON sq1.id = Aventurero.id"

        return session.createNativeQuery(hql, Aventurero::class.java).singleResult as Aventurero
    }
}