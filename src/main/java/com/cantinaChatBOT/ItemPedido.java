package com.cantinaChatBOT;

/**
 * Classe para os itens do pedido
 * Possui as informações de cada produto da cantina: Nome do produto, quantidade e preço
 */
public class ItemPedido {
    //Todos final, não podem ser alterados após a criação do objeto
    private final String produto;
    private final int quantidade;
    private final double precoUnitario;

    //Construtor padrão com os 3 campos
    public ItemPedido(String produto, int quantidade, double precoUnitario) {
        this.produto = produto;
        this.quantidade = quantidade;
        this.precoUnitario = precoUnitario;
    }

    // Getters
    public String getProduto() {
        return produto;
    }

    public int getQuantidade() {
        return quantidade;
    }

    public double getSubtotal() {
        return quantidade * precoUnitario;
    }

    // toString para mostrar a quantidade dos itens formatada
    @Override
    public String toString() {
        return String.format("%d x %s - R$ %.2f (Subtotal: R$ %.2f)",
                quantidade, produto, precoUnitario, getSubtotal());
    }
}