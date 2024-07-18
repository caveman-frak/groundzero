package uk.co.bluegecko.utility.spatial;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.withinPercentage;
import static si.uom.NonSI.NAUTICAL_MILE;
import static systems.uom.ucum.UCUM.METER;

import java.awt.geom.Point2D;
import java.awt.geom.Point2D.Double;
import net.sf.geographiclib.Geodesic;
import org.geotools.referencing.datum.DefaultEllipsoid;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.Test;

public class GeoToolsTest extends AbstractSpatialTest<Point2D> {


	DefaultEllipsoid ellipsoid;

	@Override
	protected void setUpContext() {
		ellipsoid = DefaultEllipsoid.WGS84;

//		CRSAuthorityFactory factory = CRS.getAuthorityFactory(true);
//		CoordinateReferenceSystem crs = factory.createCoordinateReferenceSystem("EPSG:4326");
//		CoordinateSystem coordinateSystem = crs.getCoordinateSystem();
	}

	@NotNull
	@Override
	Point2D position(double lat, double lng) {
		return new Double(lng, lat);
	}

	@Override
	void calcDestinationFromPointWithBearingAndDistance() {
		Geodesic geod = Geodesic.WGS84;
//		GeodesicData g = geod.Inverse(y1, x1, y2, x2, GeodesicMask.DISTANCE);
//		return g.s12;
//				assertThat(travel(vessel.getPosition(), vessel.getBearing(), vessel.distance(Duration.ofHours(1)),
//						LengthUnit.NAUTICAL_MILE))
//				.isEqualTo(new LatLng(0.166554, 0.0));
	}

	@Test
	@Override
	void calcDistanceFromPointToPoint() {
		assertThat(METER.getConverterTo(NAUTICAL_MILE).convert(
				ellipsoid.orthodromicDistance(vessel.getPosition(), new Double(0.166554, 0.0))))
				.isCloseTo(10.011, withinPercentage(0.01));
	}

	@Override
	void calcBearingFromPointToPoint() {

	}

}