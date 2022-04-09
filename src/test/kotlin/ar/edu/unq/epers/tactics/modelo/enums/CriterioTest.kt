package ar.edu.unq.epers.tactics.modelo.enums

import ar.edu.unq.epers.tactics.modelo.tacticas.enums.Criterio
import org.junit.Assert
import org.junit.jupiter.api.Test

class CriterioTest {

    @Test
    fun cuandoEsMayorQueOtro(){
        Assert.assertTrue(Criterio.MAYOR_QUE.compararCriterios(7,5))
    }

    @Test
    fun cuandoNoEsMayorQueOtro(){
        Assert.assertFalse(Criterio.MAYOR_QUE.compararCriterios(7,9))
    }

    @Test
    fun cuandoEsIgualQueOtro(){
        Assert.assertTrue(Criterio.IGUAL.compararCriterios(7,7))
    }

    @Test
    fun cuandoNoEsIgualQueOtro(){
        Assert.assertFalse(Criterio.IGUAL.compararCriterios(7,9))
    }

    @Test
    fun cuandoEsMenorQueOtro(){
        Assert.assertTrue(Criterio.MENOR_QUE.compararCriterios(7,9))
    }

    @Test
    fun cuandoNoEsMenorQueOtro(){
        Assert.assertFalse(Criterio.MENOR_QUE.compararCriterios(7,5))
    }


}