package com.cantinaChatBOT;

import java.time.LocalTime;
import java.util.*;

/**
 * Classe com métodos de cada opção do menu principal do ChatBot
 * Também contém metodo para exibição do menu principal
 * As classes estão com o 'telegram' no final pois o sistema foi feito inicialmente para rodar no terminal, assim adicionei o
 * telegram ao final quando fiz a conversão das classes, para o telegram
 * Os prompts tiveram que ser bem especificados pois a IA alterava o sentido e a lingua em alguns casos
 */
public class CantinaBot {
    //Iniciar o CantinaService e o IABotService
    private final CantinaService cantinaService;
    private final InterfaceIABotService iaBotService;

    //Maps para controlar as sessões
    //Usa o HashMap por ser rápido, e pela facilidade de buscar as informações contidas
    private final Map<String, Pedido> pedidosTelegram = new HashMap<>();
    private final Map<String, Cliente> clientesTelegram = new HashMap<>();

    // Instância única do gerador de códigos, cada codigo gerado é unico
    private final CodigoClienteGenerator geradorCodigo = GeradorCodigoCliente.getInstance();

    //Construtor da classe
    public CantinaBot(CantinaService cantinaService, InterfaceIABotService iaBotService) {
        this.cantinaService = cantinaService;
        this.iaBotService = iaBotService;
    }

