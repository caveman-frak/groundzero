package uk.co.bluegecko.utility.geo.ex;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

@Getter
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class ArgumentOverflowException extends RuntimeException {

	Field field;
	Direction direction;
	int bound;
	Number value;


	public ArgumentOverflowException(Field field, Direction direction, int bound, Number value) {
		super("%s %s beyond %d with value %s".formatted(field.getText(), direction.getText(), bound, value));

		this.field = field;
		this.direction = direction;
		this.bound = bound;
		this.value = value;
	}

}