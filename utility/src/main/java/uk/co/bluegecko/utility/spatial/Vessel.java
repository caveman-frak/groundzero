package uk.co.bluegecko.utility.spatial;

import static si.uom.NonSI.KNOT;
import static si.uom.NonSI.NAUTICAL_MILE;
import static tech.units.indriya.unit.Units.MINUTE;

import java.time.Duration;
import javax.measure.Quantity;
import javax.measure.quantity.Length;
import javax.measure.quantity.Speed;
import javax.measure.quantity.Time;
import lombok.Builder;
import lombok.Builder.Default;
import lombok.Value;
import tech.units.indriya.quantity.Quantities;

@Builder
@Value
public class Vessel<T> {

	T position;
	@Default
	Quantity<Speed> knots = Quantities.getQuantity(0.0, KNOT);
	double bearing;
	double rateOfTurn;

	public Quantity<Length> distance(Quantity<Time> duration) {
		return knots.multiply(duration).asType(Length.class).to(NAUTICAL_MILE);
	}

	public Quantity<Length> distance(Duration duration) {
		return distance(Quantities.getQuantity(duration.toMinutes(), MINUTE));
	}
	
}