    //Metodo para processar as mensagens do telegram
    public String processarMensagemTelegram(String mensagem, String chatId) {

        //Garantir que exista um cliente, se não existir um cliente cria um novo, e gera um novo codigo para ele
        clientesTelegram.putIfAbsent(chatId, new Cliente(geradorCodigo));
        Cliente cliente = clientesTelegram.get(chatId);

        //Garantir que exista um pedido, se não existir criar um novo pedido e usa o codigo gerado
        pedidosTelegram.putIfAbsent(chatId, new Pedido(cliente.getCodigo()));
        Pedido pedido = pedidosTelegram.get(chatId);

        //Remove os espaços da mensagem do cliente e converte tudo para minuscula para garantir a leitura correta dos valores
        //digitados pelo cliente, para facilitar na hora de comparar
        String input = mensagem.trim().toLowerCase();

        //Cada case tem varias opções para garantir uma melhor experiencia ao cliente
        switch (input) {

            //Iniciar o atendimento pelo bot, alguns cliente normalmente enviam mensagens de bom dia antes de uma conversa
            //então coloquei essas opções, pois o bot precisa uma mensagem para iniciar
            case "/start", "iniciar", "menu", "bom dia", "boa noite", "boa tarde", "eae", "aoba", "eai", "oi", "tudo certo?",
                 "so de boa", "de boas", "fala meu chegado", "fala parceiro", "eai parceiro", "eae parceiro", "eae meu caro",
                 "fala meu nobre", "so de boas", "tudo bão", "tudo bão?", "tudo bem?", "quero ver o menu", "ver o menu", "ver menu",
                 "ver menu principal", "ver o menu principal", "quero ver o menu principal" -> {
                String saudacao = gerarSaudacao(); //Gera a saudação

                //String da mensagem inicial, e adiciona o menu
                String mensagemInicial = saudacao + " Bem-vindo à Cantina da Faculdade!\n" +
                        "Seu código de atendimento é #" + cliente.getCodigo() + ".\n\n" +
                        "Não altere o sentido as opções no menu, DEIXE exatamente como ele é, apenas formate, e mantenha igual sem alterar nada" +
                        "que seja de facil interpretação pelo usuario, mantenha exatamente igual como é" +
                        "NÃO ADICIONE EXEMPLOS AO MENU, MANTENHA O MENU EXATAMENTE IGUAL, NÃO ALTERE NADA"+ exibirMenuTelegram();

                //Retorna ao cliente, a mensagem inicial gerada pela IA
                return iaBotService.melhorarResposta("Responda apenas em português do Brasil com simpatia e bom humor, " +
                        "NÃO altere valores e NÃO altere informações:\n" + mensagemInicial);
            }

            //Case para imprimir o cardapio ao cliente
            case "ver cardápio", "cardápio", "menu cardápio", "ver cardapio", "quero ver o cardapio", "ver o cardapio",
                 "quero ver o cardápio", "ver o cardápio" -> {
                //String para gerar o titulo do cardapio, apenas o titulo passa pela IA
                String titulo = iaBotService.melhorarResposta("""
                    Responda em português do Brasil.\s
                    Reescreva de forma simpática e calorosa:\s
                    NÃO mude a frase, NÃO adicione notas dizendo que fez o que foi pedido.\s
                    mantenha a mesma estrutura:\s
                    'Confira nosso cardápio completo abaixo!'\s
                   \s""");

                //Retorna o titulo melhorado por IA, e adiciona o cardapio, que não passa pela IA, para evitar erros de escrita e valores
                return titulo + "\n\n" + construirCardapioTelegram();
            }

            //Case para o pedido, caso o cliente digite alguma das opções, ele informa ao cliente como adicionar o item ao seu pedido
            case "pedido", "adicionar", "adicionar item", "como faço meu pedido?", "como faço meu pedido", "como fazer um pedido?",
                 "como faço o pedido", "como faço o pedido?", "como fazer um pedido", "como eu adiciono itens?", "como eu adiciono um item?",
                 "como adicionar itens", "como adicionar um item" -> {

                //Retorna ao cliente uma mensagem explicando como ele faz para adicionar itens no pedido
                return iaBotService.melhorarResposta("""
                        Explique de forma clara e simpática como adicionar ou remover itens\
                        explique sem fazer um texto muito comprido, seja breve:
                        Para adicionar itens, digite no formato:
                        `adicionar [número do item] [quantidade]`
                        Para remover, digite:
                        `remover [número do item]`
                        """);
            }

            //Case caso o cliente queria consultar o seu pedido
            case "consultar", "meu pedido", "consultar pedido", "ver meu pedido", "ver pedido", "consultar o meu pedido",
                 "consultar meu pedido", "quero ver o meu pedido", "quero ver o pedido", "quero consultar o pedido",
                 "quero consultar o meu pedido", "quero ver pedido" -> {
                //Retorna o resumo do pedido
                return construirResumoPedidoTelegram(pedido, cliente);
            }

            //Case para finalizar o pedido, apenas exibe a mensagem pois a finalização acontece quando o cliente digitar confirmar
            case "enviar", "finalizar pedido", "quero finalizar o pedido", "quero enviar o pedido",
                 "enviar meu pedido", "quero enviar meu pedido", "quero enviar o meu pedido", "enviar o meu pedido" -> {
                //Retorna uma mensagem pedindo para o cliente digitar confirmar
                return iaBotService.melhorarResposta("Responda apenas em português do Brasil, não altere o sentido," +
                        "escreva praticamente a mesma coisa: " +
                        "DIGITE: 'confirmar' para finalizar o seu pedido!");
            }

            //Case para encerrar o sistema, reseta o ID do cliente dos pedidos e do cliente e retorna a mensagem
            case "sair", "encerrar", "fechar atendimento", "fechar", "sair do atendimento", "finalizar atendimento" -> {
                pedidosTelegram.remove(chatId); //Remove o pedido associado ao chat para evitar divergências
                clientesTelegram.remove(chatId); //Remove o cliente associado ao chat para evitar divergências
                return iaBotService.melhorarResposta("Responda apenas em português do Brasil com tom amigável: " +
                        "\uD83C\uDF7D️ Obrigado por usar o atendimento da Cantina! Volte sempre!");
            }

            //Case para confirmar o pedido da cantina, contem varias palavras que o cliente pode digitar
            case "confirmar", "confirmo", "certo", "correto", "tudo certo", "tudo correto", "esta certo",
                 "esta correto", "ta certinho", "ta correto", "ta certo", "eu confirmo", "sim", "positivo",
                 "afirmativo", "pode", "pode enviar" -> {
                return finalizarPedidoTelegram(chatId, pedido, cliente);
            }
        }

        //If para adicionar os itens ao pedido ou remover, chama os métodos correspondentes
        if (input.startsWith("adicionar ")) {
            return adicionarItemTelegram(input, pedido);
        } else if (input.startsWith("remover ")) {
            return removerItemTelegram(input, pedido);
        }

        //Se não se encaixar em nenhuma das opções, ele retorna uma mensagem da IA, respondendo à pergunta do cliente
        return iaBotService.responderPergunta("Responda em português do Brasil, responda como se você fosse um atendente de uma cantina de faculdade" +
                "diga ao cliente uma dessas duas opções: pedi para o cliente responder no padrão solicitado, ou que não compreendeu o que foi dito" + mensagem, cliente.getCodigo());
    }

