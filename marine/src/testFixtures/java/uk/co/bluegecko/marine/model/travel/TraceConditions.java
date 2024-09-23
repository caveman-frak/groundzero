package uk.co.bluegecko.marine.model.travel;

import static org.assertj.core.condition.AllOf.allOf;

import java.time.Instant;
import java.util.UUID;
import javax.measure.Quantity;
import javax.measure.quantity.Speed;
import org.assertj.core.api.Condition;
import org.assertj.core.data.Offset;
import org.assertj.core.data.Percentage;
import si.uom.quantity.AngularSpeed;
import uk.co.bluegecko.marine.model.compass.Bearing;
import uk.co.bluegecko.marine.model.compass.Latitude;
import uk.co.bluegecko.marine.model.compass.Longitude;
import uk.co.bluegecko.marine.model.quantity.QuantityConditions;

public class TraceConditions extends QuantityConditions {

	public static Condition<Trace> hasVesselId(UUID expected) {
		return new Condition<>(t -> t.getVesselId().equals(expected), "has vessel id eq %s", expected);
	}

	public static Condition<Trace> hasTimestamp(Instant expected) {
		return new Condition<>(t -> t.getTimestamp().equals(expected), "has timestamp eq %s", expected);
	}

	public static Condition<Trace> hasLatitude(double expected, Offset<Double> offset) {
		return new Condition<>(t -> isCloseTo(expected, offset).matches(t.getCoordinate().latitude()),
				"has latitude ~eq %f ±%s", expected, offset);
	}

	public static Condition<Trace> hasLatitude(Latitude expected, Offset<Double> offset) {
		return new Condition<>(closeTo(t -> t.getCoordinate().latitude(), expected, offset),
				"has latitude ~eq %s ±%s", expected, offset);
	}

	public static Condition<Trace> hasLatitude(Latitude expected, Percentage percentage) {
		return new Condition<>(closeTo(t -> t.getCoordinate().latitude(), expected, percentage),
				"has latitude ~eq %s ±%f", expected, percentage.value);
	}

	public static Condition<Trace> hasLongitude(double expected, Offset<Double> offset) {
		return new Condition<>(t -> isCloseTo(expected, offset).matches(t.getCoordinate().longitude()),
				"has longitude ~eq %f ±%s", expected, offset);
	}

	public static Condition<Trace> hasLongitude(Longitude expected, Offset<Double> offset) {
		return new Condition<>(closeTo(t -> t.getCoordinate().longitude(), expected, offset),
				"has longitude ~eq %s ±%s", expected, offset);
	}

	public static Condition<Trace> hasLongitude(Longitude expected, Percentage percentage) {
		return new Condition<>(closeTo(t -> t.getCoordinate().longitude(), expected, percentage),
				"has longitude ~eq %s ±%f", expected, percentage.value);
	}

	public static Condition<Trace> hasBearing(double expected, Offset<Double> offset) {
		return new Condition<>(t -> isCloseTo(expected, offset).matches(t.getBearing()),
				"has bearing ~eq %f ±%s", expected, offset);
	}

	public static Condition<Trace> hasBearing(Bearing expected, Offset<Double> offset) {
		return new Condition<>(nullOrCloseTo(Trace::getBearing, expected, offset),
				"has bearing ~eq %s ±%s", expected, offset);
	}

	public static Condition<Trace> hasBearing(Bearing expected, Percentage percentage) {
		return new Condition<>(nullOrCloseTo(Trace::getBearing, expected, percentage),
				"has bearing ~eq %s ±%f", expected, percentage.value);
	}

	public static Condition<Trace> hasSpeed(double expected, Offset<Double> offset) {
		return new Condition<>(t -> isCloseTo(expected, offset).matches(t.getSpeed()),
				"has speed ~eq %f ±%s", expected, offset);
	}

	public static Condition<Trace> hasSpeed(Quantity<Speed> expected, Offset<Double> offset) {
		return new Condition<>(nullOrCloseTo(Trace::getSpeed, expected, offset),
				"has speed ~eq %s ±%s", expected, offset);
	}

	public static Condition<Trace> hasSpeed(Quantity<Speed> expected, Percentage percentage) {
		return new Condition<>(nullOrCloseTo(Trace::getSpeed, expected, percentage),
				"has speed ~eq %s ±%f", expected, percentage.value);
	}

	public static Condition<Trace> hasRateOfTurn(Double expected, Offset<Double> offset) {
		return new Condition<>(t -> isCloseTo(expected, offset).matches(t.getRateOfTurn()),
				"has RoT ~eq %f ±%s", expected, offset);
	}

	public static Condition<Trace> hasRateOfTurn(Quantity<AngularSpeed> expected, Offset<Double> offset) {
		return new Condition<>(nullOrCloseTo(Trace::getRateOfTurn, expected, offset),
				"has RoT ~eq %s ±%s", expected, offset);
	}

	public static Condition<Trace> hasRateOfTurn(Quantity<AngularSpeed> expected, Percentage percentage) {
		return new Condition<>(nullOrCloseTo(Trace::getRateOfTurn, expected, percentage),
				"has RoT ~eq %s ±%f", expected, percentage.value);
	}

	public static Condition<Trace> isEqualsTo(Trace expected, Offset<Double> offset) {
		return allOf(hasVesselId(expected.getVesselId()),
				hasTimestamp(expected.getTimestamp()),
				hasLatitude(expected.getCoordinate().latitude(), offset),
				hasLongitude(expected.getCoordinate().longitude(), offset),
				hasBearing(expected.getBearing(), offset),
				hasSpeed(expected.getSpeed(), offset),
				hasRateOfTurn(expected.getRateOfTurn(), offset));
	}

	public static Condition<Trace> isEqualsTo(Trace expected, Percentage percentage) {
		return allOf(
				hasVesselId(expected.getVesselId()),
				hasTimestamp(expected.getTimestamp()),
				hasLatitude(expected.getCoordinate().latitude(), percentage),
				hasLongitude(expected.getCoordinate().longitude(), percentage),
				hasBearing(expected.getBearing(), percentage),
				hasSpeed(expected.getSpeed(), percentage),
				hasRateOfTurn(expected.getRateOfTurn(), percentage));
	}

}