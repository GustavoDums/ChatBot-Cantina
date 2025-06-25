package com.cantinaChatBOT;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class CantinaBotTest {

    private CantinaBot cantinaBot;

    // Mock para InterfaceIABotService
    static class IABotServiceMock implements InterfaceIABotService {
        @Override
        public String melhorarResposta(String prompt) {
            return prompt; // Retorna o prompt original
        }

        @Override
        public String responderPergunta(String pergunta, int codigoCliente) {
            return "Resposta genérica para: " + pergunta;
        }
    }

    @BeforeEach
    void setUp() {
        // Fake CantinaService com cardápio fixo
        CantinaService fakeCantinaService = new CantinaService() {
            @Override
            public Map<String, Double> getCardapio() {  //Cria um cardapio Fake com dois itens
                return Map.of("Café", 3.0, "Pão de Queijo", 4.0);
            }

            @Override
            public void receberPedido(Pedido pedido) {
                // Simula recebimento do pedido (sem ação)
            }
        };

        InterfaceIABotService fakeIaBotService = new IABotServiceMock();

        cantinaBot = new CantinaBot(fakeCantinaService, fakeIaBotService);
    }

    //Teste para ver se o comando /start esta retornando a mensagem de bem-vindo
    @Test
    void testStartComando() {
        String resposta = cantinaBot.processarMensagemTelegram("/start", "12345");
        assertTrue(resposta.toLowerCase().contains("bem-vindo"));
    }

    //Teste para ver se o comando ver cardápio retorna os itens corretamente
    @Test
    void testVerComandoCardapio() {
        String resposta = cantinaBot.processarMensagemTelegram("ver cardápio", "12345");
        assertTrue(resposta.contains("Café"));
        assertTrue(resposta.contains("Pão de Queijo"));
    }

    //Teste para ver se o comando de pedido orienta o cliente sobre como adicionar o item ao pedido dele
    @Test
    void testComandoPedido() {
        String resposta = cantinaBot.processarMensagemTelegram("pedido", "12345");
        assertTrue(resposta.toLowerCase().contains("adicionar"));
    }

    //Teste para verificar se o item foi adicionado com sucesso
    @Test
    void testAdicionarItemValido() {
        cantinaBot.processarMensagemTelegram("/start", "12345");
        String resposta = cantinaBot.processarMensagemTelegram("adicionar 1 2", "12345");
        assertTrue(resposta.toLowerCase().contains("adicionado"));
    }

    //Teste para ver se caso o cliente digite um valor invalido, ele retorna a resposta de valor invalido
    @Test
    void testAdicionarItemFormatoInvalido() {
        cantinaBot.processarMensagemTelegram("/start", "12345");
        String resposta = cantinaBot.processarMensagemTelegram("adicionar x y", "12345");
        assertTrue(resposta.toLowerCase().contains("formato inválido") || resposta.toLowerCase().contains("erro"));
    }

    //Verifica se o item foi removido com sucesso
    @Test
    void testRemoverItemValido() {
        cantinaBot.processarMensagemTelegram("/start", "12345");
        cantinaBot.processarMensagemTelegram("adicionar 1 1", "12345");
        String resposta = cantinaBot.processarMensagemTelegram("remover 1", "12345");
        assertTrue(resposta.toLowerCase().contains("removido"));
    }

    //Teste para ver se está sendo exibido o texto caso o cliente digite em um formato invalido
    @Test
    void testRemoverItemNumeroInvalido() {
        cantinaBot.processarMensagemTelegram("/start", "12345");
        String resposta = cantinaBot.processarMensagemTelegram("remover 5", "12345");
        assertTrue(resposta.toLowerCase().contains("número inválido"));
    }

    //Teste para ver se esta sendo retornado a mensagem dizendo que não tem itens no pedido
    @Test
    void testConsultarPedidoSemItens() {
        cantinaBot.processarMensagemTelegram("/start", "12345");
        String resposta = cantinaBot.processarMensagemTelegram("consultar", "12345");
        assertTrue(resposta.toLowerCase().contains("não há itens") || resposta.toLowerCase().contains("pedido"));
    }

    //Teste para verificar se retorna uma mensagem caso o cliente digite confirmar para um pedido vazio
    @Test
    void testConfirmarPedidoVazio() {
        cantinaBot.processarMensagemTelegram("/start", "12345");
        String resposta = cantinaBot.processarMensagemTelegram("confirmar", "12345");
        assertTrue(resposta.toLowerCase().contains("vazio"));
    }

    //Teste para ver se o pedido é confirmado e finalizado com exito
    @Test
    void testConfirmarPedidoComItens() {
        cantinaBot.processarMensagemTelegram("/start", "12345");
        cantinaBot.processarMensagemTelegram("adicionar 1 1", "12345");
        String resposta = cantinaBot.processarMensagemTelegram("confirmar", "12345");
        assertTrue(resposta.toLowerCase().contains("finalizado"));
    }

    //Teste para ver se quando o cliente deseja enviar ele pede para confirmar
    @Test
    void testEnviarPedido() {
        String resposta = cantinaBot.processarMensagemTelegram("enviar", "12345");
        assertTrue(resposta.toLowerCase().contains("confirmar"));
    }

    //Teste para ver se caso o cliente digite algo fora do sistema, do menu principal, a retorna uma mensagem de resposta generica
    @Test
    void testComandoDesconhecido() {
        String resposta = cantinaBot.processarMensagemTelegram("qual o horário de funcionamento?", "12345");
        assertTrue(resposta.startsWith("Resposta genérica para"));
    }
}
