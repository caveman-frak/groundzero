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
		if (target instanceof Address address) {
			ValidationUtils.rejectIfEmptyOrWhitespace(errors, "name", "empty");
			ValidationUtils.rejectIfEmptyOrWhitespace(errors, "building", "empty");
			ValidationUtils.rejectIfEmptyOrWhitespace(errors, "street", "empty");
			ValidationUtils.rejectIfEmptyOrWhitespace(errors, "town", "empty");

			validatePostCode(errors, address.getPostcode(), address.getCountry());
		}
	}

	private void validatePostCode(@NonNull Errors errors, String postcode, String country) {
		try {
			errors.pushNestedPath("postcode");
			if (postcode == null) {
				ValidationUtils.rejectIfEmptyOrWhitespace(errors, null, "empty");
			} else {
				if (country == null) {
					if (postcode.length() < 6 || postcode.length() > 8) {
						errors.rejectValue(null, "length");
					} else {
						String[] parts = postcode.split(" ");
						if (parts.length != 2) {
							errors.rejectValue(null, "invalid");
						}
						validateOutward(errors, parts[0]);
						validateInward(errors, parts[1]);
					}
				} else if (postcode.length() < 3 || postcode.length() > 10) {
					errors.rejectValue(null, "length");
				}
			}
		} finally {
			errors.popNestedPath();
		}
	}

	private void validateOutward(@NonNull Errors errors, String outward) {
		if (outward.length() < 2 || outward.length() > 4) {
			errors.rejectValue(null, "length");
		} else {
			if (!Character.isLetter(outward.charAt(0))) {
				errors.rejectValue("0", "not-alpha");
			}
			if (!Character.isLetterOrDigit(outward.charAt(1))) {
				errors.rejectValue("1", "invalid");
			}
			if (outward.length() > 2 && !Character.isLetterOrDigit(outward.charAt(2))) {
				errors.rejectValue("2", "invalid");
			}
			if (outward.length() > 3 && !Character.isLetterOrDigit(outward.charAt(3))) {
				errors.rejectValue("3", "invalid");
			}
		}
	}

	private void validateInward(@NonNull Errors errors, String inward) {
		if (inward.length() != 3) {
			errors.rejectValue(null, "length");
		} else {
			if (!Character.isDigit(inward.charAt(0))) {
				errors.rejectValue("0", "not-alpha");
			}
			if (!Character.isLetter(inward.charAt(1))) {
				errors.rejectValue("1", "invalid");
			}
			if (!Character.isLetter(inward.charAt(2))) {
				errors.rejectValue("2", "invalid");
			}
		}
	}

}