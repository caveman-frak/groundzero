package uk.co.bluegecko.utility.spatial;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.withinPercentage;
import static si.uom.NonSI.KNOT;
import static si.uom.NonSI.NAUTICAL_MILE;
import static systems.uom.ucum.UCUM.METER;

import java.awt.geom.Point2D;
import java.awt.geom.Point2D.Double;
import net.sf.geographiclib.Geodesic;
import org.geotools.api.referencing.FactoryException;
import org.geotools.referencing.datum.DefaultEllipsoid;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import tech.units.indriya.quantity.Quantities;
import uk.co.bluegecko.utility.spatial.Vessel.VesselBuilder;

public class GeoToolsTest {

	Vessel<Point2D> vessel;
	DefaultEllipsoid ellipsoid;


	@BeforeEach
	void setUp() throws FactoryException {
//		CRSAuthorityFactory factory = CRS.getAuthorityFactory(true);
//		CoordinateReferenceSystem crs = factory.createCoordinateReferenceSystem("EPSG:4326");
//		CoordinateSystem coordinateSystem = crs.getCoordinateSystem();

		ellipsoid = DefaultEllipsoid.WGS84;
		VesselBuilder<Point2D> point2DVesselBuilder = Vessel.<Point2D>builder()
				.position(new Double(0, 0));
		vessel = point2DVesselBuilder.knots(Quantities.getQuantity(10.0, KNOT))
				.bearing(0.0)
				.rateOfTurn(0.0)
				.build();
	}

	@Test
	void calcDestination() {
		Geodesic geod = Geodesic.WGS84;
//		GeodesicData g = geod.Inverse(y1, x1, y2, x2, GeodesicMask.DISTANCE);
//		return g.s12;
//				assertThat(travel(vessel.getPosition(), vessel.getBearing(), vessel.distance(Duration.ofHours(1)),
//						LengthUnit.NAUTICAL_MILE))
//				.isEqualTo(new LatLng(0.166554, 0.0));
	}

	@Test
	void calcDistance() {
		assertThat(METER.getConverterTo(NAUTICAL_MILE).convert(
				ellipsoid.orthodromicDistance(vessel.getPosition(), new Double(0.166554, 0.0))))
				.isCloseTo(10.011, withinPercentage(0.01));
	}

	@Test
	void calcBearing() {
//		assertThat(initialBearing(vessel.getPosition(), new LatLng(0.166554, 0.0)))
//				.isCloseTo(0.0, withinPercentage(0.01));
	}

}