package com.FinalBoss.GestionGastosPersonales;

import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ControlGastosApplication {

    public static void main(String[] args) {
        Dotenv dotenv = Dotenv.configure().ignoreIfMissing().load();
        dotenv.entries().forEach(entrada -> System.setProperty(entrada.getKey(), entrada.getValue()));
        SpringApplication.run(ControlGastosApplication.class, args);
        System.out.println("API de Control de Gastos en ejecución");
    }
}
