package io.github.renatoconrado.libraryapi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@EnableJpaAuditing
@RestController
public @SpringBootApplication class Application {
    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
        System.out.println("Initialized");
    }

    @GetMapping private String initialized() {
        return "Application Initialized";
    }
}
