package com.cantinaChatBOT;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

/**
 * Componente Spring Boot que inicia o bot automaticamente na inicialização da ConsoleApp
 */
@Component //@Component para indicar ao Spring que é uma classe componente gerenciada
public class BotRunner implements CommandLineRunner { //Implementa essa interface para o metodo run() ser executado automaticamente logo apos o spring boot iniciar

    private final CantinaService cantinaService;
    private final IABotService iaBotService;

    //O Spring vai injetar automaticamente as dependencias
    public BotRunner(CantinaService cantinaService, IABotService iaBotService) {
        this.cantinaService = cantinaService;
        this.iaBotService = iaBotService;
    }

    @Override
    public void run(String... args) throws Exception {
        try {
            //Cria um objeto utilizando a implementação padrao da sessão Telegram
            TelegramBotsApi botsApi = new TelegramBotsApi(DefaultBotSession.class);
            CantinaTelegramBot telegramBot = new CantinaTelegramBot(cantinaService, iaBotService);
            botsApi.registerBot(telegramBot); //Registra o Bot na API do Telegram
            System.out.println("Bot da cantina iniciado no Telegram com sucesso!");

            //Se der algum erro exibe a mensagem
        } catch (TelegramApiException e) {
            System.err.println("Erro ao iniciar o bot do Telegram: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
