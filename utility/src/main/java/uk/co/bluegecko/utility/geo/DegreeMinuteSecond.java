package uk.co.bluegecko.utility.geo;

import static systems.uom.ucum.UCUM.DEGREE;

import java.math.BigDecimal;
import java.math.MathContext;
import java.util.Optional;
import java.util.Set;
import javax.measure.Quantity;
import javax.measure.quantity.Angle;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.experimental.FieldDefaults;
import org.hibernate.validator.constraints.Range;
import tech.units.indriya.ComparableQuantity;
import tech.units.indriya.quantity.Quantities;
import uk.co.bluegecko.utility.geo.ex.ArgumentOverflowException;
import uk.co.bluegecko.utility.geo.ex.Direction;
import uk.co.bluegecko.utility.geo.ex.Field;

@Getter
@EqualsAndHashCode
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public abstract class DegreeMinuteSecond<T extends DegreeMinuteSecond<?>> implements DMS {

	ComparableQuantity<Angle> lower;
	ComparableQuantity<Angle> upper;

	int degrees;
	@Range(max = 60)
	int minutes;
	@Range(max = 60)
	double seconds;

	protected DegreeMinuteSecond(int lower, int upper,
			int degrees, int minutes, double seconds) {
		this.lower = Quantities.getQuantity(lower, DEGREE);
		this.upper = Quantities.getQuantity(upper, DEGREE);
		this.degrees = Math.abs(degrees);
		this.minutes = Math.abs(minutes);
		this.seconds = truncate(Math.abs(seconds));
	}

	protected boolean isReversed() {
		return false;
	}

	@Override
	public ComparableQuantity<Angle> decimal() {
		double d = (double) degrees + ((double) minutes / 60) + seconds / 3600;
		ComparableQuantity<Angle> decimal = Quantities.getQuantity(d, DEGREE);

		return isReversed() ? (ComparableQuantity<Angle>) decimal.negate() : decimal;
	}

	protected static Number[] partsFromAngle(Quantity<Angle> decimal) {
		Number degrees = decimal.to(DEGREE).getValue();
		Number minutes = (degrees.doubleValue() - degrees.intValue()) * 60;
		return new Number[]{degrees, minutes.intValue(), (minutes.doubleValue() - minutes.intValue()) * 60};
	}

	private static double truncate(double seconds) {
		return BigDecimal.valueOf(seconds).round(MathContext.DECIMAL32).doubleValue();
	}

	public boolean isCardinal() {
		return seconds == 0.0 && minutes == 0 && degrees % 90 == 0;
	}

	public Optional<Compass> compassPoint(Set<Compass> points) {
		if (seconds == 0.0 && minutes == 0) {
			return points.stream().filter(c -> c.getBearing().equals(this)).findFirst();
		}
		return Optional.empty();
	}

	public Optional<Compass> compassPoint() {
		return compassPoint(Compass.sixteenWinds());
	}

	public T add(ComparableQuantity<Angle> term, boolean wrap) {
		Quantity<Angle> decimal = wrap(lower, upper, decimal().add(term), wrap);
		Number[] args = partsFromAngle(decimal);
		return create(args[0].intValue(), args[1].intValue(), args[2].doubleValue());
	}

	public T add(ComparableQuantity<Angle> term) {
		return add(term, true);
	}

	public T add(T term, boolean wrap) {
		return add(term.decimal(), wrap);
	}

	public T add(T term) {
		return add(term, true);
	}

	public T subtract(ComparableQuantity<Angle> term, boolean wrap) {
		Quantity<Angle> decimal = wrap(lower, upper, decimal().subtract(term), wrap);
		Number[] args = partsFromAngle(decimal);
		return create(args[0].intValue(), args[1].intValue(), args[2].doubleValue());
	}

	public T subtract(ComparableQuantity<Angle> term) {
		return subtract(term, true);
	}

	public T subtract(T term, boolean wrap) {
		return subtract(term.decimal(), wrap);
	}

	public T subtract(T term) {
		return subtract(term, true);
	}

	protected Quantity<Angle> wrap(ComparableQuantity<Angle> lower, ComparableQuantity<Angle> upper,
			ComparableQuantity<Angle> decimal, boolean wrap) {
		if (decimal.isLessThan(lower)) {
			if (wrap) {
				return upper.subtract(lower.subtract(decimal));
			}
			throw new ArgumentOverflowException(Field.DECIMAL, Direction.UNDERFLOW, lower, decimal);
		} else if (decimal.isGreaterThan(upper)) {
			if (wrap) {
				return lower.add(decimal.subtract(upper));
			}
			throw new ArgumentOverflowException(Field.DECIMAL, Direction.OVERFLOW, upper, decimal);
		} else {
			return decimal;
		}
	}

	protected abstract T create(int degrees, int minutes, double seconds);

	@Override
	public String toString() {
		return getToStringBuilder().toString();
	}

}