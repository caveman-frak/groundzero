package uk.co.bluegecko.marine.model.travel;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import uk.co.bluegecko.marine.model.AbstractTest;

class TraceTest extends AbstractTest {

	@BeforeEach
	void setUp() {
		setUpClock();
	}

	@Test
	void initial() {
		Trace trace = Trace.builder()
				.vesselId(new UUID(0, 0))
				.timestamp(clock.instant())
				.build();

		assertThat(trace.getLatitude()).isEqualTo(0.0);
		assertThat(trace.getLongitude()).isEqualTo(0.0);
		assertThat(trace.getBearing()).isNull();
		assertThat(trace.getSpeed()).isNull();
		assertThat(trace.getRateOfTurn()).isNull();
	}

}