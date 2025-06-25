package com.cantinaChatBOT;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class ItemPedidoTest {

    private ItemPedido itemPedido;

    @BeforeEach
    void setUp(){
        itemPedido = new ItemPedido("Cafézinho", 2, 2.50);
    }

    //Reseta os valores para evitar erros
    @AfterEach
    void tearDown(){
        itemPedido = null;
    }

    //Verifica se o toString esta sendo gerado corretamente
    @Test
    void testToStringParaMostrarProdutos (){
        assertEquals("2 x Cafézinho - R$ 2,50 (Subtotal: R$ 5,00)", itemPedido.toString());
    }

    //Testa os getters para ter cobertura de 100%
    @Test
    void testGetters() {
        assertEquals("Cafézinho", itemPedido.getProduto());
        assertEquals(2, itemPedido.getQuantidade());
        assertEquals(5.0, itemPedido.getSubtotal());
    }
}
