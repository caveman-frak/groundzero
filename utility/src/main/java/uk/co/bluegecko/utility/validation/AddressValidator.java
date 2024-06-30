package uk.co.bluegecko.utility.validation;

import lombok.NonNull;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;
import uk.co.bluegecko.common.model.invoice.Invoice;

public class AddressValidator implements Validator {

	@Override
	public boolean supports(@NonNull Class<?> clazz) {
		return Invoice.class.equals(clazz);
	}

	@Override
	public void validate(@NonNull Object target, @NonNull Errors errors) {
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "name", "name.empty");
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "building", "building.empty");
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "street", "street.empty");
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "town", "town.empty");
	}

	private void validatePostCode(@NonNull Errors errors, String postcode, String country) {
		if (postcode == null) {
			ValidationUtils.rejectIfEmptyOrWhitespace(errors, "postcode", "postcode.empty");

		} else {
			if (country == null) {
				String[] parts = postcode.split(" ");
				if (parts.length != 2) {
					errors.rejectValue("postcode", "postcode.invalidFormat");
				}
				validateOutward(errors, parts[0]);
				validateInward(errors, parts[1]);
			}
		}
	}

	private void validateInward(@NonNull Errors errors, String outward) {
		if (outward.length() < 2 || outward.length() > 4) {
			errors.rejectValue("postcode.outward", "postcode.outward.length");
		}
		if (!Character.isLetter(outward.charAt(0))) {
			errors.rejectValue("postcode.outward.0", "postcode.outward.start-not-alpha");
		}
		if (!Character.isLetterOrDigit(outward.charAt(1))) {
			errors.rejectValue("postcode.outward.1", "postcode.outward.invalid-character");
		}
		if (outward.length() > 2 && !Character.isLetterOrDigit(outward.charAt(2))) {
			errors.rejectValue("postcode.outward.2", "postcode.outward.invalid-character");
		}
		if (outward.length() > 3 && !Character.isLetterOrDigit(outward.charAt(3))) {
			errors.rejectValue("postcode.outward.3", "postcode.outward.invalid-character");
		}
	}

	private void validateOutward(@NonNull Errors errors, String inward) {
		if (inward.length() != 3) {
			errors.rejectValue("postcode.inward", "postcode.inward.length");
		}
		if (!Character.isDigit(inward.charAt(0))) {
			errors.rejectValue("postcode.inward.0", "postcode.inward.start-not-alpha");
		}
		if (!Character.isLetter(inward.charAt(1))) {
			errors.rejectValue("postcode.inward.1", "postcode.inward.invalid-character");
		}
		if (!Character.isLetter(inward.charAt(2))) {
			errors.rejectValue("postcode.inward.2", "postcode.inward.invalid-character");
		}
	}

}