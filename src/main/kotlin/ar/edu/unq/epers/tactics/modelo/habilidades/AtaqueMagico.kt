package ar.edu.unq.epers.tactics.modelo.habilidades

import ar.edu.unq.epers.tactics.modelo.Aventurero
import javax.persistence.Entity
import javax.persistence.PrimaryKeyJoinColumn

@Entity
@PrimaryKeyJoinColumn(name="id")
class AtaqueMagico(emisor : Aventurero, receptor : Aventurero) : Habilidad(receptor,emisor) {

    override fun conValores(emisor: Aventurero, receptor: Aventurero) : Habilidad {
        this.dañoMagico=this.calcularDaño(emisor.poderMagico())
        return this
    }

    override fun resolverHabilidadContra(receptor: Aventurero, emisor: Aventurero) {
        emisor.utilizarMana()
        if (this.acierta ||ataqueExitoso(receptor.velocidad(), receptor.nivel())){
            this.ejecutada=true
            this.acierta=true
            receptor.recibirHabilidadDeDaño(this.dañoMagico)
            emisor.incrementarDañoInflingido(this.dañoMagico)
        }
    }

    private fun ataqueExitoso(velocidadReceptor: Int, lvlReceptor: Int) : Boolean{
        return (1..20).random() + lvlReceptor >= (velocidadReceptor / 2)
    }

    private fun calcularDaño(dañoMagicoEmisor: Int) : Int {
        var dañoAplicado = dañoMagicoEmisor
        if(this.receptor.vidaActual()<=dañoMagicoEmisor){
            dañoAplicado = this.receptor.vidaActual()
        }
        return dañoAplicado
    }

}