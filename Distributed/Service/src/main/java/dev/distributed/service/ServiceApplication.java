package dev.distributed.service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@Configuration("configuration.xml") // Loading up the Bean configurations
@EnableJpaRepositories // JPA backing to the database.
@EntityScan("dev.distributed.service.entities") // Database entities.
public class ServiceApplication {

    // Default start of Spring applications.
    public static void main(String[] args) {
        SpringApplication.run(ServiceApplication.class, args);
    }

}