    /**
     * Metodo para gerar a saudação ao cliente, conforme o horario gera a sua correspondente
     * @return para retornar o boa noite, pois não precisa verificar
     */
    private String gerarSaudacao() {
        int hora = LocalTime.now().getHour(); //Captura o horario atual
        if (hora < 12) return "Bom dia!";  //Verifica o horario e retorna a mensagem
        if (hora < 18) return "Boa tarde!"; //Verifica o horario e retorna a mensagem
        return "Boa noite!"; //Se não for em nenhuma das opções acima ele retorna o "boa noite", pois é a unica opção restante
    }

    /**
     * Metodo para exibir o menu principal
     * Apenas retorna o menu principal
     * @return que retorna o menu montado
     */
    private String exibirMenuTelegram() {
        return """
           \uD83C\uDF7D️ *MENU PRINCIPAL* \uD83C\uDF7D️

           \uD83D\uDC49 *Ver cardápio* - \uD83D\uDCCB Ver o cardápio completo
           \uD83D\uDC49 *Adicionar [numero do item  quantidade]* - \uD83C\uDF54 Adicionar itens
           \uD83D\uDC49 *Remover [numero do item]* - \uD83C\uDF54 Para remover itens
           \uD83D\uDC49 *Consultar* - \uD83D\uDD0D Ver resumo do seu pedido
           \uD83D\uDC49 *Enviar* - ✅ Finalizar e enviar pedido
           \uD83D\uDC49 *Sair* - ❌ Encerrar o atendimento
           Lembrete: Sempre digite sair ao finalizar e enviar o pedido.

           Digite uma das palavras acima para navegar.
           """;
    }

    /**
     * Metodo simples apenas para construir o cardapio da cantina
     * @return que retorna o cardapio montado
     */
    private String construirCardapioTelegram() {
        StringBuilder cardapio = new StringBuilder("\uD83C\uDF7D️ CARDÁPIO DA CANTINA   \uD83C\uDF7D️\n");
        int i = 1; //Variavel que vai ser usada para numerar os itens

        //For para percorrer todos os itens
        for (Map.Entry<String, Double> item : cantinaService.getCardapio().entrySet()) {
            cardapio.append(i++).append(". ").append(item.getKey()) //getKey para pegar o produto
                    .append(" - R$ ").append(String.format("%.2f", item.getValue())) //getValue para pegar o valor do item
                    .append("\n");
        }
        //Retorna o cardapio, não passa pela IA para evitar erros na digitação, em testes a IA alterava o nome dos itens
        return cardapio.toString();
    }

