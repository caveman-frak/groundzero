package uk.co.bluegecko.rabbit.out;

import java.time.Clock;
import java.time.Duration;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ExecutionException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.MessagePostProcessor;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitMessagingTemplate;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import uk.co.bluegecko.rabbit.model.Trace;

@Slf4j
@Component
@RequiredArgsConstructor
public class Sender implements CommandLineRunner {

	private final RabbitMessagingTemplate messagingTemplate;

	protected RabbitTemplate rabbitTemplate() {
		return messagingTemplate.getRabbitTemplate();
	}

	@Override
	public void run(String... args) throws InterruptedException, ExecutionException {
		Trace trace = Trace.builder().vesselId(new UUID(0, 10)).timestamp(Clock.systemUTC().instant())
				.latitude(40.0).longitude(-20.0).bearing(30.0).speed(10.0).rateOfTurn(0.0).build();
		send(trace);
		Thread.sleep(Duration.ofSeconds(5));
		send(trace.withRateOfTurn(-1.5));
	}

	private void send(Trace trace) throws ExecutionException, InterruptedException {
		log.info("Sending message...{}", trace);
		CorrelationData correlationData = new CorrelationData(UUID.randomUUID().toString());
		rabbitTemplate().convertAndSend("foo-exchange", "foo.bar.baz", trace,
				addHeaders(Map.of("instance", "client")), correlationData);
		log.info("Sent message, {}", correlationData.getFuture().get());
	}

	private MessagePostProcessor addHeaders(Map<String, String> headers) {
		return m -> {
			m.getMessageProperties().getHeaders().putAll(headers);
			return m;
		};
	}

}