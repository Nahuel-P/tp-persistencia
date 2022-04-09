package ar.edu.unq.epers.tactics.modelo.habilidades

import ar.edu.unq.epers.tactics.modelo.Aventurero
import javax.persistence.Entity
import javax.persistence.PrimaryKeyJoinColumn

@Entity
@PrimaryKeyJoinColumn(name="id")
class Meditar(aventureroEmisor: Aventurero, aventureroReceptor: Aventurero): Habilidad(aventureroEmisor,aventureroReceptor) {

    override fun conValores(emisor: Aventurero, receptor: Aventurero) : Habilidad {
        this.meditacion=this.calcularMeditacion(emisor.nivel())
        return this
    }

    override fun resolverHabilidadContra(receptor: Aventurero, emisor: Aventurero) {
        receptor.meditar(this.meditacion)
        this.ejecutada=true
        this.acierta=true
    }

    private fun calcularMeditacion(nivel: Int) : Int {
        var mediacion = nivel
        if(this.receptor.manaBase()<=mediacion){
            mediacion = this.receptor.manaBase() - this.receptor.manaActual()
        }else{
            if (this.receptor.manaActual() + mediacion > this.receptor.vidaBase())
                mediacion = this.receptor.manaBase() - this.receptor.manaActual()
        }
        return mediacion
    }

}