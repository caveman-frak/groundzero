package uk.co.bluegecko.utility.validation;

import static org.assertj.core.api.Assertions.assertThat;
import static uk.co.bluegecko.common.model.invoice.InvoiceFixture.address;
import static uk.co.bluegecko.common.model.invoice.InvoiceFixture.invoice;
import static uk.co.bluegecko.common.model.invoice.InvoiceFixture.items;
import static uk.co.bluegecko.common.model.invoice.InvoiceFixture.lines;

import java.time.Clock;
import java.time.Instant;
import java.time.LocalDate;
import java.time.Month;
import java.time.ZoneOffset;
import java.util.List;
import java.util.Locale;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.Errors;
import org.springframework.validation.FieldError;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;
import uk.co.bluegecko.common.model.invoice.Invoice;
import uk.co.bluegecko.common.model.invoice.Invoice.InvoiceBuilder;

@SpringJUnitConfig
class InvoiceValidatorTest {

	@Autowired
	Clock clock;
	@Autowired
	MessageSource messageSource;

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

	@TestConfiguration
	static class Configuration {

		@Bean
		Clock clock() {
			return Clock.fixed(Instant.ofEpochSecond(961072210), ZoneOffset.UTC);
		}

		@Bean
		public MessageSource messageSource() {
			ReloadableResourceBundleMessageSource messageSource = new ReloadableResourceBundleMessageSource();
			messageSource.setBasename("classpath:messages/validation-invoice");
			messageSource.setDefaultEncoding("UTF-8");
			return messageSource;
		}

		@Bean
		public LocalValidatorFactoryBean getValidator() {
			LocalValidatorFactoryBean bean = new LocalValidatorFactoryBean();
			bean.setValidationMessageSource(messageSource());
			return bean;
		}

	}

	@NotNull
	private Errors validate(Invoice invoice) {
		Errors errors = new BeanPropertyBindingResult(invoice, "invoice");
		AddressValidator addressValidator = new AddressValidator();
		InvoiceValidator invoiceValidator = new InvoiceValidator(clock, addressValidator);
		invoiceValidator.validate(invoice, errors);
		return errors;
	}

}