package jm.model;

import org.junit.Assert;
import org.junit.Test;

public class BeanExemploTest {

    @Test
    public void testToString() {
        final BeanExemplo be = new BeanExemplo(1L, "Exemplo 1", "Observação de exemplo");

        Assert.assertEquals("BeanExemplo[numero = 1, descricao = Exemplo 1]", be.toString());
    }

}
