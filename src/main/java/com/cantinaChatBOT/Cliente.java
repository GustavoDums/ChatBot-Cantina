package com.cantinaChatBOT;

public class Cliente {
    private final int codigo; //Final para que não seja alterado

    /**
     * Foi necessário criar os dois construtores para poder fazer o teste do gerador de código
     */
    // Construtor que recebe o gerador via interface, para injetar um gerador controlado no teste
    public Cliente(CodigoClienteGenerator geradorCodigo) {
        this.codigo = geradorCodigo.gerarNovoCodigo();
    }

    // Construtor padrão sem parâmetros, usa o gerador singleton, uso no sistema
    public Cliente() {
        this(GeradorCodigoCliente.getInstance());
    }

    public int getCodigo() {
        return codigo;
    }

    //toString para ter uma melhor vizualição do codigo do cliente
    @Override
    public String toString() {
        return "Cliente #" + codigo;
    }
}
