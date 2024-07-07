package uk.co.bluegecko.utility.geo.validate;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import java.util.Arrays;
import uk.co.bluegecko.utility.geo.Compass;

public class AllowedCompassPointValidator implements ConstraintValidator<AllowedCompassPoint, Compass> {

	private Compass[] subset;

	@Override
	public void initialize(AllowedCompassPoint constraintAnnotation) {
		subset = constraintAnnotation.oneOf();
	}

	@Override
	public boolean isValid(Compass value, ConstraintValidatorContext context) {
		return value == null || Arrays.asList(subset).contains(value);
	}

}