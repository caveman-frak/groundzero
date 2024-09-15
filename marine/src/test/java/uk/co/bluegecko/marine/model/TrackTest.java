package uk.co.bluegecko.marine.model;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.offset;
import static org.assertj.core.api.InstanceOfAssertFactories.DOUBLE;

import com.uber.h3core.H3Core;
import java.awt.geom.Point2D;
import java.io.IOException;
import java.time.Duration;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.Month;
import java.time.ZoneOffset;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import uk.co.bluegecko.marine.model.Resolution.Partition;

class TrackTest extends AbstractTest {

	private H3Core h3Core;
	private List<Trace> traces;

	@BeforeEach
	void setUp() throws IOException {
		setUpClock();
		h3Core = H3Core.newInstance();
		SimpleCourse course = new SimpleCourse(clock, 0.03, new Point2D.Double(0.0001, 0.0001));
		Trace trace = Trace.builder()
				.vesselId(new UUID(0, 0))
				.timestamp(clock.instant())
				.latitude(0)
				.longitude(0)
				.build();

		traces = new ArrayList<>(List.of(trace));
		for (int i = 0; i < 10; i++) {
			clock.tick(Duration.ofMinutes(10));
			trace = course.next().apply(trace);
			traces.add(trace);
		}
	}

	@Test
	void traces() {
		assertThat(traces).hasSize(11).extracting(Trace::getLongitude)
				.element(10, DOUBLE).isCloseTo(0.018, offset(0.000001));
		assertThat(clock.instant()).isEqualTo(Instant.ofEpochMilli(1577886000000L));
	}

	@Test
	void calcPartition() {
		Resolution resolution = Resolution.MEDIUM;
		Partition partition = resolution.partition(h3Core, traces.get(1));
		long epochIntervals = partition.epochIntervals();
		assertThat(Instant.EPOCH.plus(epochIntervals, ChronoUnit.HOURS)).isEqualTo(
				LocalDateTime.of(2020, Month.JANUARY, 1, 12, 0).toInstant(ZoneOffset.UTC));
		assertThat(resolution.start(epochIntervals)).isEqualTo(
				LocalDateTime.of(2020, Month.JANUARY, 1, 12, 0).toInstant(ZoneOffset.UTC));
		assertThat(resolution.end(epochIntervals)).isEqualTo(
				LocalDateTime.of(2020, Month.JANUARY, 1, 13, 0).toInstant(ZoneOffset.UTC));
		long h3Cell = partition.h3Cell();
		assertThat(h3Cell).isEqualTo(610049622659825663L);
		assertThat(h3Core.getResolution(h3Cell)).isEqualTo(7);
		assertThat(h3Core.h3ToString(h3Cell)).isEqualTo("87754e64dffffff");
	}

	@Test
	void tracksFine() {
		List<Track> tracks = Track.tracks(h3Core, Resolution.FINE, traces);
		assertThat(tracks).hasSize(11)
				.extracting(Track::getH3Cell)
				.containsExactlyInAnyOrder(619056821909585919L, 619056821901197311L, 619056559524937727L,
						619056821840379903L, 619056559532277759L, 619056821900148735L, 619056559523889151L,
						619056821900935167L, 619056822403727359L, 619056821909061631L, 619056821910110207L);
		assertThat(tracks).extracting(Track::getEpochIntervals)
				.containsExactlyInAnyOrder(1753204L, 1753202L, 1753205L, 1753200L, 1753206L, 1753201L, 1753204L,
						1753200L, 1753206L, 1753202L, 1753203L);
		assertThat(tracks).extracting(Track::getTraces).extracting(Collection::size)
				.containsExactly(1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1);
		assertThat(tracks).extracting(Track::getEarliest).extracting(Instant::toString)
				.containsExactlyInAnyOrder("2020-01-01T13:00:00Z", "2020-01-01T12:30:00Z", "2020-01-01T13:20:00Z",
						"2020-01-01T12:00:00Z", "2020-01-01T13:40:00Z", "2020-01-01T12:20:00Z", "2020-01-01T13:10:00Z",
						"2020-01-01T12:10:00Z", "2020-01-01T13:30:00Z", "2020-01-01T12:40:00Z", "2020-01-01T12:50:00Z");
	}

	@Test
	void tracksMedium() {
		List<Track> tracks = Track.tracks(h3Core, Resolution.MEDIUM, traces);
		assertThat(tracks).hasSize(3)
				.extracting(Track::getH3Cell)
				.containsExactlyInAnyOrder(610049622659825663L, 610049622659825663L, 610049360280944639L);
		assertThat(tracks).extracting(Track::getEpochIntervals)
				.containsExactlyInAnyOrder(438300L, 438301L, 438301L);
		assertThat(tracks).extracting(Track::getTraces).extracting(Collection::size)
				.containsExactlyInAnyOrder(6, 1, 4);
		assertThat(tracks).extracting(Track::getEarliest).extracting(Instant::toString)
				.containsExactlyInAnyOrder("2020-01-01T12:00:00Z", "2020-01-01T13:00:00Z", "2020-01-01T13:10:00Z");
	}

	@Test
	void tracksCoarse() {
		List<Track> tracks = Track.tracks(h3Core, Resolution.COARSE, traces);
		assertThat(tracks).hasSize(1)
				.extracting(Track::getH3Cell)
				.containsExactly(601042424243945471L);
		assertThat(tracks).extracting(Track::getEpochIntervals)
				.containsExactly(73050L);
		assertThat(tracks).extracting(Track::getTraces).extracting(Collection::size)
				.containsExactly(11);
		assertThat(tracks).extracting(Track::getEarliest).extracting(Instant::toString)
				.containsExactly("2020-01-01T12:00:00Z");
	}

}