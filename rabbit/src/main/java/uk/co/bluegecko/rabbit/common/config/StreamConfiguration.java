package uk.co.bluegecko.rabbit.common.config;

import com.rabbitmq.stream.Address;
import com.rabbitmq.stream.Environment;
import com.rabbitmq.stream.StreamCreator;
import java.util.function.Consumer;
import org.springframework.amqp.rabbit.config.ContainerCustomizer;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionNameStrategy;
import org.springframework.amqp.rabbit.listener.RabbitListenerContainerFactory;
import org.springframework.boot.autoconfigure.amqp.EnvironmentBuilderCustomizer;
import org.springframework.boot.autoconfigure.amqp.RabbitProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.rabbit.stream.config.StreamRabbitListenerContainerFactory;
import org.springframework.rabbit.stream.listener.ConsumerCustomizer;
import org.springframework.rabbit.stream.listener.StreamListenerContainer;
import org.springframework.rabbit.stream.producer.ProducerCustomizer;
import org.springframework.rabbit.stream.producer.RabbitStreamTemplate;
import org.springframework.rabbit.stream.support.StreamAdmin;

@Configuration
public class StreamConfiguration {

	@Bean
	public Consumer<StreamCreator> defaultCreatorConsumer() {
		return _ -> {
		};
	}

	@Bean
	public ConsumerCustomizer defaultConsumerCustomizer() {
		return (_, _) -> {
		};
	}

	@Bean
	public ProducerCustomizer defaultProducerCustomizer() {
		return (_, _) -> {
		};
	}

	@Bean
	public ContainerCustomizer<StreamListenerContainer> defaultContainerCustomizer() {
		return _ -> {
		};
	}

	@Bean
	public String defaultStreamName() {
		return "";
	}

	@Bean
	public StreamAdmin streamAdmin(Environment environment, Consumer<StreamCreator> creatorConsumer) {
		return new StreamAdmin(environment, creatorConsumer);
	}

	@Bean
	RabbitStreamTemplate streamTemplate(Environment env, ProducerCustomizer producerCustomizer, String streamName) {
		RabbitStreamTemplate template = new RabbitStreamTemplate(env, streamName);
		template.setProducerCustomizer(producerCustomizer);
		return template;
	}

	@Bean
	RabbitListenerContainerFactory<StreamListenerContainer> streamListenerFactory(Environment env,
			ConsumerCustomizer consumerCustomizer, ContainerCustomizer<StreamListenerContainer> containerCustomizer) {
		StreamRabbitListenerContainerFactory factory = new StreamRabbitListenerContainerFactory(env);
		factory.setConsumerCustomizer(consumerCustomizer);
		factory.setContainerCustomizer(containerCustomizer);
		return factory;
	}

	@Bean
	RabbitListenerContainerFactory<StreamListenerContainer> nativeStreamFactory(Environment env,
			ConsumerCustomizer consumerCustomizer) {
		StreamRabbitListenerContainerFactory factory = new StreamRabbitListenerContainerFactory(env);
		factory.setNativeListener(true);
		factory.setConsumerCustomizer(consumerCustomizer);
		return factory;
	}

	@Bean
	EnvironmentBuilderCustomizer environmentBuilderCustomizer(RabbitProperties rabbitProperties,
			ConnectionFactory connectionFactory, ConnectionNameStrategy connectionNameStrategy) {
		String host = rabbitProperties.getStream().getHost();
		int port = rabbitProperties.getStream().getPort();
		return builder -> builder
				.id(connectionNameStrategy.obtainNewConnectionName(connectionFactory))
				.host(host)
				.port(port)
				.addressResolver(_ -> new Address(host, port));
	}

}