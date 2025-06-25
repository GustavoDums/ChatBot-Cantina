package com.cantinaChatBOT;

//Interface que permite varias gerações de codigo do cliente, foi criada para poder realizar os testes
@FunctionalInterface
public interface CodigoClienteGenerator {
    int gerarNovoCodigo();
}
