package com.cantinaChatBOT;

import org.junit.jupiter.api.Test;

//Teste simples do Main
//Testa se o Main vai executar sem lançar a excecao
class ConsoleAppTest {
    @Test
    void testMainExecutaSemExcecao() {
        ConsoleApp.main(new String[0]);
    }
}
