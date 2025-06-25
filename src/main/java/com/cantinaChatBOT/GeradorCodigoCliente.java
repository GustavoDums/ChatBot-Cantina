package com.cantinaChatBOT;

import java.time.LocalDate;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Gera códigos únicos para clientes, reiniciando a contagem a cada novo dia.
 * Usa o padrão Singleton para garantir que exista apenas uma instância da classe na aplicação inteira
 * para garantir a consistência do gerador
 */
public class GeradorCodigoCliente implements CodigoClienteGenerator {
    private static final GeradorCodigoCliente instance = new GeradorCodigoCliente();

    private LocalDate dataAtual = LocalDate.now(); //Guarda o dia atual
    private final AtomicInteger contador = new AtomicInteger(1); //Usa o AtomicInteger devido a segurança para bots
                                                                           //ja que eles recebem muitas mensagens

    private GeradorCodigoCliente() {}

    public static GeradorCodigoCliente getInstance() {
        return instance;
    }

    @Override
    public synchronized int gerarNovoCodigo() {
        LocalDate hoje = LocalDate.now(); //Verifica o dia atual

        // Se mudou o dia, reinicia a contagem
        if (!hoje.equals(dataAtual)) {
            dataAtual = hoje;
            contador.set(1);
        }

        //Retorna o próximo numero e incrementa para o próximo cliente
        return contador.getAndIncrement();
    }
}
