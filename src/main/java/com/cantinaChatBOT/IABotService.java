package com.cantinaChatBOT;

import org.springframework.ai.chat.ChatClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service //Para o Spring entender que é um serviço
public class IABotService implements InterfaceIABotService{

    //O Spring vai automaticamente fornecer um codigo chatClient
    private final ChatClient chatClient;  //Final para não alterar o valor apos a inicialização

    @Autowired
    public IABotService(ChatClient chatClient) {
        this.chatClient = chatClient;
    }

    /**
     * Metodo para melhorar uma frase simples usando a IA.
     * Envia o prompt para o modelo Mistral no Ollama pelo Spring AI.
     */
    public String melhorarResposta(String prompt) {
        try {
            return chatClient.call(prompt);  //Envia o prompt para a IA e retorna

        } catch (Exception e) { //Se der erro entra nessa exceção
            System.err.println("Erro ao se comunicar com o Spring AI / Ollama:");
            e.printStackTrace();
            return "Erro ao se comunicar com a IA. Verifique se o Ollama está rodando.";
        }
    }

    /**
     * Metodo para responder perguntas abertas do cliente.
     * Insere o código do cliente no contexto.
     */
    public String responderPergunta(String pergunta, int codigoCliente) {
        String prompt = String.format("Cliente #%d perguntou: %s  Responda como se você fosse um atendente de uma cantina" +
            "Peça para o cliente responder no formato necessário" +
                "ou que você não entendeu a pergunta que ele fez", codigoCliente, pergunta);
        return melhorarResposta(prompt); //Uso metodo melhorarResposta criado acima, para responder a pergunta do cliente
    }
}