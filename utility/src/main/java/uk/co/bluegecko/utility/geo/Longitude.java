package uk.co.bluegecko.utility.geo;

import javax.measure.Quantity;
import javax.measure.quantity.Angle;
import lombok.Getter;
import org.hibernate.validator.constraints.Range;
import uk.co.bluegecko.utility.geo.validate.AllowedCompassPoint;

@Getter
public class Longitude extends DegreeMinuteSecond<Longitude> implements Hemisphere {

	private final Compass hemisphere;

	public Longitude(@Range(min = 0, max = 180) int degrees,
			@Range(max = 60) int minutes,
			@Range(max = 60) double seconds,
			@AllowedCompassPoint(oneOf = {Compass.E, Compass.W})
			Compass hemisphere) {
		super(-180, 180, degrees, minutes, seconds);

		this.hemisphere = hemisphere;
	}

	public Longitude(@Range(min = -180, max = 180) int degrees,
			@Range(max = 60) int minutes,
			@Range(max = 60) double seconds) {
		this(degrees, minutes, seconds,
				degrees < 0 || minutes < 0 || seconds < 0 ? Compass.W : Compass.E);
	}

	@Override
	protected boolean isReversed() {
		return hemisphere == Compass.W;
	}

	@Override
	protected Longitude create(int degrees, int minutes, double seconds) {
		return new Longitude(degrees, minutes, seconds);
	}

	public static Longitude fromAngle(Quantity<Angle> decimal) {
		Number[] args = Longitude.partsFromAngle(decimal);
		return new Longitude(args[0].intValue(), args[1].intValue(), args[2].doubleValue());
	}

}