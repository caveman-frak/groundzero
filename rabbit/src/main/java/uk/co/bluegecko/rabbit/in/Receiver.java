package uk.co.bluegecko.rabbit.in;

import com.rabbitmq.stream.OffsetSpecification;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.Timer;
import java.util.TimerTask;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitMessagingTemplate;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.rabbit.stream.listener.ConsumerCustomizer;
import org.springframework.rabbit.stream.producer.ProducerCustomizer;
import org.springframework.stereotype.Component;
import uk.co.bluegecko.marine.model.position.Trace;

@Slf4j
@RequiredArgsConstructor
@Component
public class Receiver {

	private final RabbitMessagingTemplate template;
	private final Timer timer;

	@RabbitListener(queues = "foo-queue")
	public void receiveMessageFoo(Trace trace, Message message) {
		log.info("Received from Foo <{}> {}", trace, message.getMessageProperties());
		reply("bar-exchange", "",
				"Vessel: %s sighted".formatted(trace.getVesselId()), Duration.ofSeconds(2));
	}

	@RabbitListener(queues = "bar1-queue")
	public void receiveMessageBar1(String message) {
		log.info("Received from Bar1 <{}>", message);
	}

	@RabbitListener(queues = "bar2-queue")
	public void receiveMessageBar2(String message) {
		log.info("Received from Bar2 <{}>", message);
	}

	@RabbitListener(queues = "bar3-stream", containerFactory = "streamListenerFactory")
	public void receiveMessageBar3(String message) {
		log.info("Received from Bar3 <{}>", message);
	}

	@RabbitListener(queues = "Dead Letter Queue")
	public void receiveMessageDLQ(Message message) {
		log.info("Received from DLQ <{}> with \n{}", new String(message.getBody(), StandardCharsets.UTF_8),
				message.getMessageProperties());
	}

	@RabbitListener(queues = "Dead Letter Queue")
	public void receiveMessageDLQ(Trace trace) {
		log.info("Resubmitting from DLQ <{}>", trace);
		reply("foo-exchange", "foo.bar.resub", trace, Duration.ofSeconds(2));
	}

	private void reply(String exchange, String routingKey, Object message, Duration delay) {
		timer.schedule(new ReplyTask(exchange, routingKey, message), delay.toMillis());
	}

	@RequiredArgsConstructor
	private class ReplyTask extends TimerTask {

		private final String exchange;
		private final String routingKey;
		private final Object message;

		@Override
		public void run() {
			log.info("Sending <{}> to {}", message, exchange);
			template.convertAndSend(exchange, routingKey, message);
		}

	}

	@Bean
	public static Timer timer() {
		return new Timer("reply-timer");
	}

	@Configuration
	public static class StreamConfiguration {

		@Bean
		@Primary
		public ConsumerCustomizer consumerCustomizer() {
			return (_, builder) -> builder.name("bar3-consumer")
					.offset(OffsetSpecification.first())
					.autoTrackingStrategy();
		}

		@Bean
		@Primary
		public ProducerCustomizer producerCustomizer() {
			return (_, builder) -> builder.name("bar3-producer");
		}

		@Bean
		@Primary
		public String streamName() {
			return "bar3-stream";
		}
	}

}