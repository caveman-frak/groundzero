package uk.co.bluegecko.marine.model;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.offset;
import static org.assertj.core.api.InstanceOfAssertFactories.DOUBLE;

import java.awt.geom.Point2D;
import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class TrackTest extends AbstractTest {

	private SimpleCourse course;
	private List<Trace> traces;

	@BeforeEach
	void setUp() {
		setUpClock();
		course = new SimpleCourse(clock, 0.03, new Point2D.Double(0.0001, 0.0001));
		Trace trace = Trace.builder()
				.vesselId(new UUID(0, 0))
				.timestamp(clock.instant())
				.latitude(0)
				.longitude(0)
				.build();

		traces = new ArrayList<>();
		for (int i = 0; i < 10; i++) {
			clock.tick(Duration.ofMinutes(10));
			trace = course.next().apply(trace);
			traces.add(trace);
		}
	}

	@Test
	void traces() {
		assertThat(traces).hasSize(10).extracting(Trace::getLongitude)
				.element(9, DOUBLE).isCloseTo(0.018, offset(0.000001));
		assertThat(clock.instant()).isEqualTo(Instant.ofEpochMilli(1577886000000L));
		System.out.printf("%s", traces.getLast());
	}

	@Test
	void tracks() {
		assertThat(Track.tracks(traces)).hasSize(3).extracting(Track::getH3Cell)
				.containsExactly(610049622659825663L, 610049622659825663L, 610049360280944639L);
		assertThat(Track.tracks(traces)).extracting(Track::getEpochHours)
				.containsExactly(438300L, 438301L, 438301L);
		assertThat(Track.tracks(traces)).extracting(Track::getTraces).extracting(Collection::size)
				.containsExactly(5, 1, 4);
	}

}