package uk.co.bluegecko.rabbit.in;

import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import uk.co.bluegecko.rabbit.common.CommonPackage;

@EnableRabbit
@SpringBootApplication(scanBasePackageClasses = {Incoming.class, CommonPackage.class})
public class Incoming {

	public static void main(String[] args) {
		new SpringApplicationBuilder(Incoming.class)
				.properties("spring.application.name=rabbit-incoming").run(args);
	}

}