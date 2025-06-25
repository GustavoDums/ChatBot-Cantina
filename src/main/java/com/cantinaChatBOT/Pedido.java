package com.cantinaChatBOT;

import java.util.ArrayList;
import java.util.List;

/**
 * Classe de pedido
 * Possui os metodos de adicionar e remover um item de um pedido e calcular o valor total de um pedido
 */
public class Pedido {
    private final int codigoCliente; //Para o codigo do cliente estar atribuido ao pedido e evitar que um cliente tenha o pedido de outro
    private final List<ItemPedido> itens;  //Lista com os itens que o cliente adicionou
    private final String status;

    public Pedido(int codigoCliente) {
        this.codigoCliente = codigoCliente;
        this.itens = new ArrayList<>();
        this.status = "Em aberto";  //Status fixo 'Em aberto'     Por enquanto para a demo será apenas esse, mas na futura melhoria tera mais opções
    }

    //Adiciona o item na lista do pedido
    public void adicionarItem(ItemPedido item) {
        itens.add(item);
    }

    //Remove um item do pedido com base na posição dele
    public boolean removerItemPorIndice(int index) {
        if (index >= 0 && index < itens.size()) { //If para garantir que o numero não seja negativo e esteja dentro do tamanho da lista
            itens.remove(index);
            return true;  //Retorna True se der certo
        }
        return false; //False se der errado
    }

    //Usa o StreamAPI para iterar e somar com clareza
    public double calcularTotal() {
        return itens.stream().mapToDouble(ItemPedido::getSubtotal).sum();
    }

    // Getters
    public List<ItemPedido> getItens() {
        return new ArrayList<>(itens); // Retorna cópia para encapsulamento
    }

    public String getStatus() {
        return status;
    }

    public int getCodigoCliente() {
        return codigoCliente;
    }
}