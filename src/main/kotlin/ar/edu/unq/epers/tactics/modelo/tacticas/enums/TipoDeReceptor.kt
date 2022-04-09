package ar.edu.unq.epers.tactics.modelo.tacticas.enums

import ar.edu.unq.epers.tactics.modelo.Aventurero

enum class TipoDeReceptor {
    ALIADO {
        override fun verificar(emisor: Aventurero, receptor: Aventurero) = emisor.esAliadoDe(receptor)
    },
    ENEMIGO {
        override fun verificar(emisor: Aventurero, receptor: Aventurero) = emisor.esEnemigoDe(receptor)
    },
    UNO_MISMO {
        override fun verificar(emisor: Aventurero, receptor: Aventurero) = emisor == receptor
    };

    abstract fun verificar(emisor: Aventurero, receptor: Aventurero): Boolean
}