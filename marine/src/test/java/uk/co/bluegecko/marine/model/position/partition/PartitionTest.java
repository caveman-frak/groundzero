package uk.co.bluegecko.marine.model.position.partition;

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
import uk.co.bluegecko.marine.model.position.Calculator;
import uk.co.bluegecko.marine.model.position.SimpleCourse;
import uk.co.bluegecko.marine.model.position.Trace;

public class PartitionTest extends AbstractTest {

	H3Core h3Core;
	List<Trace> traces;
	Partitioner partitioner;

	void setUpTraces() throws IOException {
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

}