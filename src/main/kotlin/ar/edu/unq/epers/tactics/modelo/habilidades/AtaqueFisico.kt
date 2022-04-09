package ar.edu.unq.epers.tactics.modelo.habilidades

import ar.edu.unq.epers.tactics.modelo.Aventurero
import javax.persistence.Entity
import javax.persistence.PrimaryKeyJoinColumn

@Entity
@PrimaryKeyJoinColumn(name="id")
class AtaqueFisico(emisor : Aventurero, receptor : Aventurero) : Habilidad(emisor,receptor ) {

    override fun conValores(emisor: Aventurero, receptor: Aventurero): Habilidad {
        this.dañoFisico = this.calcularDaño(emisor.dañoFisico())
        return this
    }

   override fun resolverHabilidadContra(receptor: Aventurero, emisor: Aventurero) {
       if (this.acierta || ataqueExitoso(emisor.precisionFisica(),receptor.armadura(),receptor.velocidad())){
            this.ejecutada = true
            this.acierta = true
            receptor.recibirHabilidadDeDaño(this.dañoFisico)
            emisor.incrementarDañoInflingido(this.dañoFisico)
        }
    }

    private fun ataqueExitoso(presionEmisor : Int, armaduraReceptor : Int, velocidadReceptor : Int) : Boolean{
        return (1..20).random() + presionEmisor >= armaduraReceptor + (velocidadReceptor / 2)
    }

    private fun calcularDaño(dañoFisicoEmisor: Int) : Int {
        var dañoAplicado = dañoFisicoEmisor
        if(this.receptor.vidaActual()<=dañoFisicoEmisor){
            dañoAplicado = this.receptor.vidaActual()
        }
        return dañoAplicado
    }

}