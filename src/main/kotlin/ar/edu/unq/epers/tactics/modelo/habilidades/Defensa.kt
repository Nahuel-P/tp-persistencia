package ar.edu.unq.epers.tactics.modelo.habilidades

import ar.edu.unq.epers.tactics.modelo.Aventurero
import javax.persistence.Entity
import javax.persistence.PrimaryKeyJoinColumn

@Entity
@PrimaryKeyJoinColumn(name="id")
class Defensa(aventureroEmisor: Aventurero, aventureroReceptor: Aventurero): Habilidad(aventureroEmisor,aventureroReceptor) {

    override fun conValores(emisor: Aventurero, receptor: Aventurero) : Habilidad {
        this.defiende=true
        return this
    }

    override fun resolverHabilidadContra(receptor: Aventurero, emisor: Aventurero) {
        receptor.recibirDefensor(this.emisor)
        this.ejecutada=true
        this.acierta=true
    }
}