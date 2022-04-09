package ar.edu.unq.epers.tactics.persistencia.dao.hibernate

import  ar.edu.unq.epers.tactics.service.runner.HibernateTransactionRunner

open class HibernateDAO<T>(private val entityType: Class<T>) {

     open fun crear(entity:T):T{
        val session = HibernateTransactionRunner.currentSession
        session.save(entity)
         return entity
    }

     open fun actualizar(entity: T): T {
        val session = HibernateTransactionRunner.currentSession
        session.update(entity)
        return entity
    }

     open fun recuperar(id: Long): T {
        val session = HibernateTransactionRunner.currentSession
        return session.get(entityType, id) ?: throw RuntimeException("No existe entidad con id provisto")
    }

    fun eliminarTodo(){
        HibernateTransactionRunner.runTrx {
            HibernateDataDAO().clear()
        }
    }
}