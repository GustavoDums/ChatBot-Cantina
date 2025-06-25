package com.cantinaChatBOT;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class ClienteTest {

    //Teste para ver se o codigo do cliente ta sendo gerado corretamente
    @Test
    void testCodigoGerado() {
        Cliente cliente1 = new Cliente();
        Cliente cliente2 = new Cliente();

        assertTrue(cliente1.getCodigo() > 0, "Código do cliente deve ser maior que zero");
        assertTrue(cliente2.getCodigo() > 0, "Código do cliente deve ser maior que zero");

        //Para confirmar que o codigo dos dois clientes não é igual
        assertNotEquals(cliente1.getCodigo(), cliente2.getCodigo(), "Cada cliente deve ter código único");
    }

    //Teste pra ver se o toString ta sendo gerado da forma correta
    @Test
    void testToString() {
        Cliente cliente = new Cliente();
        String expected = "Cliente #" + cliente.getCodigo();
        assertEquals(expected, cliente.toString());
    }

    // Testa getter para ter cobertura de 100%
    @Test
    void testGetterComValorEspecifico() {

        CodigoClienteGenerator geradorFake = () -> 42; //Injeta um gerador fake

        Cliente cliente = new Cliente(geradorFake);

        //Ve se o valor do codigo é o mesmo que foi injetado
        assertEquals(42, cliente.getCodigo(), "O código deve ser o valor fornecido pelo gerador fake");
    }
}
