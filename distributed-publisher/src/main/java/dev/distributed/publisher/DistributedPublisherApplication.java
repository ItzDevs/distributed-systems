package dev.distributed.publisher;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Configuration;

@SpringBootApplication
@Configuration("configuration.xml")
public class DistributedPublisherApplication {

	public static void main(String[] args) {
		SpringApplication.run(DistributedPublisherApplication.class, args);
	}

}
