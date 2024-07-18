package uk.co.bluegecko.utility.spatial;

import java.awt.geom.Point2D;
import java.awt.geom.Point2D.Double;
import org.jetbrains.annotations.NotNull;

public class ApacheSisTest extends AbstractSpatialTest<Point2D> {


	@NotNull
	@Override
	Point2D position(double lat, double lng) {
		return new Double(lng, lat);
	}

	@Override
	void calcDestinationFromPointWithBearingAndDistance() {

	}

	@Override
	void calcDistanceFromPointToPoint() {

	}

	@Override
	void calcBearingFromPointToPoint() {

	}
}