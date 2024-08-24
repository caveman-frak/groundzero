package uk.co.bluegecko.rabbit.config;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.FanoutExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class RabbitDefinition {

	@Bean
	Queue queueFoo() {
		return new Queue("foo-queue", false);
	}

	@Bean
	Queue queueBar1() {
		return new Queue("bar1-queue", false);
	}

	@Bean
	Queue queueBar2() {
		return new Queue("bar2-queue", false);
	}

	@Bean
	TopicExchange exchangeFoo() {
		return new TopicExchange("foo-exchange");
	}

	@Bean
	FanoutExchange exchangeBar() {
		return new FanoutExchange("bar-exchange", false, false);
	}

	@Bean
	Binding bindingFoo(Queue queueFoo, TopicExchange exchangeFoo) {
		return BindingBuilder.bind(queueFoo).to(exchangeFoo).with("foo.bar.#");
	}

	@Bean
	Binding bindingBar1(Queue queueBar1, FanoutExchange exchangeBar) {
		return BindingBuilder.bind(queueBar1).to(exchangeBar);
	}

	@Bean
	Binding bindingBar2(Queue queueBar2, FanoutExchange exchangeBar) {
		return BindingBuilder.bind(queueBar2).to(exchangeBar);
	}

}