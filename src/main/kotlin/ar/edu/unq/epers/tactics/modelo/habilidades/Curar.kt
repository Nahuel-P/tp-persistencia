package ar.edu.unq.epers.tactics.modelo.habilidades

import ar.edu.unq.epers.tactics.modelo.Aventurero
import javax.persistence.Entity
import javax.persistence.PrimaryKeyJoinColumn

@Entity
@PrimaryKeyJoinColumn(name="id")
class Curar(emisor : Aventurero, receptor : Aventurero) : Habilidad(receptor,emisor) {

    override fun conValores(emisor: Aventurero, receptor: Aventurero) : Habilidad {
        this.curacion= this.calcularSanacion(emisor.poderMagico())
        return this
    }

    override fun resolverHabilidadContra(receptor: Aventurero, emisor: Aventurero) {
        emisor.utilizarMana()
        receptor.curarCon(this.curacion)
        this.ejecutada=true
        this.acierta=true
    }

    private fun calcularSanacion(poderMagicoEmisor: Int) : Int {
        var curacionAplicada = poderMagicoEmisor
        if(this.receptor.vidaBase()<=poderMagicoEmisor){
            curacionAplicada = this.receptor.vidaBase() - this.receptor.vidaActual()
        }else{
            if (this.receptor.vidaActual() + poderMagicoEmisor > this.receptor.vidaBase())
                curacionAplicada = this.receptor.vidaBase() - this.receptor.vidaActual()
        }
        return curacionAplicada
    }
}