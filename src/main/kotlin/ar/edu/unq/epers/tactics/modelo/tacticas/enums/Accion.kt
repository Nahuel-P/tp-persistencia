package ar.edu.unq.epers.tactics.modelo.tacticas.enums

import ar.edu.unq.epers.tactics.modelo.Aventurero
import ar.edu.unq.epers.tactics.modelo.habilidades.*

enum class Accion{

    ATAQUE_FISICO{
                    override fun generarHabilidad(emisor: Aventurero, receptor: Aventurero): Habilidad {
                        var  ataqueFisico = AtaqueFisico(emisor, receptor).conValores(emisor, receptor)
                        //ataqueFisico.conValores(emisor, receptor)
                        return ataqueFisico
                    }
                 },
    //---------------------------------------------------//
    DEFENDER    {
                    override fun generarHabilidad(emisor: Aventurero, receptor: Aventurero): Habilidad {
                        var  defensa = Defensa(emisor,receptor).conValores(emisor,receptor)
                        return defensa
                    }
                },
    //---------------------------------------------------//
    CURAR       {
                    override fun generarHabilidad(emisor: Aventurero, receptor: Aventurero): Habilidad {
                        var curacion = Curar(emisor,receptor).conValores(emisor,receptor)
                        return curacion
                    }
                },
    //---------------------------------------------------//
    ATAQUE_MAGICO{
                    override fun generarHabilidad(emisor: Aventurero, receptor: Aventurero): Habilidad {
                        var ataqueMagico = AtaqueMagico(emisor,receptor).conValores(emisor,receptor)
                        return ataqueMagico
                    }
                 },
    //---------------------------------------------------//
    MEDITAR     {
                    override fun generarHabilidad(emisor: Aventurero, receptor: Aventurero): Habilidad {
                        var meditacion = Meditar(emisor,receptor).conValores(emisor,receptor)
                        return meditacion
                     }
                 };
    //---------------------------------------------------//
    abstract fun generarHabilidad(emisor: Aventurero, receptor: Aventurero) : Habilidad
}