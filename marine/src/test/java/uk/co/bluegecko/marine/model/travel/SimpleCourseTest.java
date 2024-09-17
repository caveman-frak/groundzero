package uk.co.bluegecko.marine.model.travel;

import static org.assertj.core.api.Assertions.assertThat;
import static uk.co.bluegecko.common.clock.SteppingClock.stepping;

import java.awt.Point;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.Month;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import uk.co.bluegecko.common.clock.SteppingClock;

class SimpleCourseTest {

	private SteppingClock clock;

	@BeforeEach
	void setUp() {
		clock = stepping(LocalDate.of(2020, Month.JANUARY, 1), LocalTime.of(12, 0));
	}

	@Test
	void bearing45() {
		SimpleCourse course = new SimpleCourse(clock, 1.0, new Point(1, 1));

		assertThat(course.getBearing()).isEqualTo(45.0);
	}

	@Test
	void bearing135() {
		SimpleCourse course = new SimpleCourse(clock, 1.0, new Point(1, -1));

		assertThat(course.getBearing()).isEqualTo(135.0);
	}

	@Test
	void bearing225() {
		SimpleCourse course = new SimpleCourse(clock, 1.0, new Point(-1, -1));

		assertThat(course.getBearing()).isEqualTo(225.0);
	}

	@Test
	void bearing315() {
		SimpleCourse course = new SimpleCourse(clock, 1.0, new Point(-1, 1));

		assertThat(course.getBearing()).isEqualTo(315.0);
	}

	@Test
	void next() {
		SimpleCourse course = new SimpleCourse(clock, 1.0, new Point(1, 1));
		Trace traceInitial = Trace.builder()
				.vesselId(new UUID(0, 0))
				.timestamp(clock.instant())
				.build();

		clock.tick();
		Trace traceOne = course.next().apply(traceInitial);
		assertThat(traceOne).isEqualTo(traceInitial.toBuilder()
				.timestamp(clock.instant())
				.coordinates(1.0, 1.0)
				.speed(1.0)
				.bearing(45.0)
				.build());

		clock.tick();
		Trace traceTwo = course.next().apply(traceOne);
		assertThat(traceTwo).isEqualTo(traceOne.toBuilder()
				.timestamp(clock.instant())
				.coordinates(2.0, 2.0)
				.build());
	}

}