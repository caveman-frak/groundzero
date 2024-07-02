package uk.co.bluegecko.utility.validation;

import static org.assertj.core.api.Assertions.assertThat;
import static uk.co.bluegecko.common.model.invoice.InvoiceFixture.address;
import static uk.co.bluegecko.common.model.invoice.InvoiceFixture.invoice;
import static uk.co.bluegecko.common.model.invoice.InvoiceFixture.items;
import static uk.co.bluegecko.common.model.invoice.InvoiceFixture.lines;

import java.time.LocalDate;
import java.time.Month;
import java.util.List;
import java.util.Locale;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.validation.Errors;
import org.springframework.validation.FieldError;
import uk.co.bluegecko.common.model.invoice.Invoice;
import uk.co.bluegecko.common.model.invoice.Invoice.InvoiceBuilder;

class InvoiceValidatorTest extends AbstractValidatorTest {

	@Test
	void invoiceValid() {
		Errors errors = validate(
				invoice(clock, address(), lines(items(1))).build());

		assertThat(errors.getFieldErrorCount()).isEqualTo(0);
	}

	@Test
	void invoiceNegativeNumber() {
		Errors errors = validate(
				invoice(clock, address(), lines(items(1)),
						i -> i.number(-1L)).build());

		assertThat(errors.getFieldErrorCount()).isEqualTo(1);
		FieldError error = errors.getFieldError("number");
		assertThat(error).isNotNull();
		assertThat(error.getRejectedValue()).isEqualTo(-1L);
		assertThat(messageSource.getMessage(error, Locale.UK))
				.isEqualTo("Invoice number must be positive.");
	}

	@ParameterizedTest
	@CsvSource({
			"en, Invoice number must be positive.",
			"de, Rechnungsnummer muss positiv sein.",
			"fr, Le numéro de facture doit être positif."
	})
	void invoiceNegativeNumberTranslated(Locale locale, String message) {
		Errors errors = validate(
				invoice(clock, address(), lines(items(1)),
						i -> i.number(-1L)).build());

		assertThat(messageSource.getMessage(errors.getFieldError("number"), locale))
				.isEqualTo(message);
	}

	@Test
	void invoiceDateInFuture() {
		Errors errors = validate(
				invoice(clock, address(), lines(items(1)),
						i -> i.date(LocalDate.now(clock).plusDays(10))).build());

		assertThat(errors.getFieldErrorCount()).isEqualTo(1);
		FieldError error = errors.getFieldError("date");
		assertThat(error).isNotNull();
		assertThat(error.getRejectedValue()).isEqualTo(LocalDate.of(2000, Month.JUNE, 25));
		assertThat(messageSource.getMessage(error, Locale.UK))
				.isEqualTo("Invoice date can not be in the future.");
	}

	@Test
	void invoiceMissingCustomer() {
		Errors errors = validate(
				invoice(clock, address(), lines(items(1)),
						i -> i.customer(null)).build());

		assertThat(errors.getFieldErrorCount()).isEqualTo(1);
		FieldError error = errors.getFieldError("customer");
		assertThat(error).isNotNull();
		assertThat(error.getRejectedValue()).isNull();
		assertThat(messageSource.getMessage(error, Locale.UK))
				.isEqualTo("Customer details are required.");
	}

	@Test
	void invoiceMissingLineItems() {
		Errors errors = validate(
				invoice(clock, address(), lines(items(1)),
						InvoiceBuilder::clearLines).build());

		assertThat(errors.getFieldErrorCount()).isEqualTo(1);
		FieldError error = errors.getFieldError("lines");
		assertThat(error).isNotNull();
		assertThat(error.getRejectedValue()).isEqualTo(List.of());
		assertThat(messageSource.getMessage(error, Locale.UK))
				.isEqualTo("Invoice line items are required.");
	}

	@Test
	void invoiceMissingCurrency() {
		Errors errors = validate(
				invoice(clock, address(), lines(items(1)),
						i -> i.currency(null)).build());

		assertThat(errors.getFieldErrorCount()).isEqualTo(1);
		FieldError error = errors.getFieldError("currency");
		assertThat(error).isNotNull();
		assertThat(error.getRejectedValue()).isNull();
		assertThat(messageSource.getMessage(error, Locale.UK))
				.isEqualTo("Currency is required.");
	}

	private Errors validate(Invoice invoice) {
		return validate(invoice, "invoice", new InvoiceValidator(clock, new AddressValidator()));
	}

}