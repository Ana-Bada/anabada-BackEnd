package com.anabada.anabadaBackend;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
public class AnabadaBackendApplication {

    public static void main(String[] args) {SpringApplication.run(AnabadaBackendApplication.class, args);
        //
    }
}