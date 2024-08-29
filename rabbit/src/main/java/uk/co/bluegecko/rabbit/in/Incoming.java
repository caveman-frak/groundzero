package uk.co.bluegecko.rabbit.in;

import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import uk.co.bluegecko.rabbit.config.ConfigurationPackage;

@EnableRabbit
@SpringBootApplication(scanBasePackageClasses = {Incoming.class, ConfigurationPackage.class})
public class Incoming {

	public static void main(String[] args) {
		new SpringApplicationBuilder(Incoming.class)
				.properties("spring.application.name=rabbit-incoming").run(args);
	}

}