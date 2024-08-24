package uk.co.bluegecko.rabbit.out;

import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.annotation.Import;
import uk.co.bluegecko.rabbit.config.RabbitConfiguration;
import uk.co.bluegecko.rabbit.config.RabbitDefinition;

@Import({RabbitConfiguration.class, RabbitDefinition.class})
@SpringBootApplication
public class Outgoing {

	public static void main(String[] args) {
		new SpringApplicationBuilder(Outgoing.class).web(WebApplicationType.NONE).run(args).close();
	}

}