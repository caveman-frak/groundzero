package uk.co.bluegecko.utility.validation;

import static org.assertj.core.api.Assertions.assertThat;
import static uk.co.bluegecko.common.model.invoice.InvoiceFixture.address;
import static uk.co.bluegecko.common.model.invoice.InvoiceFixture.invoice;
import static uk.co.bluegecko.common.model.invoice.InvoiceFixture.items;
import static uk.co.bluegecko.common.model.invoice.InvoiceFixture.lines;

import java.util.Locale;
import org.junit.jupiter.api.Test;
import org.springframework.validation.Errors;
import org.springframework.validation.FieldError;
import uk.co.bluegecko.common.model.invoice.Address;
import uk.co.bluegecko.common.model.invoice.Invoice;

public class AddressValidationTest extends AbstractValidatorTest {

	@Test
	void addressValid() {
		Errors errors = validate(
				address().build());

		assertThat(errors.getFieldErrorCount()).isEqualTo(0);
	}

	@Test
	void addressMissingName() {
		Errors errors = validate(
				address(a -> a.name(null)).build());

		assertThat(errors.getFieldErrorCount()).isEqualTo(1);
		FieldError error = errors.getFieldError("name");
		assertThat(error).isNotNull();
		assertThat(error.getRejectedValue()).isEqualTo(null);
		assertThat(messageSource.getMessage(error, Locale.UK))
				.isEqualTo("Name is required.");
	}

	@Test
	void invoiceMissingCustomerAddressName() {
		Errors errors = validate(
				invoice(clock, address(a -> a.name(null)), lines(items(1))).build());

		assertThat(errors.getFieldErrors()).hasSize(1);
		FieldError error = errors.getFieldError("customer.name");
		assertThat(error).isNotNull();
		assertThat(error.getRejectedValue()).isEqualTo(null);
		assertThat(messageSource.getMessage(error, Locale.UK))
				.isEqualTo("Customer name is required.");
	}

	@Test
	void addressMissingBuilding() {
		Errors errors = validate(
				address(a -> a.building(null)).build());

		assertThat(errors.getFieldErrorCount()).isEqualTo(1);
		FieldError error = errors.getFieldError("building");
		assertThat(error).isNotNull();
		assertThat(error.getRejectedValue()).isEqualTo(null);
		assertThat(messageSource.getMessage(error, Locale.UK))
				.isEqualTo("Building is required.");
	}

	@Test
	void addressMissingStreet() {
		Errors errors = validate(
				address(a -> a.street(null)).build());

		assertThat(errors.getFieldErrorCount()).isEqualTo(1);
		FieldError error = errors.getFieldError("street");
		assertThat(error).isNotNull();
		assertThat(error.getRejectedValue()).isEqualTo(null);
		assertThat(messageSource.getMessage(error, Locale.UK))
				.isEqualTo("Street is required.");
	}

	@Test
	void addressMissingTown() {
		Errors errors = validate(
				address(a -> a.town(null)).build());

		assertThat(errors.getFieldErrorCount()).isEqualTo(1);
		FieldError error = errors.getFieldError("town");
		assertThat(error).isNotNull();
		assertThat(error.getRejectedValue()).isEqualTo(null);
		assertThat(messageSource.getMessage(error, Locale.UK))
				.isEqualTo("Town is required.");
	}

	@Test
	void addressMissingPostcode() {
		Errors errors = validate(
				address(a -> a.postcode(null)).build());

		assertThat(errors.getFieldErrorCount()).isEqualTo(1);
		FieldError error = errors.getFieldError("postcode");
		assertThat(error).isNotNull();
		assertThat(error.getRejectedValue()).isEqualTo(null);
		assertThat(messageSource.getMessage(error, Locale.UK))
				.isEqualTo("Postcode is required.");
	}

	@Test
	void addressShortPostcode() {
		Errors errors = validate(
				address(a -> a.postcode("L1 3A")).build());

		assertThat(errors.getFieldErrors()).hasSize(1);
		FieldError error = errors.getFieldError("postcode");
		assertThat(error).isNotNull();
		assertThat(error.getRejectedValue()).isEqualTo("L1 3A");
		assertThat(messageSource.getMessage(error, Locale.UK))
				.isEqualTo("Postcode length is invalid.");
	}

	private Errors validate(Invoice invoice) {
		return validate(invoice, "invoice", new InvoiceValidator(clock, new AddressValidator()));
	}

	private Errors validate(Address address) {
		return validate(address, "address", new AddressValidator());
	}
}