    /**
     * Metodo para construir o resumo do pedido do cliente
     * @param pedido verificar se o pedido é nulo
     * @param cliente para mostrar o ID do cliente (Codigo do seu atendimento)
     * @return para retornar o resumo do pedido
     */
    private String construirResumoPedidoTelegram(Pedido pedido, Cliente cliente) {
        if (pedido == null || pedido.getItens().isEmpty()) { //Se o pedido estiver vazio responde ao cliente
            return iaBotService.melhorarResposta("Responda em português do Brasil: " +
                    "Diga ao cliente de forma simpática que ainda não há itens no pedido.");
        }

        //Gerador do resumo do pedido
        StringBuilder resumo = new StringBuilder("\uD83D\uDCCB Seu pedido (Cliente #" + cliente.getCodigo() + "):\n");
        int i = 1;
        for (ItemPedido item : pedido.getItens()) {
            resumo.append(i++).append(". ") //Adiciona o numero na frente de cada item
                    .append(item.getQuantidade()).append(" x ") //Adiciona a quantidade do item e a letra 'x'
                    .append(item.getProduto()).append(" - R$ ") //Adiciona o produto e o 'R$'
                    .append(String.format("%.2f", item.getSubtotal()))  //Calcula e adiciona o subtotal de cada item
                    .append("\n"); //Adiciona um \n para dar um espaçamento
        }

        //Adiciona o total de todos os itens e retorna o resumo ao cliente
        resumo.append("Total: R$ ").append(String.format("%.2f", pedido.calcularTotal()));
        return iaBotService.melhorarResposta("Responda em português do Brasil com tom simpático, " +
                "NÃO altere valores, NÃO invente valores, NÃO altere os itens, mantenha o pedido extamente como está" +
                "apenas formate, seja breve:\n" + resumo);
    }

    /**
     * Metodo para adicionar um item a um pedido
     * @param comando para validar o que digitado pelo cliente
     * @param pedido para adicionar o(s) iten(s) ao pedido e calcular o total
     * @return Usado para retornar a resposta ao usuario. Exemplo: se foi adicionado ao pedido, se foi removido, ou se deu erro
     */
    private String adicionarItemTelegram(String comando, Pedido pedido) {
        try {
            String[] partes = comando.split(" ");  //Divide o comando digitado pelo cliente em partes
            if (partes.length != 3) {  //Verificação para ver se o comando tem 3 partes, (O comando('adicionar'), o numero do item, e a quantidade)
                //Se não tiver os 3 espaços, ele retorna uma mensagem ao cliente pedido para escrever no formato correto
                return iaBotService.melhorarResposta("Responda em português do Brasil, " +
                        "sem alterar o sentido dela, apenas responda exatamente igual está aqui, não altere nada: " +
                        "Formato inválido. Use: adicionar [número do item] [quantidade]");
            }

            int numeroItem = Integer.parseInt(partes[1]); //Converte o primeiro valor(numero do item) para int
            int quantidade = Integer.parseInt(partes[2]); //Converte o segundo valor(quantidade) para int

            if (quantidade <= 0) {  //Verifização do valor digitado, se for 0 ou negativo retorna a mensagem ao cliente
                return iaBotService.melhorarResposta("Responda em português do Brasil:" +
                        "A quantidade deve ser maior que zero.");
            }

            //Lista com os itens do cardapio para poder fazer a verificação do comando digitado pelo cliente
            List<String> itensCardapio = new ArrayList<>(cantinaService.getCardapio().keySet());

            //Verificação para ver se o numero do item esta dentro do esperado, dos limites, e retorna uma mensagem ao cliente
            //dizendo que o valor esta incorreto
            if (numeroItem < 1 || numeroItem > itensCardapio.size()) {
                return iaBotService.melhorarResposta("Responda em português do Brasil:" +
                        "Número do item inválido. Digite \"Ver cardápio\" para consultar o cardápio da cantina.");
            }

            String produto = itensCardapio.get(numeroItem - 1);  //Identifica o produto (o -1 serve para dimiuir em um o valor digitado, pois uma lista o primeiro item é o da posição 0)
            double preco = cantinaService.getCardapio().get(produto); //Pega o valor do item

            //Cria um itempedido com os dados que foram capturados acima
            pedido.adicionarItem(new ItemPedido(produto, quantidade, preco));

            //Retorna uma mensagem ao cliente dizendo que o produto selecionado foi adicionado
            return iaBotService.melhorarResposta(
                    "Responda em português do Brasil, seja breve, NÃO altere os valores dos itens, NÃO invente valores" +
                            ", NÃO altere os itens, seja simples e apenas apresente a mensagem: "
                            + quantidade + " x " + produto +
                            " adicionado ao pedido. Total parcial: R$ "
                            + String.format("%.2f", pedido.calcularTotal())
            );

            //Se o cliente digitar algo que não se encaixa no esperado ele cai nessa exceção, que retorna uma mensagem pedindo
            //para usar o formato correto
        } catch (NumberFormatException e) {
            return iaBotService.melhorarResposta("Responda em português do Brasil, seja breve e não crie coisas sem sentido " +
                    "Erro ao interpretar comando. Use o formato: adicionar [número do item] [quantidade]");
        }
    }

