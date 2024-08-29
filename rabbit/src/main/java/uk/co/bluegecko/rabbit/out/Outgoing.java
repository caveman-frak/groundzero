package uk.co.bluegecko.rabbit.out;

import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.scheduling.annotation.EnableAsync;
import uk.co.bluegecko.rabbit.common.CommonPackage;

@EnableAsync
@SpringBootApplication(scanBasePackageClasses = {Outgoing.class, CommonPackage.class})
public class Outgoing {

	public static void main(String[] args) {
		new SpringApplicationBuilder(Outgoing.class)
				.web(WebApplicationType.NONE)
				.properties("spring.application.name=rabbit-outgoing")
				.run(args).close();
	}

}