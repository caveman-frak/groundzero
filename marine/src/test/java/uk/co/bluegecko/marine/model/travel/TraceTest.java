package uk.co.bluegecko.marine.model.travel;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import uk.co.bluegecko.marine.model.AbstractTest;
import uk.co.bluegecko.marine.model.compass.Coordinate;

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

		assertThat(trace.getCoordinate()).isEqualTo(new Coordinate(0.0, 0.0));
		assertThat(trace.getBearing()).isNull();
		assertThat(trace.getSpeed()).isNull();
		assertThat(trace.getRateOfTurn()).isNull();
	}

}