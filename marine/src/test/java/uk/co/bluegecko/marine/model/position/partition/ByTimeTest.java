package uk.co.bluegecko.marine.model.position.partition;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.Instant;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import uk.co.bluegecko.marine.model.AbstractTest;

class ByTimeTest extends AbstractTest {

	ByTime time;

	@BeforeEach
	void setUp() {
		setUpClock();
		Resolution resolution = Resolution.MEDIUM;
		time = new TimePartition(resolution, resolution.epochIntervals(clock.instant()));
	}

	@Test
	void time() {
		assertThat(time.epochIntervals()).isEqualTo(438300L);
		assertThat(time.resolution()).isEqualTo(Resolution.MEDIUM);
	}

	@Test
	void start() {
		assertThat(time.start()).isEqualTo("2020-01-01T12:00:00Z");
	}

	@Test
	void end() {
		assertThat(time.end()).isEqualTo("2020-01-01T13:00:00Z");
	}

	@Test
	void withinTrue() {
		assertThat(time.isWithin(Instant.parse("2020-01-01T12:30:00Z"))).isTrue();
	}

	@Test
	void withinBefore() {
		assertThat(time.isWithin(Instant.parse("2020-01-01T11:30:00Z"))).isFalse();
	}

	@Test
	void withinAfter() {
		assertThat(time.isWithin(Instant.parse("2020-01-01T13:30:00Z"))).isFalse();
	}

}