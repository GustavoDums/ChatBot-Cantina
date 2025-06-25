package com.cantinaChatBOT;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication //Indica que essa é a classe principal do sistema
public class ConsoleApp {
    public static void main(String[] args) {
        SpringApplication.run(ConsoleApp.class, args); //Codigo para iniciar a aplicação Spring Boot
    }
}