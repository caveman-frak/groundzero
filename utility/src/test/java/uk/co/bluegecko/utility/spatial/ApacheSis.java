package uk.co.bluegecko.utility.spatial;

import static si.uom.NonSI.KNOT;

import java.awt.geom.Point2D;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import tech.units.indriya.quantity.Quantities;
import uk.co.bluegecko.utility.spatial.Vessel.VesselBuilder;

public class ApacheSis {

	private Vessel<Point2D> vessel;

	@BeforeEach
	void setUp() {
		VesselBuilder<Point2D> point2DVesselBuilder = Vessel.<Point2D>builder()
				.position(new Point2D.Double(0, 0));
		vessel = point2DVesselBuilder.knots(Quantities.getQuantity(10.0, KNOT))
				.bearing(0.0)
				.rateOfTurn(0.0)
				.build();
	}

	@Test
	void calcDestination() {
	}

	@Test
	void calcDistance() {
	}

	@Test
	void calcBearing() {
	}

}