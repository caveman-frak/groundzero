package uk.co.bluegecko.utility.geo;

import javax.measure.Quantity;
import javax.measure.quantity.Angle;
import lombok.Getter;
import org.hibernate.validator.constraints.Range;
import uk.co.bluegecko.utility.geo.validate.AllowedCompassPoint;

@Getter
public class Latitude extends DegreeMinuteSecond<Latitude> implements Hemisphere {

	private final Compass hemisphere;

	public Latitude(@Range(min = 0, max = 90) int degrees,
			@Range(max = 60) int minutes,
			@Range(max = 60) double seconds,
			@AllowedCompassPoint(oneOf = {Compass.N, Compass.S})
			Compass hemisphere) {
		super(-90, 90, degrees, minutes, seconds);

		this.hemisphere = hemisphere;
	}

	public Latitude(@Range(min = -90, max = 90) int degrees,
			@Range(max = 60) int minutes,
			@Range(max = 60) double seconds) {
		this(degrees, minutes, seconds,
				degrees < 0 || minutes < 0 || seconds < 0 ? Compass.S : Compass.N);
	}

	@Override
	protected boolean isReversed() {
		return hemisphere == Compass.S;
	}

	@Override
	protected Latitude create(int degrees, int minutes, double seconds) {
		return new Latitude(degrees, minutes, seconds);
	}

	public static Latitude fromAngle(Quantity<Angle> decimal) {
		Number[] args = Latitude.partsFromAngle(decimal);
		return new Latitude(args[0].intValue(), args[1].intValue(), args[2].doubleValue());
	}

}