    /**
     * Metodo para remover um item do pedido
     * @param comando para validar o que digitado pelo cliente
     * @param pedido para remover o item do pedido
     * @return Para retornar ao cliente, caso de erro, ou se foi removido com sucesso
     */
    private String removerItemTelegram(String comando, Pedido pedido) {
        try {
            String[] partes = comando.split(" "); //Divide o comando digitado pelo cliente em partes
            if (partes.length != 2) {  //Verifica se o comando está dividido em duas partes
                return iaBotService.melhorarResposta("Responda em português do Brasil seja breve" +
                        "Formato inválido. Use: remover [número do item]");
            }

            int numeroItem = Integer.parseInt(partes[1]); //Converte a segunda parte do comando para int
            int index = numeroItem - 1; //Por ser baseada em indices diminui-se 1

            List<ItemPedido> itens = pedido.getItens();  //Cópia protegida da lista
            if (index >= 0 && index < itens.size()) {  //Verifica se o valor não negativo e esta no tamanho da lista
                String nomeProdutoRemovido = itens.get(index).getProduto();  //Pega o nome do item antes de remover
                pedido.removerItemPorIndice(index);  // Remove o item da lista real
                return iaBotService.melhorarResposta("Responda em português do Brasil, seja simpático e breve, NÃO altere" +
                        "o nome do item, mantenha EXATAMENTE o mesmo: " +
                        "Item \"" + nomeProdutoRemovido + "\" removido com sucesso!");
            } else {
                //Caso o numero não esteja no esperado, retorna a mensagem de numero invalido
                return iaBotService.melhorarResposta("Responda em português do Brasil, Número inválido. Tente novamente.");
            }

        } catch (NumberFormatException e) {  //Se não tiver em duas partes retorna uma mensagem ao cliente dizendo o formato
            return iaBotService.melhorarResposta("Responda em português do Brasil, Formato inválido. Use: remover [número do item]");
        }
    }

    /**
     * Classe para a finalização do pedido do cliente
     * Manda o pedido para a classe CantinaService e remove o ID do chat nos pedidos
     * @param chatId ID do chat, gerado pelo gerador de codigo
     * @param pedido pedido do cliente
     * @param cliente Cliente
     * @return retorna uma mensagem confirmando a finalização do pedido, e retorna o numero do pedido do cliente e o valor total
     */
    private String finalizarPedidoTelegram(String chatId, Pedido pedido, Cliente cliente) {
        if (pedido == null || pedido.getItens().isEmpty()) { //Se o pedido estiver vazio retorna uma mensagem ao cliente
            return iaBotService.melhorarResposta("Responda em português do Brasil, Não é possível finalizar um pedido vazio.");
        }

        cantinaService.receberPedido(pedido); //Registra o pedido no servico da cantina
        pedidosTelegram.remove(chatId); //Remove o pedido do cliente para zerar a sessão do cliente depois de ter finalizado

        return iaBotService.melhorarResposta(  //Retorna a resposta de confirmação da finalização, mostra o codigo do pedido de o valor total
                "Responda em português do Brasil. Confirme ao cliente que o pedido foi finalizado, seja breve, NÃO altere valores, " +
                        "agradecendo com simpatia:\n" +
                        "Lembre-se de digitar 'sair' ao terminar o pedido" +
                        "Pedido #" + cliente.getCodigo() + " finalizado com sucesso! Total: R$ " + String.format("%.2f", pedido.calcularTotal())
        );
    }
}
