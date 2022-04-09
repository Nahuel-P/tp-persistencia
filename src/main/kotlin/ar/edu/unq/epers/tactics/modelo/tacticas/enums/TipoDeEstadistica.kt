package ar.edu.unq.epers.tactics.modelo.tacticas.enums

import ar.edu.unq.epers.tactics.modelo.Aventurero

enum class TipoDeEstadistica {
    VIDA  {
        override fun valorDeEstadistica(aventurero: Aventurero) = aventurero.vidaActual()
          },

    ARMADURA {
        override fun valorDeEstadistica(aventurero: Aventurero) = aventurero.armadura()
             },
    MANA {
        override fun valorDeEstadistica(aventurero: Aventurero) = aventurero.manaActual()
         },

    VELOCIDAD {
        override fun valorDeEstadistica(aventurero: Aventurero) = aventurero.velocidad()
              },

    DAÑO_FISICO {
        override fun valorDeEstadistica(aventurero: Aventurero) = aventurero.dañoFisico()
                },

    DAÑO_MAGICO {
        override fun valorDeEstadistica(aventurero: Aventurero) = aventurero.poderMagico()
                },

    PRECISION_FISICA {
        override fun valorDeEstadistica(aventurero: Aventurero) = aventurero.precisionFisica()
    };

    abstract fun valorDeEstadistica(aventurero: Aventurero): Int
}