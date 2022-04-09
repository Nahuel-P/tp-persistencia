package ar.edu.unq.epers.tactics.service

import ar.edu.unq.epers.tactics.modelo.Clase
import ar.edu.unq.epers.tactics.modelo.Cofre
import ar.edu.unq.epers.tactics.modelo.Item

interface CofreService {
    fun crearCofre(cofre: Cofre): Cofre
    fun crearMultiplesCofres(cofres: MutableList<Cofre>):MutableList<Cofre>
}