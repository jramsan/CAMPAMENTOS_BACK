package com.example.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.boot.autoconfigure.domain.EntityScan;

@SpringBootApplication
@ComponentScan(basePackages = {
        "com.example.demo",   // donde está la clase main
        "controller",
        "service",
        "repository",
        "entity",
        "exception",
        "DTO"
})
@EnableJpaRepositories(basePackages = { "repository" })
@EntityScan(basePackages = { "entity" })
public class CampamentoschatApplication {
    public static void main(String[] args) {
        // arrancar la aplicación
        SpringApplication.run(CampamentoschatApplication.class, args);
    }
}
