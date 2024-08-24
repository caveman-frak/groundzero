package uk.co.bluegecko.rabbit.in;

import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;
import uk.co.bluegecko.rabbit.config.RabbitConfiguration;
import uk.co.bluegecko.rabbit.config.RabbitDefinition;

@EnableRabbit
@Import({RabbitConfiguration.class, RabbitDefinition.class})
@SpringBootApplication
public class Incoming {

	public static void main(String[] args) {
		SpringApplication.run(Incoming.class, args);
	}

}