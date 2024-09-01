package uk.co.bluegecko.marine.model;

import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

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
				.latitude(0)
				.longitude(0)
				.build();
	}

}