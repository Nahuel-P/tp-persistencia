package ar.edu.unq.epers.tactics.persistencia.dao

import ar.edu.unq.epers.tactics.modelo.Pelea
import ar.edu.unq.epers.tactics.service.PeleasPaginadas

interface PeleaDAO {
    fun crear(pelea:Pelea) : Pelea
    fun actualizar(pelea: Pelea): Pelea
    fun recuperar(idDeLaPelea: Long): Pelea
    fun recuperarPeleasOrdenadas(partyId: Long, pagina: Int?): PeleasPaginadas
}