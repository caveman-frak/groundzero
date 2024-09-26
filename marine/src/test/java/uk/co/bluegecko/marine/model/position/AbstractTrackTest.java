package uk.co.bluegecko.marine.model.position;

import static si.uom.NonSI.KNOT;

import com.uber.h3core.H3Core;
import java.awt.geom.Point2D;
import java.io.IOException;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import org.locationtech.spatial4j.context.SpatialContext;
import org.locationtech.spatial4j.context.SpatialContextFactory;
import tech.units.indriya.quantity.Quantities;
import uk.co.bluegecko.marine.model.AbstractTest;
import uk.co.bluegecko.marine.model.compass.Bearing;
import uk.co.bluegecko.marine.model.position.partition.LocationPartition;
import uk.co.bluegecko.marine.model.position.partition.LocationTimePartition;
import uk.co.bluegecko.marine.model.position.partition.Resolution;

public class AbstractTrackTest extends AbstractTest {

	protected H3Core h3Core;
	protected List<Trace> traces;

	protected Track buildLocationTrack(long cell, List<Trace> traces) {
		return new Track(new LocationPartition(Resolution.MEDIUM, cell), traces, clock.instant());
	}

	protected Track buildLocationTrack(long cell, Trace trace) {
		return buildLocationTrack(cell, List.of(trace));
	}

	protected Track buildLocationTimeTrack(long cell, long epochIntervals, List<Trace> traces) {
		return new Track(new LocationTimePartition(Resolution.MEDIUM, cell, epochIntervals), traces, clock.instant());
	}

	protected Track buildLocationTimeTrack(long cell, long epochIntervals, Trace trace) {
		return buildLocationTimeTrack(cell, epochIntervals, List.of(trace));
	}

	protected Trace buildTrace(UUID vesselId) {
		return Trace.builder()
				.vesselId(vesselId)
				.timestamp(clock.instant())
				.bearing(Bearing.asDegrees(45))
				.speed(Quantities.getQuantity(10, KNOT))
				.coordinates(10, 10)
				.build();
	}

	protected List<Trace> generateTraces(SimpleCourse course, UUID vesselId) {
		List<Trace> traces;
		Trace trace = Trace.builder()
				.vesselId(vesselId)
				.timestamp(clock.instant())
				.build();

		traces = new ArrayList<>(List.of(trace));
		for (int i = 0; i < 10; i++) {
			clock.tick(Duration.ofMinutes(10));
			trace = course.next().apply(trace);
			traces.add(trace);
		}
		return traces;
	}

	protected void setUpTraces() throws IOException {
		setUpClock();
		h3Core = H3Core.newInstance();
		SpatialContext ctx = SpatialContextFactory.makeSpatialContext(Map.of("geo", "true"), null);
		SimpleCourse course = new SimpleCourse(new Calculator(ctx), clock, Quantities.getQuantity(10, KNOT),
				new Point2D.Double(0.0001, 0.0001));
		traces = generateTraces(course, new UUID(0, 0));
	}


}