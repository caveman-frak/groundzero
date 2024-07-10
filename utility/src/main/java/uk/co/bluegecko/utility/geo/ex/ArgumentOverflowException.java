package uk.co.bluegecko.utility.geo.ex;

import javax.measure.Quantity;
import javax.measure.quantity.Angle;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

@Getter
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class ArgumentOverflowException extends RuntimeException {

	Field field;
	Direction direction;
	Number bound;
	Number value;


	public ArgumentOverflowException(Field field, Direction direction, Number bound, Number value) {
		super("%s %s beyond %s with value %s".formatted(field.getText(), direction.getText(), bound, value));

		this.field = field;
		this.direction = direction;
		this.bound = bound;
		this.value = value;
	}

	public ArgumentOverflowException(Field field, Direction direction, Quantity<Angle> bound, Quantity<Angle> value) {
		this(field, direction, bound.getValue(), value.getValue());
	}
}