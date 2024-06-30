package uk.co.bluegecko.utility.validation;

import java.time.LocalDate;
import lombok.NonNull;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import uk.co.bluegecko.common.model.invoice.Invoice;

public class InvoiceValidator implements Validator {

	@Override
	public boolean supports(@NonNull Class<?> clazz) {
		return Invoice.class.equals(clazz);
	}

	@Override
	public void validate(@NonNull Object target, @NonNull Errors errors) {
		if (target instanceof Invoice invoice) {
			if (invoice.getNumber() < 0) {
				errors.rejectValue("number", "number.negative");
			}
			if (invoice.getDate().isAfter(LocalDate.now())) {
				errors.rejectValue("date", "date.in-future");
			}
			if (invoice.getCustomer() == null) {
				errors.rejectValue("customer", "customer.missing");
			}
			if (invoice.getLines().isEmpty()) {
				errors.rejectValue("lines", "lines.empty");
			}
		}
	}
}