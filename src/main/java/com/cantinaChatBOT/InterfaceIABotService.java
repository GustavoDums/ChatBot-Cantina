package com.cantinaChatBOT;

/**
 * Interface criada para poder fazer os testes da classe IABotService
 */
public interface InterfaceIABotService {
    String melhorarResposta(String prompt);
    String responderPergunta(String pergunta, int codigoCliente);
}
