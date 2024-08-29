package uk.co.bluegecko.rabbit.common.config;

import java.net.InetAddress;
import java.net.UnknownHostException;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.connection.SimplePropertyValueConnectionNameStrategy;

public class GroundZeroConnectionNamingStrategy extends SimplePropertyValueConnectionNameStrategy {

	public GroundZeroConnectionNamingStrategy() {
		super("spring.application.name");
	}

	@Override
	public String obtainNewConnectionName(ConnectionFactory connectionFactory) {
		String applicationName = super.obtainNewConnectionName(connectionFactory);
		String hostName;
		try {
			hostName = InetAddress.getLocalHost().getHostName();
		} catch (UnknownHostException e) {
			hostName = "unknown";
		}

		return "%s@%s".formatted(applicationName, hostName);
	}
}