package uk.co.bluegecko.rabbit.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.config.ContainerCustomizer;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionNameStrategy;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.listener.MessageListenerContainer;
import org.springframework.amqp.rabbit.listener.RabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.amqp.support.ConsumerTagStrategy;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

@Slf4j
@Configuration
public class RabbitConfiguration {

	@Bean
	public RabbitAdmin rabbitAdmin(ConnectionFactory connectionFactory) {
		return new RabbitAdmin(connectionFactory);
	}

	@Bean
	public RabbitListenerContainerFactory<?> listenerContainerFactory(ConnectionFactory connectionFactory) {
		SimpleRabbitListenerContainerFactory containerFactory = new SimpleRabbitListenerContainerFactory();
		containerFactory.setConnectionFactory(connectionFactory);
		return containerFactory;
	}

	@Bean
	public MessageListenerContainer messageListenerContainer(
			RabbitListenerContainerFactory<?> listenerContainerFactory) {
		return listenerContainerFactory.createListenerContainer();
	}

	@Bean
	public ConnectionNameStrategy connectionNamingStrategy() {
		return new GroundZeroConnectionNamingStrategy();
	}

	@Bean
	public ConsumerTagStrategy consumerTagStrategy(Environment env) {
		return new GroundZeroConsumerTagStrategy(env);
	}

	@Bean
	public ContainerCustomizer<SimpleMessageListenerContainer> containerCustomizer(
			ConsumerTagStrategy consumerTagStrategy) {
		return c -> c.setConsumerTagStrategy(consumerTagStrategy);
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