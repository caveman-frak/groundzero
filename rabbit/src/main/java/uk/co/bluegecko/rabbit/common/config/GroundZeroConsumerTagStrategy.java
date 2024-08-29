package uk.co.bluegecko.rabbit.common.config;

import java.util.Base64;
import java.util.Random;
import java.util.random.RandomGenerator;
import org.springframework.amqp.support.ConsumerTagStrategy;
import org.springframework.core.env.Environment;

public class GroundZeroConsumerTagStrategy implements ConsumerTagStrategy {

	private final Base64.Encoder encoder = Base64.getMimeEncoder();
	private final RandomGenerator random = new Random();
	private final String applicationName;

	public GroundZeroConsumerTagStrategy(Environment environment) {
		applicationName = environment.getProperty("spring.application.name", "unknown");
	}

	@Override
	public String createConsumerTag(String queue) {
		return "%s@%s#%s".formatted(applicationName, queue, generateIdentifier());
	}

	private String generateIdentifier() {
		byte[] bytes = new byte[9];
		random.nextBytes(bytes);
		return encoder.encodeToString(bytes);
	}

}