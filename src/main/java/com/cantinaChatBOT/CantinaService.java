package com.cantinaChatBOT;

import org.springframework.stereotype.Service;

import java.util.LinkedHashMap;
import java.util.Locale;
import java.util.Map;

/**
 * Classe de serviço da cantina
 * Contém o contrutor com o HashMap do cardápio
 * Contém o metodo de receber o pedido
 */
@Service
public class CantinaService {
    private final Map<String, Double> cardapio;

    //Construtor que cria o cardapio por HashMap
    public CantinaService() {  //HashMap do cardapio da cantina
        cardapio = new LinkedHashMap<>();  //Usa o LinkedHashMap pois precisamos saber a posição de cada item no map
        cardapio.put("Pão de Queijo", 4.50);
        cardapio.put("Hamburgão", 8.50);
        cardapio.put("Doguinho", 8.50);
        cardapio.put("Bolinho de carne", 9.00);
        cardapio.put("Pão com bolinho", 13.00);
        cardapio.put("Almoço", 26.00);
        cardapio.put("Misto Quente", 8.50);
        cardapio.put("Bolo", 5.00);
        cardapio.put("Água", 4.50);
        cardapio.put("Chocoleite", 5.00);
        cardapio.put("Refrigerante 600ml", 10.00);
        cardapio.put("Refrigerante lata", 8.00);
        cardapio.put("Energético", 14.00);
        cardapio.put("Café P", 5.00);
        cardapio.put("Café G", 6.00);
    }

    public Map<String, Double> getCardapio() {
        return cardapio;
    }

    /**
     * Metodo de receber o pedido
     * Por hora apenas exibe uma mensagem com informações do pedido
     * Futuramente vai fazer o envio do pedido do cliente para a cantina via WhatsApp
     */
    public void receberPedido(Pedido pedido) {
        System.out.println("Pedido recebido pela cantina:");
        for (ItemPedido item : pedido.getItens()) { //Pega cada item do pedido
            System.out.printf(Locale.forLanguageTag("pt-BR"), "- %d x %s (R$ %.2f)%n", //Formata o texto
                    item.getQuantidade(),              //usa o locale.forLanguageTag para garantir que o formato esteja em pt-BR
                    item.getProduto(),
                    item.getSubtotal());
        }
        //Tambem usa o Locale para garantir a formatação correta
        System.out.printf(Locale.forLanguageTag("pt-BR"), "💰 Total: R$ %.2f%n", pedido.calcularTotal());
    }
}
