package ar.edu.unq.epers.tactics.service.impl

import ar.edu.unq.epers.tactics.modelo.Aventurero
import ar.edu.unq.epers.tactics.modelo.Pelea
import ar.edu.unq.epers.tactics.modelo.habilidades.Habilidad
import ar.edu.unq.epers.tactics.persistencia.dao.AventureroDAO
import ar.edu.unq.epers.tactics.persistencia.dao.PartyDAO
import ar.edu.unq.epers.tactics.persistencia.dao.PeleaDAO
import ar.edu.unq.epers.tactics.service.PeleaService
import ar.edu.unq.epers.tactics.service.PeleasPaginadas
import ar.edu.unq.epers.tactics.service.runner.HibernateTransactionRunner.runTrx

class PeleaServiceImpl(val peleaDAO: PeleaDAO, val partyDAO: PartyDAO, val aventureroDAO: AventureroDAO): PeleaService {

    override fun iniciarPelea(idPartyA: Long, idPartyB: Long): Pelea {
        return runTrx {
            var partyA = partyDAO.recuperar(idPartyA)
            var partyB = partyDAO.recuperar(idPartyB)
            partyA.entrarEnPelea()
            partyB.entrarEnPelea()
            partyDAO.actualizar(partyA)
            partyDAO.actualizar(partyB)
            var pelea = Pelea(partyA,partyB)
            peleaDAO.crear(pelea)
            pelea
        }
    }

    override fun estaEnPelea(partyId: Long): Boolean =
        runTrx {
            val enPelea = partyDAO.recuperar(partyId).estaEnPelea()
            enPelea
        }

    override fun resolverTurno(idPelea:Long, idAventurero:Long) : Habilidad =
        runTrx {
            val aventurero = aventureroDAO.recuperar(idAventurero)
            val pelea = peleaDAO.recuperar(idPelea)
            var enemigos : MutableList<Aventurero> = mutableListOf()
            if(aventurero.party()?.id() == pelea.partyA()!!.id()){
                enemigos.addAll(pelea.partyB()!!.aventureros())
            }else{
                enemigos.addAll(pelea.partyA()!!.aventureros())
            }
            val habilidadUtilizada = aventurero.resolverTurno(enemigos)
            //this.recibirHabilidad(idPelea, idAventurero, habilidadUtilizada)
            aventureroDAO.actualizar(aventurero)
            pelea.agregarHabilidad(habilidadUtilizada)
            peleaDAO.actualizar(pelea)
            habilidadUtilizada
        }

    override fun recibirHabilidad(idPelea:Long, idAventurero:Long, habilidad: Habilidad): Aventurero =
        runTrx {
            val pelea = peleaDAO.recuperar(idPelea)
            var aventurero = aventureroDAO.recuperar(idAventurero)
            var idEmisor = habilidad.emisor().id()
            var emisorRecuperado = aventureroDAO.recuperar(idEmisor!!)
            habilidad.resolverHabilidadContra(aventurero,emisorRecuperado)
            pelea.agregarHabilidad(habilidad)
            aventureroDAO.actualizar(aventurero)
            aventureroDAO.actualizar(emisorRecuperado)
            peleaDAO.actualizar(pelea)
            aventurero
        }

    override fun terminarPelea(idDeLaPelea: Long): Pelea =
        runTrx {
            val pelea = peleaDAO.recuperar(idDeLaPelea)
            pelea.finalizar()
            peleaDAO.actualizar(pelea)
            pelea
        }

    override fun recuperarPelea(idDeLaPelea :Long): Pelea{
        return runTrx { peleaDAO.recuperar(idDeLaPelea)}
    }

    override fun recuperarOrdenadas(partyId: Long, pagina: Int?): PeleasPaginadas {
        return runTrx { peleaDAO.recuperarPeleasOrdenadas(partyId, pagina) }
    }
}