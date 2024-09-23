package uk.co.bluegecko.marine.model.quantity;

import java.util.Objects;
import java.util.function.Function;
import java.util.function.Predicate;
import javax.measure.Quantity;
import javax.measure.Unit;
import org.assertj.core.api.Condition;
import org.assertj.core.condition.AllOf;
import org.assertj.core.data.Offset;
import org.assertj.core.data.Percentage;
import uk.co.bluegecko.marine.model.position.Trace;

@SuppressWarnings({"unchecked", "rawtypes"})
public class QuantityConditions {

	public static Condition<Quantity> hasUnit(Unit unit) {
		return new Condition<>(q -> q.getUnit().isEquivalentTo(unit), "has unit eq %s", unit);
	}

	public static Condition<Quantity> isEqualTo(double expected) {
		return new Condition<>(q -> q.getValue().doubleValue() == expected, "has value eq %f", expected);
	}

	public static Condition<Quantity> isEqualTo(Number expected) {
		return isEqualTo(expected.doubleValue());
	}

	private static Condition<Quantity> isCloseTo(double expected, double offset, boolean strict) {
		return new Condition<>(q -> {
			double diff = Math.abs(q.getValue().doubleValue() - expected);
			return strict ? diff < offset : diff <= offset;
		}, "has value ~eq %f ±%s", expected, offset);
	}

	public static Condition<Quantity> isCloseTo(double expected, Offset<Double> offset) {
		return isCloseTo(expected, offset.value, offset.strict);
	}

	public static Condition<Quantity> isCloseTo(Number expected, Offset<Double> offset) {
		return isCloseTo(expected.doubleValue(), offset.value, offset.strict);
	}

	public static Condition<Quantity> isCloseTo(double expected, Percentage percentage) {
		return new Condition<>(
				q -> Math.abs(q.getValue().doubleValue() - expected) < (expected * percentage.value / 100.0),
				"has value ~eq %f ±%s", expected, percentage);
	}

	public static Condition<Quantity> isCloseTo(Number expected, Percentage percentage) {
		return isCloseTo(expected.doubleValue(), percentage);
	}

	public static Condition<Quantity> isEqualTo(Quantity expected) {
		return AllOf.allOf(hasUnit(expected.getUnit()), isEqualTo(expected.getValue()));
	}

	public static Condition<Quantity> isCloseTo(Quantity expected, Offset<Double> offset) {
		return AllOf.allOf(hasUnit(expected.getUnit()), isCloseTo(expected.getValue(), offset));
	}

	public static Condition<Quantity> isCloseTo(Quantity expected, Percentage percentage) {
		return AllOf.allOf(hasUnit(expected.getUnit()), isCloseTo(expected.getValue(), percentage));
	}

	public static Condition<Quantity> equivalentTo(Quantity expected) {
		return new Condition<>(q -> q.isEquivalentTo(expected), "is equivalent to %s", expected);
	}

	public static Condition<Quantity> equivalentTo(Quantity expected, Offset<Double> offset) {
		return new Condition<>(q -> {
			Quantity adjustedActual = q.toSystemUnit();
			Quantity adjustedExpected = expected.toSystemUnit();
			return adjustedActual.getUnit().equals(adjustedExpected)
					&& Math.abs(adjustedActual.getValue().doubleValue() - adjustedExpected.getValue().doubleValue())
					< offset.value;
		}, "is ~equivalent to %s ±%s", expected, offset);
	}

	public static Condition<Quantity> equivalentTo(Quantity expected, Percentage percentage) {
		return new Condition<>(q -> {
			Quantity adjustedActual = q.toSystemUnit();
			Quantity adjustedExpected = expected.toSystemUnit();
			return adjustedActual.getUnit().equals(adjustedExpected) &&
					Math.abs(adjustedActual.getValue().doubleValue() - adjustedExpected.getValue().doubleValue()) <
							(adjustedExpected.getValue().doubleValue() * percentage.value / 100);
		}, "is ~equivalent to %s ±%s", expected, percentage);
	}

	protected static Predicate<Trace> closeTo(Function<Trace, Quantity> actual, Quantity expected,
			Offset<Double> offset) {
		return t -> isCloseTo(expected, offset).matches(actual.apply(t));
	}

	protected static Predicate<Trace> closeTo(Function<Trace, Quantity> actual, Quantity expected,
			Percentage percentage) {
		return t -> isCloseTo(expected, percentage).matches(actual.apply(t));
	}

	protected static Predicate<Trace> nullOrCloseTo(Function<Trace, Quantity> actual, Quantity expected,
			Offset<Double> offset) {
		return t -> Objects.equals(actual.apply(t), expected)
				|| isCloseTo(expected, offset).matches(actual.apply(t));
	}

	protected static Predicate<Trace> nullOrCloseTo(Function<Trace, Quantity> actual, Quantity expected,
			Percentage percentage) {
		return t -> Objects.equals(actual.apply(t), expected)
				|| isCloseTo(expected, percentage).matches(actual.apply(t));
	}

	protected static <T> Condition<T> fail(Class<T> klass) {
		return new Condition<>(_ -> false, "true");
	}

}