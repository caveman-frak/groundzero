package uk.co.bluegecko.marine.model.quantity;

import javax.measure.Quantity;
import org.assertj.core.api.WritableAssertionInfo;
import org.assertj.core.data.Offset;
import org.assertj.core.data.Percentage;
import org.assertj.core.internal.Doubles;
import tech.uom.lib.assertj.assertions.AbstractQuantityAssert;

@SuppressWarnings({"unchecked", "rawtypes"})
public class QuantityAssertions extends AbstractQuantityAssert<QuantityAssertions, Quantity> {

	private final Doubles doubles;
	public WritableAssertionInfo info;

	protected QuantityAssertions(Quantity actual) {
		super(actual, QuantityAssertions.class);

		doubles = Doubles.instance();
		info = new WritableAssertionInfo(null);
	}

	public static QuantityAssertions assertThat(Quantity<?> actual) {
		return new QuantityAssertions(actual);
	}

	public QuantityAssertions isCloseTo(double expected, Offset<Double> offset) {
		doubles.assertIsCloseTo(info, actual.getValue().doubleValue(), expected, offset);
		return myself;
	}

	public QuantityAssertions isCloseTo(Double expected, Offset<Double> offset) {
		doubles.assertIsCloseTo(info, actual.getValue().doubleValue(), expected, offset);
		return myself;
	}

	public QuantityAssertions isCloseTo(Quantity<?> expected, Offset<Double> offset) {
		doubles.assertIsCloseTo(info, actual.getValue().doubleValue(), expected.getValue().doubleValue(), offset);
		return myself;
	}

	public QuantityAssertions isCloseTo(double expected, Percentage percentage) {
		doubles.assertIsCloseToPercentage(info, actual.getValue().doubleValue(), expected, percentage);
		return myself;
	}

	public QuantityAssertions isCloseTo(Double expected, Percentage percentage) {
		doubles.assertIsCloseToPercentage(info, actual.getValue().doubleValue(), expected, percentage);
		return myself;
	}

	public QuantityAssertions isCloseTo(Quantity<?> expected, Percentage percentage) {
		doubles.assertIsCloseToPercentage(info, actual.getValue().doubleValue(),
				expected.getValue().doubleValue(), percentage);
		return myself;
	}

	public QuantityAssertions isEquivalentTo(Quantity<?> expected, Offset<Double> offset) {
		Quantity<?> adjusted = expected.to(actual.getUnit());
		return isCloseTo(adjusted.getValue().doubleValue(), offset);
	}

	public QuantityAssertions isEquivalentTo(Quantity<?> expected, Percentage percentage) {
		Quantity<?> adjusted = expected.to(actual.getUnit());
		return isCloseTo(adjusted.getValue().doubleValue(), percentage);
	}

}