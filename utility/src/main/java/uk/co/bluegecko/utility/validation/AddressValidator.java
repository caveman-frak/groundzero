package uk.co.bluegecko.utility.validation;

import lombok.NonNull;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;
import uk.co.bluegecko.common.model.invoice.Address;

public class AddressValidator implements Validator {

	@Override
	public boolean supports(@NonNull Class<?> clazz) {
		return Address.class.equals(clazz);
	}

	@Override
	public void validate(@NonNull Object target, @NonNull Errors errors) {
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "name", "empty");
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "building", "empty");
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "street", "empty");
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "town", "empty");
	}

	private void validatePostCode(@NonNull Errors errors, String postcode, String country) {
		if (postcode == null) {
			ValidationUtils.rejectIfEmptyOrWhitespace(errors, "postcode", "empty");

		} else {
			if (country == null) {
				String[] parts = postcode.split(" ");
				if (parts.length != 2) {
					errors.rejectValue("postcode", "invalid");
				}
				validateOutward(errors, parts[0]);
				validateInward(errors, parts[1]);
			}
		}
	}

	private void validateOutward(@NonNull Errors errors, String outward) {
		if (outward.length() < 2 || outward.length() > 4) {
			errors.rejectValue("postcode.outward", "outward.length");
		}
		if (!Character.isLetter(outward.charAt(0))) {
			errors.rejectValue("postcode.outward.0", "outward.not-alpha");
		}
		if (!Character.isLetterOrDigit(outward.charAt(1))) {
			errors.rejectValue("postcode.outward.1", "outward.invalid");
		}
		if (outward.length() > 2 && !Character.isLetterOrDigit(outward.charAt(2))) {
			errors.rejectValue("postcode.outward.2", "outward.invalid");
		}
		if (outward.length() > 3 && !Character.isLetterOrDigit(outward.charAt(3))) {
			errors.rejectValue("postcode.outward.3", "outward.invalid");
		}
	}

	private void validateInward(@NonNull Errors errors, String inward) {
		if (inward.length() != 3) {
			errors.rejectValue("postcode.inward", "inward.length");
		}
		if (!Character.isDigit(inward.charAt(0))) {
			errors.rejectValue("postcode.inward.0", "inward.not-alpha");
		}
		if (!Character.isLetter(inward.charAt(1))) {
			errors.rejectValue("postcode.inward.1", "inward.invalid");
		}
		if (!Character.isLetter(inward.charAt(2))) {
			errors.rejectValue("postcode.inward.2", "inward.invalid");
		}
	}

}