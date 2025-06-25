package com.cantinaChatBOT;

import org.telegram.telegrambots.bots.TelegramLongPollingBot; //Import do LongPollingBot
import org.telegram.telegrambots.meta.api.objects.Update; //Atualização recebida do Telegram
import org.telegram.telegrambots.meta.api.objects.Message; //Para receber a mensagem do usuario
import org.telegram.telegrambots.meta.api.methods.send.SendMessage; //Para criar mensagens de resposta
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;//Exceção da propria API do Telegram

/**
 * Le as mensagens, envia para o BOT processar e responde no Telegram
 * Essa classe extende a TelegramLongPollingBot, pois é a recomendação oficial do telegram para integrar os bots com a API
 * Esta classe: TelegramLongPollingBot escuta continuamente por atualizações, e reage a cada nova mensagem recebida
 */
public class CantinaTelegramBot extends TelegramLongPollingBot {

    //Armazena uma instância da classe CantinaBot, que é onde toda a lógica funciona
    private final CantinaBot cantinaBot;

    // Construtor que recebe os serviços para passar para o CantinaBot
    public CantinaTelegramBot(CantinaService cantinaService, IABotService iaBotService) {
        this.cantinaBot = new CantinaBot(cantinaService, iaBotService);
    }

    //Define o Username do bot no telegram (nome este que foi criado no BotFather)
    @Override
    public String getBotUsername() {
        return "CantinaUnivilleChatBot";
    }

    //Token gerado pelo BotFather permite autenticar e enviar mensagens no nome do Bot
    @Override
    public String getBotToken() {
        return "7700722473:AAGUt5c-dp0eYhiXDUw6g9oX-Ajakc5hob0";
    }

    //Este metodo é chamado toda vez que o bot recebe uma mensagem
    @Override
    public void onUpdateReceived(Update update) {
        if (update.hasMessage() && update.getMessage().hasText()) { //If para garantir que a mensagem recebida possui algum texto
            Message msg = update.getMessage();  //Extrai a mensagem
            String texto = msg.getText();
            String chatId = msg.getChatId().toString(); //Extrai o ID

            // Processa a mensagem usando seu CantinaBot e pega a resposta
            String resposta = cantinaBot.processarMensagemTelegram(texto, chatId);

            // Cria um objeto para mandar a resposta ao Telegram
            // Tambem define para qual cliente e qual mensagem enviar, para evitar que um cliente receba mensagem de outro
            SendMessage message = new SendMessage();
            message.setChatId(chatId);
            message.setText(resposta);

            try {
                execute(message);  //Usa o metodo execute da API para enviar a mensagem
            } catch (TelegramApiException e) {
                e.printStackTrace(); //Se der algum erro imprime no console
            }
        }
    }
}
