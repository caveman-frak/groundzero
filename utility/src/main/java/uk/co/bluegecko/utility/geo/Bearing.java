package uk.co.bluegecko.utility.geo;

import javax.measure.Quantity;
import javax.measure.quantity.Angle;
import org.hibernate.validator.constraints.Range;

public class Bearing extends DegreeMinuteSecond<Bearing> {

	public Bearing(@Range(max = 360) int degrees,
			@Range(max = 60) int minutes,
			@Range(max = 60) double seconds) {
		super(0, 360, degrees, minutes, seconds);
	}

	public Bearing(@Range(max = 360) int degrees,
			@Range(max = 60) int minutes) {
		this(degrees, minutes, 0);
	}

	public Bearing(@Range(max = 360) int degrees) {
		this(degrees, 0, 0);
	}

	public static Bearing fromAngle(Quantity<Angle> decimal) {
		Number[] args = Bearing.partsFromAngle(decimal);
		return new Bearing(args[0].intValue(), args[1].intValue(), args[2].doubleValue());
	}

	@Override
	protected Bearing create(int degrees, int minutes, double seconds) {
		return new Bearing(degrees, minutes, seconds);
	}

}