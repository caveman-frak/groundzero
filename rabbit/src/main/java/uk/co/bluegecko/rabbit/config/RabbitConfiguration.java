package uk.co.bluegecko.rabbit.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.rabbitmq.stream.Environment;
import com.rabbitmq.stream.StreamCreator;
import java.util.UUID;
import java.util.function.Consumer;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.config.ContainerCustomizer;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory.ConfirmType;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionNameStrategy;
import org.springframework.amqp.rabbit.connection.SimplePropertyValueConnectionNameStrategy;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.listener.AbstractMessageListenerContainer;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.amqp.CachingConnectionFactoryConfigurer;
import org.springframework.boot.autoconfigure.amqp.RabbitProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.rabbit.stream.support.StreamAdmin;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class RabbitConfiguration {

	@Bean
	public RabbitAdmin rabbitAdmin(ConnectionFactory connectionFactory) {
		return new RabbitAdmin(connectionFactory);
	}

	@Bean
	public Consumer<StreamCreator> defaultConsumer() {
		return _ -> {
		};
	}

	@Bean
	public StreamAdmin streamAdmin(Environment environment, Consumer<StreamCreator> creatorConsumer) {
		return new StreamAdmin(environment, creatorConsumer);
	}

	@Bean
	public ConnectionNameStrategy connectionNamingStrategy() {
		return new SimplePropertyValueConnectionNameStrategy("spring.application.name");
	}

	@Bean
	public ContainerCustomizer<? extends AbstractMessageListenerContainer> containerCustomizer(
			@Value("${spring.application.name}") String applicationName) {
		return c -> c.setConsumerTagStrategy(q -> "%s_%s_%s".formatted(applicationName, q, UUID.randomUUID()));
	}

	@Bean
	public CachingConnectionFactoryConfigurer connectionFactoryConfigurer(RabbitProperties properties) {
		return new CachingConnectionFactoryConfigurer(properties) {
			@Override
			public void configure(CachingConnectionFactory connectionFactory, RabbitProperties rabbitProperties) {
				super.configure(connectionFactory, rabbitProperties);
				connectionFactory.setPublisherReturns(true);
				connectionFactory.setPublisherConfirmType(ConfirmType.CORRELATED);
			}
		};
	}

	@Bean
	public ObjectMapper objectMapper() {
		return new ObjectMapper()
				.registerModule(new JavaTimeModule())
				.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
	}

	@Bean
	public MessageConverter messageConverter(ObjectMapper objectMapper) {
		return new Jackson2JsonMessageConverter(objectMapper);
	}

}