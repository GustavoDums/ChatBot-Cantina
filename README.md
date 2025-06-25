# ChatBot-Cantina
ChatBot para a cantina da Univille, ele usa o Ollama como IA para fornecer mensagens personalizadas ao cliente

MANUAL DE USO:
1- Abra o projeto na IDE de sua preferência, recomendo o uso do IntelliJ, pois foi nessa IDE que o Bot foi desenvolvido.

2- Para usar o ChatBot, é preciso ter o Ollama baixado em seu sistema, para baixar, basta acessar o site oficial do Ollama e fazer o download, rode o modelo Mistral em sua máquina, para rodar o modelo digite no terminal ou no PowerShell o seguinte comando: 'Ollama run mistral'

- Vale lembrar que este é um modelo relativamente pesado, mas entrega uma boa qualidade, por isso é necessária uma máquina próxima de um nível mediano para rodar. Ele precisa de aproximadamente 4gb a 5gb de memória RAM disponível.

3- Após o modelo ser baixado, instale as dependências pelo Maven, é inportante ter o Maven instalado também, para rodar basta digitar no terminal da IDE o seguinte comando: 'mvn clean install'

4- Entre no site oficial do Telegram e vá em API, procure pelo BotFather, e envie a mensagem '/start', este é um Bot oficial do Telegram para criar Bot's, ele vai guiar para a criação do nome e ID do seu bot, e vai enviar um Token de acesso.

5- Criado o bot no BotFather, entre no sistema e vá até a Classe 'CantinaTelegramBot' e no método 'getBotUsername()' troque o 'AQUI O NOME DO BOT CRIADO' pelo nome do Bot que foi criado, e no método 'getBotToken()' troque o 'AQUI O SEU TOKEN DO BOT', pelo token que o BotFather enviou. Atenção: Tenha cuidado com esse Token, pois qualquer pessoa com acesso a ele, terá total acesso ao seu Bot

6- Após isso o sistema estará pronto para ser executado, basta ir à Classe 'ConseleApp' que é a Main do sistema e executar, deve aparecer a mensagem que o bot foi iniciado com sucesso no seu terminal.

7- Após executar, e confirmar que o bot foi iniciado corretamente, basta abrir o Telegram e pesquisar pelo nome que foi criado no BotFather

8- Identificado o seu Bot no Telegram, basta digitar uma mensagem padrão de iniciação dos bots do Telegram que é '/start', ou uma das opções que podem ser encontradas dentro da Classe 'CantinaBot' no método 'processarMensagemTelegram', um exemplo de outra mensagem para iniciar o bot seriam: 'Boa noite', 'Bom dia', 'Fala parceiro'. Lembrete: pode ser adicionado novas opções ou remover se quiser.

9- Se quiser alterar o modelo de IA do Ollama para utilizar no sistema, basta alterar o nome no arquivo 'application.yml', que está na pasta 'resources', e instalar o modelo na sua máquina rodando o: 'Ollama run "NOME DO MODELO"', vale lembrar que é necessária uma máquina relativamente boa para rodar os modelos de IA com melhor processamento e criação de mensagens, procure os modelos no site oficial do Ollama, para lhe auxiliar na escolha. 

10- Se quiser alterar a IA para uma que não seja do Ollama, basta fazer as alterações necessárias que a IA escolhida necessita, o processo de conversão para JSON, e a ponte entre o servidor e sistema Java já é todo automatizado, pois o sistema utiliza o Spring-AI para automatizar este processo.

11- Agora é só testar o seu Bot!
