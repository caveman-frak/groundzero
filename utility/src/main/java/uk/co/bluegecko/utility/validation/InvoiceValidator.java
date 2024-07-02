package uk.co.bluegecko.utility.validation;

import java.time.Clock;
import java.time.LocalDate;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;
import uk.co.bluegecko.common.model.invoice.Address;
import uk.co.bluegecko.common.model.invoice.Invoice;

@RequiredArgsConstructor
public class InvoiceValidator implements Validator {

	private final Clock clock;
	private final AddressValidator addressValidator;

	@Override
	public boolean supports(@NonNull Class<?> clazz) {
		return Invoice.class.equals(clazz);
	}

	@Override
	public void validate(@NonNull Object target, @NonNull Errors errors) {
		if (target instanceof Invoice invoice) {
			if (invoice.getNumber() < 0) {
				errors.rejectValue("number", "negative");
			}
			if (invoice.getDate().isAfter(LocalDate.now(clock))) {
				errors.rejectValue("date", "future");
			}
			if (invoice.getCustomer() == null) {
				errors.rejectValue("customer", "empty");
			}
			if (invoice.getLines().isEmpty()) {
				errors.rejectValue("lines", "empty");
			}
			if (invoice.getCurrency() == null) {
				errors.rejectValue("currency", "empty");
			}
			validateAddress(invoice.getCustomer(), errors, "customer");
			validateAddress(invoice.getDelivery(), errors, "delivery");
		}
	}

	private void validateAddress(Address address, @NotNull Errors errors, String name) {
		if (address != null) {
			try {
				errors.pushNestedPath(name);
				ValidationUtils.invokeValidator(addressValidator, address, errors);
			} finally {
				errors.popNestedPath();
			}
		}
	}
}