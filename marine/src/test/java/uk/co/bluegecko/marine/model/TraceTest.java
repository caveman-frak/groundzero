package uk.co.bluegecko.marine.model;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.Month;
import java.time.ZoneOffset;
import java.time.temporal.ChronoUnit;
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

		long epochHours = trace.getEpochHours();
		assertThat(epochHours).isEqualTo(438300);
		assertThat(Instant.EPOCH.plus(epochHours, ChronoUnit.HOURS)).isEqualTo(
				LocalDateTime.of(2020, Month.JANUARY, 1, 12, 0).toInstant(ZoneOffset.UTC));
		assertThat(Resolution.start(epochHours)).isEqualTo(
				LocalDateTime.of(2020, Month.JANUARY, 1, 12, 0).toInstant(ZoneOffset.UTC));
		assertThat(Resolution.end(epochHours)).isEqualTo(
				LocalDateTime.of(2020, Month.JANUARY, 1, 13, 0).toInstant(ZoneOffset.UTC));
		long h3Cell = trace.getH3Cell();
		assertThat(h3Cell).isEqualTo(610049622659825663L);
		assertThat(Resolution.H3_CORE.getResolution(h3Cell)).isEqualTo(7);
		assertThat(Resolution.H3_CORE.h3ToString(h3Cell)).isEqualTo("87754e64dffffff");
	}

}