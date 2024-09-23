package uk.co.bluegecko.marine.model.position;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.offset;
import static org.assertj.core.api.InstanceOfAssertFactories.DOUBLE;
import static si.uom.NonSI.KNOT;

import com.uber.h3core.AreaUnit;
import com.uber.h3core.H3Core;
import java.awt.geom.Point2D;
import java.io.IOException;
import java.time.Duration;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.Month;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.locationtech.spatial4j.context.SpatialContext;
import org.locationtech.spatial4j.context.SpatialContextFactory;
import tech.units.indriya.quantity.Quantities;
import uk.co.bluegecko.marine.model.AbstractTest;
import uk.co.bluegecko.marine.model.position.Resolution.Partition;

class TrackTest extends AbstractTest {

	private H3Core h3Core;
	private List<Trace> traces;

	@BeforeEach
	void setUp() throws IOException {
		setUpClock();
		h3Core = H3Core.newInstance();
		SpatialContext ctx = SpatialContextFactory.makeSpatialContext(Map.of("geo", "true"), null);
		SimpleCourse course = new SimpleCourse(new Calculator(ctx), clock, Quantities.getQuantity(10, KNOT),
				new Point2D.Double(0.0001, 0.0001));
		Trace trace = Trace.builder()
				.vesselId(new UUID(0, 0))
				.timestamp(clock.instant())
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
		assertThat(traces).hasSize(11).extracting(Trace::latitude)
				.element(10, DOUBLE).isCloseTo(0.1962858, offset(0.000001));
		assertThat(traces).hasSize(11).extracting(Trace::longitude)
				.element(10, DOUBLE).isCloseTo(0.1962863, offset(0.000001));
		assertThat(clock.instant()).isEqualTo(Instant.ofEpochMilli(1577886000000L))
				.isBetween(LocalDateTime.of(2020, Month.JANUARY, 1, 13, 0).toInstant(ZoneOffset.UTC),
						LocalDateTime.of(2020, Month.JANUARY, 1, 14, 0).toInstant(ZoneOffset.UTC));
	}

	@Test
	void calcPartition() {
		Resolution resolution = Resolution.MEDIUM;
		Partition partition = resolution.partition(h3Core, traces.get(1));
		long epochIntervals = partition.epochIntervals();
		assertThat(Instant.EPOCH.plus(resolution.duration().multipliedBy(epochIntervals))).isEqualTo(
				LocalDateTime.of(2020, Month.JANUARY, 1, 12, 0).toInstant(ZoneOffset.UTC));
		assertThat(resolution.start(epochIntervals)).isEqualTo(
				LocalDateTime.of(2020, Month.JANUARY, 1, 12, 0).toInstant(ZoneOffset.UTC));
		assertThat(resolution.end(epochIntervals)).isEqualTo(
				LocalDateTime.of(2020, Month.JANUARY, 1, 13, 0).toInstant(ZoneOffset.UTC));
		assertThat(resolution.duration()).isEqualTo(Duration.ofHours(1));
		long h3Cell = partition.h3Cell();
		assertThat(h3Cell).isEqualTo(596538831059025919L);
		assertThat(h3Core.getResolution(h3Cell)).isEqualTo(4);
		assertThat(h3Core.h3ToString(h3Cell)).isEqualTo("84754e7ffffffff");
		assertThat(h3Core.cellArea(h3Cell, AreaUnit.rads2)).isCloseTo(0.0000324, offset(0.000001));
		assertThat(h3Core.cellArea(h3Cell, AreaUnit.km2)).isCloseTo(1311.7533914, offset(0.000001));
		assertThat(h3Core.cellToBoundary(h3Cell)).hasSize(6);
		assertThat(h3Core.gridDisk(h3Cell, 1)).hasSize(7).contains(h3Cell)
				.contains(596538831059025919L, 596538822469091327L, 596539879031046143L, 596538581950922751L,
						596538564771053567L, 596538813879156735L, 596538805289222143L)
				.extracting(l -> h3Core.h3ToString(l)).contains("84754a9ffffffff")
				.contains("84754e7ffffffff", "84754e5ffffffff", "84755dbffffffff", "84754adffffffff", "84754a9ffffffff",
						"84754e3ffffffff", "84754e1ffffffff");

	}

	@Test
	void tracksFine() {
		List<Track> tracks = Track.tracks(h3Core, Resolution.FINE, traces);
		assertThat(tracks).hasSize(11)
				.extracting(Track::getH3Cell)
				.containsOnly(605546023066009599L, 605546011120631807L, 605546023602880511L, 605546007630970879L,
						605546007094099967L, 605546011523284991L)
				.extracting(l -> h3Core.h3ToString(l))
				.containsOnly("86754e64fffffff", "86754e66fffffff", "86754e39fffffff", "86754e387ffffff",
						"86754e297ffffff", "86754e2b7ffffff");
		assertThat(tracks).extracting(Track::getEpochIntervals)
				.containsExactlyInAnyOrder(2629800L, 2629801L, 2629802L, 2629803L, 2629804L, 2629805L, 2629806L,
						2629807L, 2629808L, 2629809L, 2629810L);
		assertThat(tracks).extracting(Track::getTraces).extracting(Collection::size)
				.containsExactly(1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1);
		assertThat(tracks).extracting(Track::getEarliest).extracting(Instant::toString)
				.containsOnly(
						"2020-01-01T12:00:00Z", "2020-01-01T12:10:00Z", "2020-01-01T12:20:00Z", "2020-01-01T12:30:00Z",
						"2020-01-01T12:40:00Z", "2020-01-01T12:50:00Z", "2020-01-01T13:00:00Z", "2020-01-01T13:10:00Z",
						"2020-01-01T13:20:00Z", "2020-01-01T13:30:00Z", "2020-01-01T13:40:00Z");
	}

	@Test
	void tracksMedium() {
		List<Track> tracks = Track.tracks(h3Core, Resolution.MEDIUM, traces);
		assertThat(tracks).hasSize(4)
				.extracting(Track::getH3Cell)
				.containsOnly(596538813879156735L, 596538831059025919L, 596538564771053567L);
		assertThat(tracks).extracting(Track::getEpochIntervals)
				.containsOnly(438300L, 438301L);
		assertThat(tracks).extracting(Track::getTraces).extracting(Collection::size)
				.containsExactlyInAnyOrder(5, 2, 3, 1);
		assertThat(tracks).extracting(Track::getEarliest).extracting(Instant::toString)
				.containsOnly("2020-01-01T12:00:00Z", "2020-01-01T12:10:00Z",
						"2020-01-01T12:40:00Z", "2020-01-01T13:00:00Z");
	}

	@Test
	void tracksCoarse() {
		List<Track> tracks = Track.tracks(h3Core, Resolution.COARSE, traces);
		assertThat(tracks).hasSize(1)
				.extracting(Track::getH3Cell)
				.containsExactly(587531734883500031L);
		assertThat(tracks).extracting(Track::getEpochIntervals)
				.containsExactly(73050L);
		assertThat(tracks).extracting(Track::getTraces).extracting(Collection::size)
				.containsExactly(11);
		assertThat(tracks).extracting(Track::getEarliest).extracting(Instant::toString)
				.containsExactly("2020-01-01T12:00:00Z");
	}

}