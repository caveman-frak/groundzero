package uk.co.bluegecko.utility.spatial;

import static si.uom.NonSI.KNOT;

import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.BeforeEach;
import tech.units.indriya.quantity.Quantities;

public abstract class AbstractSpatialTest<T> {

	Vessel<T> vessel;

	@BeforeEach
	void setUp() {
		setUpContext();
		setUpVessel();
	}

	protected void setUpContext() {
	}

	protected void setUpVessel() {
		vessel = Vessel.<T>builder()
				.position(position(0, 0))
				.knots(Quantities.getQuantity(10.0, KNOT))
				.bearing(0.0)
				.rateOfTurn(0.0)
				.build();
	}

	@NotNull
	abstract T position(double lat, double lng);

	abstract void calcDestinationFromPointWithBearingAndDistance();

	abstract void calcDistanceFromPointToPoint();

	abstract void calcBearingFromPointToPoint();

}