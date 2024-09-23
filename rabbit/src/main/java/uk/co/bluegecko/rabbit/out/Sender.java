package uk.co.bluegecko.rabbit.out;

import static si.uom.NonSI.KNOT;
import static uk.co.bluegecko.marine.model.position.Trace.DEGREE_PER_MINUTE;

import java.time.Clock;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.MessagePostProcessor;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.connection.CorrelationData.Confirm;
import org.springframework.amqp.rabbit.core.RabbitMessagingTemplate;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.boot.CommandLineRunner;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import tech.units.indriya.quantity.Quantities;
import uk.co.bluegecko.marine.model.compass.Bearing;
import uk.co.bluegecko.marine.model.position.Trace;

@Slf4j
@Component
@RequiredArgsConstructor
public class Sender implements CommandLineRunner {

	private final RabbitMessagingTemplate messagingTemplate;

	protected RabbitTemplate rabbitTemplate() {
		return messagingTemplate.getRabbitTemplate();
	}

	@Override
	public void run(String... args) throws InterruptedException {
		List<CompletableFuture<?>> futures = new ArrayList<>();
		Trace trace = Trace.builder()
				.vesselId(new UUID(0, 10))
				.timestamp(Clock.systemUTC().instant())
				.coordinates(40.0, -20.0)
				.bearing(Bearing.asDegrees(30.0))
				.speed(Quantities.getQuantity(10.0, KNOT))
				.build();
		futures.add(confirm(send(trace)));
		Thread.sleep(Duration.ofSeconds(15));
		futures.add(confirm(send(trace.withRateOfTurn(Quantities.getQuantity(-150, DEGREE_PER_MINUTE)))));
		CompletableFuture.allOf(futures.toArray(CompletableFuture[]::new)).join();
	}

	@Async
	protected CompletableFuture<Confirm> confirm(CorrelatedTrace data) {
		return data.getFuture().whenComplete((confirm, ex) -> {
			String id = data.getId();
			if (ex != null) {
				log.error("Unable to send {} because {}", id, ex.getMessage(), ex);
			} else if (confirm.isAck()) {
				log.info("Successfully send message {} :: <{}>", id, data.getTrace());
			} else {
				log.warn("Sending {} failed because {}", id, confirm.getReason());
			}
		});
	}

	private CorrelatedTrace send(Trace trace) {
		log.info("Sending message...{}", trace);
		CorrelatedTrace correlationData = new CorrelatedTrace(trace);
		rabbitTemplate().convertAndSend("foo-exchange", "foo.bar.baz", trace,
				addHeaders(Map.of("instance", "client")), correlationData);
		return correlationData;
	}

	private MessagePostProcessor addHeaders(Map<String, String> headers) {
		return m -> {
			m.getMessageProperties().getHeaders().putAll(headers);
			return m;
		};
	}

	@Getter
	protected static class CorrelatedTrace extends CorrelationData {

		private final Trace trace;

		private CorrelatedTrace(Trace trace) {
			super(UUID.randomUUID().toString());
			this.trace = trace;
		}

	}

}