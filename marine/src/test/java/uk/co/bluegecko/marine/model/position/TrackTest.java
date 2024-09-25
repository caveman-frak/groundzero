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
import java.util.List;
import java.util.Map;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.locationtech.spatial4j.context.SpatialContext;
import org.locationtech.spatial4j.context.SpatialContextFactory;
import tech.units.indriya.quantity.Quantities;
import uk.co.bluegecko.marine.model.AbstractTest;
import uk.co.bluegecko.marine.model.position.partition.LocationTimeVesselPartition;
import uk.co.bluegecko.marine.model.position.partition.Resolution;

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
		LocationTimeVesselPartition partition = (LocationTimeVesselPartition) LocationTimeVesselPartition
				.partitioner(h3Core).apply(resolution, traces.get(1));
		long epochIntervals = partition.epochIntervals();
		assertThat(Instant.EPOCH.plus(resolution.duration().multipliedBy(epochIntervals))).isEqualTo(
				LocalDateTime.of(2020, Month.JANUARY, 1, 12, 0).toInstant(ZoneOffset.UTC));
		assertThat(resolution.start(epochIntervals)).isEqualTo(
				LocalDateTime.of(2020, Month.JANUARY, 1, 12, 0).toInstant(ZoneOffset.UTC));
		assertThat(resolution.end(epochIntervals)).isEqualTo(
				LocalDateTime.of(2020, Month.JANUARY, 1, 13, 0).toInstant(ZoneOffset.UTC));
		assertThat(resolution.duration()).isEqualTo(Duration.ofHours(1));
		long h3Cell = partition.cell();
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

}