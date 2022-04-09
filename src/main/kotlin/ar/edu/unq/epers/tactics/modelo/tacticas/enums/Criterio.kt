package ar.edu.unq.epers.tactics.modelo.tacticas.enums

enum class Criterio {
    IGUAL {
        override fun compararCriterios(criterioA: Int, criterioB: Int) = criterioA == criterioB
          },

    MAYOR_QUE {
        override fun compararCriterios(criterioA: Int, criterioB: Int) = criterioA > criterioB
    },

    MENOR_QUE {
        override fun compararCriterios(criterioA: Int, criterioB: Int) = criterioA < criterioB
    };

    abstract fun compararCriterios(criterioA: Int, criterioB: Int): Boolean
}