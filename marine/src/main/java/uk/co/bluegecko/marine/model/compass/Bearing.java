package uk.co.bluegecko.marine.model.compass;

import static systems.uom.ucum.UCUM.DEGREE;
import static uk.co.bluegecko.marine.model.compass.Limit.BEARING;

import javax.measure.quantity.Angle;
import tech.units.indriya.ComparableQuantity;
import tech.units.indriya.quantity.Quantities;

public class Bearing extends Compass {

	public Bearing(ComparableQuantity<Angle> angle) {
		super(angle, BEARING);
	}

	public Bearing(Number degrees) {
		this(Quantities.getQuantity(degrees, DEGREE));
	}

	public Bearing(int degrees, int minutes, double seconds) {
		this(Quantities.getQuantity((double) degrees + ((double) minutes / 60) + seconds / 3600, DEGREE));
	}

	public Bearing(int degrees, int minutes) {
		this(degrees, minutes, 0);
	}

	@Override
	protected ComparableQuantity<Angle> wrap(ComparableQuantity<Angle> angle) {
		return new Bearing(angle);
	}

}