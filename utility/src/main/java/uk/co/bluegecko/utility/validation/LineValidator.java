package uk.co.bluegecko.utility.validation;

import lombok.NonNull;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import uk.co.bluegecko.common.model.invoice.Line;

public class LineValidator implements Validator {

	@Override
	public boolean supports(@NonNull Class<?> clazz) {
		return Line.class.equals(clazz);
	}

	@Override
	public void validate(@NonNull Object target, @NonNull Errors errors) {

	}

}