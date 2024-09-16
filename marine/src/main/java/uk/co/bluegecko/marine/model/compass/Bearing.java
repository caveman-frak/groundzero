package uk.co.bluegecko.marine.model.compass;

import static systems.uom.ucum.UCUM.DEGREE;
import static systems.uom.ucum.UCUM.RADIAN;
import static uk.co.bluegecko.marine.model.compass.Limit.BEARING;

import javax.measure.quantity.Angle;
import tech.units.indriya.ComparableQuantity;
import tech.units.indriya.quantity.Quantities;

public class Bearing extends Compass {

	public Bearing(ComparableQuantity<Angle> angle) {
		super(angle, BEARING);
	}

	public static Bearing asDegrees(Number degrees) {
		return new Bearing(Quantities.getQuantity(degrees, DEGREE));
	}

	public static Bearing asRadians(Number radians) {
		return new Bearing(Quantities.getQuantity(radians, RADIAN));
	}

	public static Bearing asDegreeMinuteSecond(int degrees, int minutes, double seconds) {
		return asDegrees((double) degrees + ((double) minutes / 60) + seconds / 3600);
	}

	public static Bearing asDegreeMinute(int degrees, int minutes) {
		return asDegreeMinuteSecond(degrees, minutes, 0);
	}

	@Override
	protected ComparableQuantity<Angle> wrap(ComparableQuantity<Angle> angle) {
		return new Bearing(angle);
	}

}