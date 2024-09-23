package uk.co.bluegecko.marine.model.compass;

import javax.measure.Quantity;
import javax.measure.quantity.Angle;
import org.assertj.core.api.Condition;
import org.assertj.core.data.Offset;
import uk.co.bluegecko.marine.model.quantity.QuantityConditions;

@SuppressWarnings({"unchecked", "rawtypes"})
public class CompassConditions {

	public static Condition<Quantity> isCloseTo(Quantity<Angle> expected, Offset<Double> offset) {
		return QuantityConditions.isCloseTo(expected, offset);
	}

}