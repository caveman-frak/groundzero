package uk.co.bluegecko.marine.model.compass;

import static systems.uom.ucum.UCUM.DEGREE;

import javax.measure.Quantity;
import javax.measure.Unit;
import javax.measure.quantity.Angle;
import lombok.AccessLevel;
import lombok.Getter;
import org.jetbrains.annotations.NotNull;
import tech.units.indriya.ComparableQuantity;
import tech.units.indriya.quantity.Quantities;
import tech.units.indriya.quantity.QuantityRange;

public abstract class Compass implements Angle, ComparableQuantity<Angle> {

	private final ComparableQuantity<Angle> angle;
	@Getter(AccessLevel.PROTECTED)
	private final Limit limit;

	protected Compass(ComparableQuantity<Angle> angle, Limit limit) {
		this.angle = limit(angle, limit);
		this.limit = limit;
	}

	public QuantityRange<Angle> range() {
		return limit.range();
	}

	protected abstract ComparableQuantity<Angle> wrap(ComparableQuantity<Angle> angle);

	@Override
	public ComparableQuantity<Angle> add(Quantity<Angle> addend) {
		return wrap(angle.add(addend));
	}

	@Override
	public ComparableQuantity<Angle> subtract(Quantity<Angle> subtrahend) {
		return wrap(angle.subtract(subtrahend));
	}

	@Override
	public ComparableQuantity<?> divide(Quantity<?> divisor) {
		return angle.divide(divisor);
	}

	@Override
	public ComparableQuantity<Angle> divide(Number divisor) {
		return wrap(angle.divide(divisor));
	}

	@Override
	public <T extends Quantity<T>, E extends Quantity<E>> ComparableQuantity<E> divide(Quantity<T> that,
			Class<E> asTypeQuantity) {
		return angle.divide(that, asTypeQuantity);
	}

	@Override
	public ComparableQuantity<?> multiply(Quantity<?> multiplicand) {
		return angle.multiply(multiplicand);
	}

	@Override
	public ComparableQuantity<Angle> multiply(Number multiplicand) {
		return wrap(angle.multiply(multiplicand));
	}

	@Override
	public <T extends Quantity<T>, E extends Quantity<E>> ComparableQuantity<E> multiply(Quantity<T> that,
			Class<E> asTypeQuantity) {
		return angle.multiply(that, asTypeQuantity);
	}

	@Override
	public ComparableQuantity<Angle> to(Unit<Angle> unit) {
		return angle.to(unit);
	}

	@Override
	public ComparableQuantity<?> inverse() {
		return angle.inverse();
	}

	@Override
	public <T extends Quantity<T>> ComparableQuantity<T> inverse(Class<T> quantityClass) {
		return angle.inverse(quantityClass);
	}

	@Override
	public Quantity<Angle> negate() {
		return angle.negate();
	}

	@Override
	public <T extends Quantity<T>> ComparableQuantity<T> asType(Class<T> type) throws ClassCastException {
		return angle.asType(type);
	}

	@Override
	public Number getValue() {
		return angle.getValue();
	}

	@Override
	public Unit<Angle> getUnit() {
		return angle.getUnit();
	}

	@Override
	public Quantity<Angle> toSystemUnit() {
		return Angle.super.toSystemUnit();
	}

	@Override
	public Scale getScale() {
		return angle.getScale();
	}

	@Override
	public boolean isEquivalentTo(Quantity<Angle> that) {
		return angle.isEquivalentTo(that);
	}

	@Override
	public boolean isGreaterThan(Quantity<Angle> that) {
		return angle.isGreaterThan(that);
	}

	@Override
	public boolean isGreaterThanOrEqualTo(Quantity<Angle> that) {
		return angle.isGreaterThanOrEqualTo(that);
	}

	@Override
	public boolean isLessThan(Quantity<Angle> that) {
		return angle.isLessThan(that);
	}

	@Override
	public boolean isLessThanOrEqualTo(Quantity<Angle> that) {
		return angle.isLessThanOrEqualTo(that);
	}

	@Override
	public int compareTo(@NotNull Quantity<Angle> o) {
		return angle.compareTo(o);
	}

	@Override
	public int hashCode() {
		return angle.hashCode();
	}

	@Override
	public boolean equals(Object obj) {
		return getClass().equals(obj.getClass()) && angle.equals(obj);
	}

	@Override
	public String toString() {
		return angle.toString();
	}

	static ComparableQuantity<Angle> limit(Quantity<Angle> angle, Limit limit) {
		Quantity<Angle> degrees = angle.to(DEGREE);
		if (limit.range().contains(degrees)) {
			return (ComparableQuantity<Angle>) angle;
		} else {
			double min = limit.range().getMinimum().getValue().doubleValue();
			double max = limit.range().getMaximum().getValue().doubleValue();
			double bounded = degrees.getValue().doubleValue() % (max - min);

			return Quantities.getQuantity(bounded < min ? bounded + max : bounded > max ? bounded + min : bounded,
					DEGREE).to(angle.getUnit());
		}
	}

}