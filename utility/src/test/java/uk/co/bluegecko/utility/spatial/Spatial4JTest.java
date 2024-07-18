package uk.co.bluegecko.utility.spatial;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.withinPercentage;

import java.util.Map;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.Test;
import org.locationtech.spatial4j.context.SpatialContext;
import org.locationtech.spatial4j.context.SpatialContextFactory;
import org.locationtech.spatial4j.shape.Point;

public class Spatial4JTest extends AbstractSpatialTest<Point> {

	SpatialContext ctx;

	@Override
	protected void setUpContext() {
		ctx = SpatialContextFactory.makeSpatialContext(Map.of("geo", "true"), null);
	}

	@NotNull
	@Override
	Point position(double lat, double lng) {
		return ctx.getShapeFactory().pointLatLon(lat, lng);
	}

	@Test
	@Override
	void calcDestinationFromPointWithBearingAndDistance() {
		assertThat(ctx.getDistCalc().pointOnBearing(vessel.getPosition(), 0.166554, 0, ctx, null))
				.isEqualTo(position(0.166554, 0.0));
	}

	@Test
	@Override
	void calcDistanceFromPointToPoint() {
		assertThat(ctx.getDistCalc().distance(vessel.getPosition(), position(0.166554, 0.0)))
				.isCloseTo(0.166554, withinPercentage(0.01));
	}

	@Override
	void calcBearingFromPointToPoint() {
	}

}