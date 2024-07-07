package uk.co.bluegecko.utility.geo;

import java.math.BigDecimal;
import java.math.MathContext;
import java.util.Optional;
import java.util.Set;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.experimental.FieldDefaults;
import org.hibernate.validator.constraints.Range;
import uk.co.bluegecko.utility.geo.ex.ArgumentOverflowException;
import uk.co.bluegecko.utility.geo.ex.Direction;
import uk.co.bluegecko.utility.geo.ex.Field;

@Getter
@EqualsAndHashCode
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public abstract class DegreeMinuteSecond<T extends DegreeMinuteSecond<?>> implements Angle {

	int lower;
	int upper;

	int degrees;
	@Range(max = 60)
	int minutes;
	@Range(max = 60)
	double seconds;

	protected DegreeMinuteSecond(int lower, int upper,
			int degrees, int minutes, double seconds) {
		this.lower = lower;
		this.upper = upper;
		this.degrees = Math.abs(degrees);
		this.minutes = Math.abs(minutes);
		this.seconds = truncate(Math.abs(seconds));
	}

	protected boolean isReversed() {
		return false;
	}

	@Override
	public double decimal() {
		double decimal = (double) degrees + ((double) minutes / 60) + seconds / 3600;
		return isReversed() ? -decimal : decimal;
	}

	protected static Number[] partsFromDecimal(double degrees) {
		double minutes = (degrees - (int) degrees) * 60;
		return new Number[]{(int) degrees, (int) minutes, (minutes - (int) minutes) * 60};
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

	public T add(double term, boolean wrap) {
		double decimal = wrap(lower, upper, decimal() + term, wrap);
		Number[] args = partsFromDecimal(decimal);
		return create((int) args[0], (int) args[1], (double) args[2]);
	}

	public T add(double term) {
		return add(term, true);
	}

	public T add(T term, boolean wrap) {
		return add(term.decimal(), wrap);
	}

	public T add(T term) {
		return add(term, true);
	}

	public T subtract(double term, boolean wrap) {
		double decimal = wrap(lower, upper, decimal() - term, wrap);
		Number[] args = partsFromDecimal(decimal);
		return create((int) args[0], (int) args[1], (double) args[2]);
	}

	public T subtract(double term) {
		return subtract(term, true);
	}

	public T subtract(T term, boolean wrap) {
		return subtract(term.decimal(), wrap);
	}

	public T subtract(T term) {
		return subtract(term, true);
	}

	protected double wrap(int lower, int upper, double decimal, boolean wrap) {
		if (decimal < lower) {
			if (wrap) {
				return upper - (lower - decimal);
			}
			throw new ArgumentOverflowException(Field.DECIMAL, Direction.UNDERFLOW, lower, decimal);
		} else if (decimal > upper) {
			if (wrap) {
				return lower + (decimal - upper);
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