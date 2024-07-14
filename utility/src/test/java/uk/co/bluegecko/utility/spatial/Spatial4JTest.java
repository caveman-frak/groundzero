package uk.co.bluegecko.utility.spatial;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.withinPercentage;
import static si.uom.NonSI.KNOT;

import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.locationtech.spatial4j.context.SpatialContext;
import org.locationtech.spatial4j.context.SpatialContextFactory;
import org.locationtech.spatial4j.shape.Point;
import tech.units.indriya.quantity.Quantities;
import uk.co.bluegecko.utility.spatial.Vessel.VesselBuilder;

public class Spatial4JTest {

	SpatialContext ctx;
	Vessel<Point> vessel;

	@BeforeEach
	void setUp() {
		ctx = SpatialContextFactory.makeSpatialContext(Map.of("geo", "true"), null);
		Point location = ctx.getShapeFactory().pointLatLon(0.0, 0.0);
		VesselBuilder<Point> pointVesselBuilder = Vessel.<Point>builder()
				.position(location);
		vessel = pointVesselBuilder.knots(Quantities.getQuantity(1.0, KNOT))
				.bearing(0.0)
				.rateOfTurn(0.0)
				.build();
	}

	@Test
	@Disabled("not implemented  for Spatial4J")
	void calcDestination() {
	}

	@Test
	void calcDistance() {
		assertThat(ctx.calcDistance(vessel.getPosition(), 0.166554, 0.0))
				.isCloseTo(0.166554, withinPercentage(0.01));
	}

	@Test
	@Disabled("not implemented  for Spatial4J")
	void calcBearing() {
	}

	@Test
	void calcPointFromBearing() {
		assertThat(ctx.getDistCalc().pointOnBearing(vessel.getPosition(), 0.166554, 0, ctx, null))
				.isEqualTo(ctx.getShapeFactory().pointLatLon(0.166554, 0.0));
	}

}