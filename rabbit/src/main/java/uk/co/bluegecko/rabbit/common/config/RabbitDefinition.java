package uk.co.bluegecko.rabbit.common.config;

import com.rabbitmq.stream.ByteCapacity;
import java.time.Duration;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.ExchangeBuilder;
import org.springframework.amqp.core.FanoutExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.QueueBuilder;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class RabbitDefinition {

	private static final String DEAD_LETTER_QUEUE = "Dead Letter Queue";
	private static final String DEAD_LETTER_EXCHANGE = "Dead Letter Exchange";

	@Bean
	Queue deadLetterQueue() {
		return QueueBuilder.durable(DEAD_LETTER_QUEUE).build();
	}

	@Bean
	FanoutExchange deadLetterExchange() {
		return ExchangeBuilder.fanoutExchange(DEAD_LETTER_EXCHANGE).durable(true).internal().build();
	}

	@Bean
	Binding deadLetterBinding(Queue deadLetterQueue, FanoutExchange deadLetterExchange) {
		return BindingBuilder.bind(deadLetterQueue).to(deadLetterExchange);
	}

	@Bean
	TopicExchange exchangeFoo() {
		return ExchangeBuilder.topicExchange("foo-exchange").durable(false).build();
	}

	@Bean
	FanoutExchange exchangeBar() {
		return ExchangeBuilder.fanoutExchange("bar-exchange").durable(false).build();
	}

	@Bean
	Queue queueFoo() {
		return QueueBuilder.durable("foo-queue")
				.quorum()
				.deadLetterExchange(DEAD_LETTER_EXCHANGE)
				.ttl((int) Duration.ofMinutes(1).toMillis())
				.build();
	}

	@Bean
	Binding bindingFoo(Queue queueFoo, TopicExchange exchangeFoo) {
		return BindingBuilder.bind(queueFoo).to(exchangeFoo).with("foo.bar.#");
	}

	@Bean
	Queue queueBar1() {
		return QueueBuilder.nonDurable("bar1-queue")
				.deadLetterExchange(DEAD_LETTER_EXCHANGE)
				.build();
	}

	@Bean
	Binding bindingBar1(Queue queueBar1, FanoutExchange exchangeBar) {
		return BindingBuilder.bind(queueBar1).to(exchangeBar);
	}

	@Bean
	Queue queueBar2() {
		return QueueBuilder.nonDurable("bar2-queue")
				.deadLetterExchange(DEAD_LETTER_EXCHANGE)
				.build();
	}

	@Bean
	Binding bindingBar2(Queue queueBar2, FanoutExchange exchangeBar) {
		return BindingBuilder.bind(queueBar2).to(exchangeBar);
	}

	@Bean
	Queue streamBar3() {
		return QueueBuilder.durable("bar3-stream")
				.stream()
				.withArgument("x-max-age", String.format("%ds", Duration.ofDays(1).toSeconds()))
				.withArgument("x-max-length-bytes", ByteCapacity.MB(1).toBytes())
				.withArgument("x-stream-max-segment-size-bytes", ByteCapacity.kB(10).toBytes())
				.withArgument("x-queue-leader-location", "least-leaders")
				.build();
	}

	@Bean
	Binding bindingBar3(Queue streamBar3, FanoutExchange exchangeBar) {
		return BindingBuilder.bind(streamBar3).to(exchangeBar);
	}

}