package uk.co.bluegecko.marine.model.travel;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.offset;
import static si.uom.NonSI.KNOT;
import static systems.uom.ucum.UCUM.DEGREE;
import static uk.co.bluegecko.common.clock.SteppingClock.stepping;

import java.awt.Point;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.Month;
import java.util.UUID;
import javax.measure.Quantity;
import javax.measure.quantity.Speed;
import org.assertj.core.data.Offset;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import tech.units.indriya.quantity.Quantities;
import uk.co.bluegecko.common.clock.SteppingClock;
import uk.co.bluegecko.marine.model.compass.Bearing;
import uk.co.bluegecko.marine.model.compass.Coordinate;
import uk.co.bluegecko.marine.model.compass.Latitude;
import uk.co.bluegecko.marine.model.compass.Longitude;

class SimpleCourseTest {

	private SteppingClock clock;
	private Quantity<Speed> speed;
	private Offset<Double> offset;

	@BeforeEach
	void setUp() {
		clock = stepping(LocalDate.of(2020, Month.JANUARY, 1), LocalTime.of(12, 0));
		speed = Quantities.getQuantity(1, KNOT);
		offset = offset(0.00000001);
	}

	@Test
	void bearing45() {
		SimpleCourse course = new SimpleCourse(clock, speed, new Point(1, 1));

		assertThat(course.bearing().to(DEGREE).getValue().doubleValue()).isEqualTo(45.0);
	}

	@Test
	void bearing135() {
		SimpleCourse course = new SimpleCourse(clock, speed, new Point(1, -1));

		assertThat(course.bearing().to(DEGREE).getValue().doubleValue()).isEqualTo(135.0);
	}

	@Test
	void bearing225() {
		SimpleCourse course = new SimpleCourse(clock, speed, new Point(-1, -1));

		assertThat(course.bearing().to(DEGREE).getValue().doubleValue()).isEqualTo(225.0);
	}

	@Test
	void bearing315() {
		SimpleCourse course = new SimpleCourse(clock, speed, new Point(-1, 1));

		assertThat(course.bearing().to(DEGREE).getValue().doubleValue()).isEqualTo(315.0);
	}

	@Test
	void next() {
		SimpleCourse course = new SimpleCourse(clock, speed, new Point(1, 1));
		Trace traceInitial = Trace.builder()
				.vesselId(new UUID(0, 0))
				.timestamp(clock.instant())
				.build();

		clock.tick();
		Trace traceOne = course.next().apply(traceInitial);
		Trace expectedOne = traceInitial.toBuilder()
				.timestamp(clock.instant())
				.coordinate(new Coordinate(Latitude.asDegrees(0.00000327143), Longitude.asDegrees(0.00000327143)))
				.speed(speed)
				.bearing(Bearing.asDegrees(45.0))
				.build();
		assertThat(traceOne).has(TraceConditions.isEqualsTo(expectedOne, offset));

		clock.tick();
		Trace traceTwo = course.next().apply(traceOne);
		Trace expectedTwo = traceOne.toBuilder()
				.timestamp(clock.instant())
				.coordinate(new Coordinate(Latitude.asDegrees(0.00000654286), Longitude.asDegrees(0.00000654286)))
				.build();
		assertThat(traceTwo).has(TraceConditions.isEqualsTo(expectedTwo, offset));
	}

}