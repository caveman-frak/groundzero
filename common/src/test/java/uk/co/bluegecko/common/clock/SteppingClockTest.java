package uk.co.bluegecko.common.clock;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.Month;
import java.time.Period;
import java.time.ZoneOffset;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class SteppingClockTest {

	LocalDate date;
	LocalTime time;

	@BeforeEach
	void setUp() {
		date = LocalDate.of(2020, Month.JANUARY, 5);
		time = LocalTime.of(12, 35);
	}

	@Test
	void constructor() {
		SteppingClock clock = SteppingClock.stepping(date, time);

		assertThat(clock.getZone()).isEqualTo(ZoneOffset.UTC);
		assertThat(clock.millis()).isEqualTo(1578227700000L);
		assertThat(clock.instant()).isEqualTo("2020-01-05T12:35:00Z");
	}

	@Test
	void copyConstructor() {
		SteppingClock base = SteppingClock.stepping(date, time);
		SteppingClock clock = SteppingClock.stepping(base);
		base.tick();

		assertThat(clock.getZone()).isEqualTo(ZoneOffset.UTC);
		assertThat(clock.millis()).isEqualTo(1578227700000L);
		assertThat(clock.instant()).isEqualTo("2020-01-05T12:35:00Z");
		assertThat(base.instant()).isEqualTo("2020-01-05T12:35:01Z");
	}

	@Test
	void tick() {
		SteppingClock clock = SteppingClock.stepping(date, time);
		clock.tick();

		assertThat(clock.instant()).isEqualTo("2020-01-05T12:35:01Z");
	}

	@Test
	void tick30Minutes() {
		SteppingClock clock = SteppingClock.stepping(date, time);
		clock.tick(Duration.ofMinutes(30));

		assertThat(clock.instant()).isEqualTo("2020-01-05T13:05:00Z");
	}

	@Test
	void tock() {
		SteppingClock clock = SteppingClock.stepping(date, time);
		clock.tock();

		assertThat(clock.instant()).isEqualTo("2020-01-06T12:35:00Z");
	}

	@Test
	void tock8Days() {
		SteppingClock clock = SteppingClock.stepping(date, time);
		clock.tock(Period.ofDays(8));

		assertThat(clock.instant()).isEqualTo("2020-01-13T12:35:00Z");
	}

}