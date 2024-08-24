package uk.co.bluegecko.rabbit.out;

import java.time.Clock;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import uk.co.bluegecko.rabbit.model.Trace;

@Slf4j
@Component
@RequiredArgsConstructor
public class Sender implements CommandLineRunner {

	private final RabbitTemplate rabbitTemplate;

	@Override
	public void run(String... args) throws Exception {
		Trace trace = Trace.builder().vesselId(new UUID(0, 10)).timestamp(Clock.systemUTC().instant())
				.latitude(40.0).longitude(-20.0).bearing(30.0).speed(10.0).rateOfTurn(0.0).build();
		log.info("Sending message...{}", trace);
		rabbitTemplate.convertAndSend("foo-exchange", "foo.bar.baz", trace);
	}

}