package uk.co.bluegecko.rabbit.in;

import java.time.Duration;
import java.util.Timer;
import java.util.TimerTask;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitMessagingTemplate;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import uk.co.bluegecko.rabbit.model.Trace;

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
				"Vessel: " + trace.getVesselId().toString() + " sighted", Duration.ofSeconds(2));
	}

	@RabbitListener(queues = "bar1-queue")
	public void receiveMessageBar1(String message) {
		log.info("Received from Bar1 <{}>", message);
	}

	@RabbitListener(queues = "bar2-queue")
	public void receiveMessageBar2(String message) {
		log.info("Received from Bar2 <{}>", message);
	}

	private void reply(String exchange, String routingKey, String message, Duration delay) {
		timer.schedule(new ReplyTask(exchange, routingKey, message), delay.toMillis());
	}

	@RequiredArgsConstructor
	private class ReplyTask extends TimerTask {

		private final String exchange;
		private final String routingKey;
		private final String message;

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

}