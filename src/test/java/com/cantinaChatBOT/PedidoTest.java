package com.cantinaChatBOT;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class PedidoTest {

    private Pedido pedido;

    @BeforeEach
    void setUp() {
        pedido = new Pedido(123);
    }

    @AfterEach
    void tearDown() {
        pedido = null;
    }

    //Teste para ver o estado inicial do pedido
    @Test
    void testInicializacaoPedido() {
        assertEquals(123, pedido.getCodigoCliente()); //O codigo tem que ser o mesmo que foi definido
        assertEquals("Em aberto", pedido.getStatus()); //O status tem que ser em aberto
        assertTrue(pedido.getItens().isEmpty()); //E a lista com os itens do pedido tem que estar vazia
        assertEquals(0.0, pedido.calcularTotal()); //E o total dos itens tambem tem que estar vazio
    }

    //Teste para ver se os itens estão sendo adicionados corretamente
    @Test
    void testAdicionarItem() {
        ItemPedido item = new ItemPedido("Café", 2, 3.0); //Cria um item
        pedido.adicionarItem(item); //Adiciona o item na lista pedidos

        List<ItemPedido> itens = pedido.getItens();
        assertEquals(1, itens.size()); //Ver se o tamanho da lista é de 1, pois so tem 1 item
        assertEquals("Café", itens.get(0).getProduto()); //Ver se o item que foi adicionado esta realmente na lista
        assertEquals(6.0, pedido.calcularTotal()); //Ver se o total calculado esta correto
    }

    //Teste para ver se o item esta sendo removido corretamente
    @Test
    void testRemoverItemPorIndice_Valido() {
        pedido.adicionarItem(new ItemPedido("Café", 2, 3.0)); //Adiciona dois itens
        pedido.adicionarItem(new ItemPedido("Pão", 1, 2.5));

        boolean removido = pedido.removerItemPorIndice(0); //Remove o primeiro item

        assertTrue(removido);
        assertEquals(1, pedido.getItens().size()); //Verificar o tamanho da lista
        assertEquals("Pão", pedido.getItens().get(0).getProduto()); //Verificar se o item contido é o correto, nesse caso o cafe
    }

    //Teste para remover o item, com um valor invalido
    @Test
    void testRemoverItemPorIndice_Invalido() {
        pedido.adicionarItem(new ItemPedido("Café", 2, 3.0)); //adiciona um item

        boolean removidoNegativo = pedido.removerItemPorIndice(-1); //Tenta remover com um valor negativo
        boolean removidoGrande = pedido.removerItemPorIndice(5); //Tenta remover com um valor maior do que o tamanho da lista

        assertFalse(removidoNegativo); //Ver se da o retorno false, pois o item não pode ser removido
        assertFalse(removidoGrande); //Ver de da o retorno false, pois o item não pode ser removido
        assertEquals(1, pedido.getItens().size()); //Ver se o item adicionado ainda está na lista
    }

    //Teste para calcular o total dos itens no pedido, com varios itens dentro
    @Test
    void testCalcularTotal_ComVariosItens() {
        //Adiciona os itens ao pedido
        pedido.adicionarItem(new ItemPedido("Café", 2, 3.0));  // 6.0
        pedido.adicionarItem(new ItemPedido("Suco", 1, 5.0));  // 5.0
        pedido.adicionarItem(new ItemPedido("Pão", 3, 2.0));   // 6.0

        assertEquals(17.0, pedido.calcularTotal()); //Ver se o valor retornado é o correto da soma dos itens
    }

    //Teste para ver se getItens esta retornando uma copia e não a lista real
    @Test
    void testGetItensEncapsulamento() {
        ItemPedido item = new ItemPedido("Café", 1, 2.0);
        pedido.adicionarItem(item); //Adiciona o item ao pedido

        List<ItemPedido> itens = pedido.getItens();
        itens.clear(); //Tenta apagar/limpar a lista

        assertEquals(1, pedido.getItens().size()); //Verifica se existe um valor dentro da lista
    }
}

