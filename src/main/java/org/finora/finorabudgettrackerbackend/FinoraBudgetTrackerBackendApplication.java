package org.finora.finorabudgettrackerbackend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class FinoraBudgetTrackerBackendApplication {

    public static void main(String[] args) {

        SpringApplication.run(FinoraBudgetTrackerBackendApplication.class, args);
    }

}

