package com.cantinaChatBOT;

import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class GeradorCodigoClienteTest {

    //Teste para ver se o contador de codigo do cliente muda quando muda o dia
    @Test
    void testContadorResetaQuandoDataMuda() throws Exception {
        GeradorCodigoCliente gerador = GeradorCodigoCliente.getInstance();

        // Gera um código para o contador avançar
        int codigoInicial = gerador.gerarNovoCodigo();

        // Pega o campo dataAtual e muda para ontem, forçando a mudança de data
        Field dataAtualField = GeradorCodigoCliente.class.getDeclaredField("dataAtual");
        dataAtualField.setAccessible(true);
        dataAtualField.set(gerador, LocalDate.now().minusDays(1));

        // Gera um código depois de mudar a data
        int codigoAposMudanca = gerador.gerarNovoCodigo();

        assertTrue(codigoInicial < codigoAposMudanca || codigoAposMudanca == 1,
                "Código após mudança de data deve ser 1 (contador resetado)");

        // O codigo deve ser 1, pois a cada mudanca de dia o codigo reseta para 1
        assertEquals(1, codigoAposMudanca);
    }
}
