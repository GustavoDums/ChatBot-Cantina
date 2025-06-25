package com.cantinaChatBOT;

import org.junit.jupiter.api.*;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.Locale;
import java.util.Map;
import static org.junit.jupiter.api.Assertions.*;

class CantinaServiceTest {

    private CantinaService cantinaService;

    private final ByteArrayOutputStream outputStreamCaptor = new ByteArrayOutputStream();
    private final PrintStream originalOut = System.out;
    private Locale defaultLocale;  //Para guardar o locale original

    @BeforeEach
    void setUp() {
        cantinaService = new CantinaService();
        System.setOut(new PrintStream(outputStreamCaptor)); //Redireciona o System.out para capturar o que é impresso no console
        defaultLocale = Locale.getDefault();
        Locale.setDefault(Locale.forLanguageTag("pt-BR"));
    }

    @AfterEach
    void tearDown() {
        System.setOut(originalOut);
        Locale.setDefault(defaultLocale);
    }

    //Teste para ver se o cardapio da sendo iniciado
    @Test
    void testCardapioInicializado() {
        Map<String, Double> cardapio = cantinaService.getCardapio();
        assertNotNull(cardapio); //Ver se ele esta vazio
        assertFalse(cardapio.isEmpty()); //Ver se ele esta vazio

        //Ver se os valores que estão contidos estão corretos
        assertEquals(4.50, cardapio.get("Pão de Queijo"));
        assertEquals(8.50, cardapio.get("Hamburgão"));
        assertEquals(5.00, cardapio.get("Café P"));
    }

    //Teste para ver se o pedido esta sendo impresso de forma correta
    @Test
    void testReceberPedidoImprimeCorretamente() {
        Pedido pedido = new Pedido(1);
        //Adiciona dois itens ao pedido
        pedido.adicionarItem(new ItemPedido("Pão de Queijo", 2, 4.50));
        pedido.adicionarItem(new ItemPedido("Café P", 1, 5.00));

        cantinaService.receberPedido(pedido);

        String output = outputStreamCaptor.toString().trim(); //Captura a saida do console

        //Ver se o contem as mensagens do pedido, e os itens, na mensagem que é enviada ao cliente
        assertTrue(output.contains("Pedido recebido pela cantina:"));
        assertTrue(output.contains(String.format(Locale.forLanguageTag("pt-BR"), "- 2 x Pão de Queijo (R$ %.2f)", 9.00)));
        assertTrue(output.contains(String.format(Locale.forLanguageTag("pt-BR"), "- 1 x Café P (R$ %.2f)", 5.00)));
        assertTrue(output.contains(String.format(Locale.forLanguageTag("pt-BR"), "💰 Total: R$ %.2f", 14.00)));
    }
}
