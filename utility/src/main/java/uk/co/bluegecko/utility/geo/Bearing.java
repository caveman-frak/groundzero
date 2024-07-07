package uk.co.bluegecko.utility.geo;

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

	public static Bearing fromDecimal(double decimal) {
		Number[] args = Bearing.partsFromDecimal(decimal);
		return new Bearing((int) args[0], (int) args[1], (double) args[2]);
	}

	@Override
	protected Bearing create(int degrees, int minutes, double seconds) {
		return new Bearing(degrees, minutes, seconds);
	}

}