package uk.co.bluegecko.utility.validation;

import static org.assertj.core.api.Assertions.assertThat;
import static uk.co.bluegecko.common.model.invoice.InvoiceFixture.item;

import java.math.BigDecimal;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import org.junit.jupiter.api.Test;
import org.springframework.validation.Errors;
import org.springframework.validation.FieldError;
import uk.co.bluegecko.common.model.invoice.Item;

public class ItemValidationTest extends AbstractValidatorTest {

	@Test
	void itemValid() {
		Errors errors = validate(
				item(1).build());

		assertThat(errors.getFieldErrorCount()).isEqualTo(0);
	}

	@Test
	void itemMissingId() {
		Errors errors = validate(
				item(1, (n, i) -> i.identifier(null)).build());

		assertThat(errors.getFieldErrors()).hasSize(1);
		FieldError error = errors.getFieldError("identifier");
		assertThat(error).isNotNull();
		assertThat(error.getRejectedValue()).isEqualTo(null);
		assertThat(messageSource.getMessage(error, Locale.UK))
				.isEqualTo("Identifier is required.");
	}

	@Test
	void itemMissingShortName() {
		Errors errors = validate(
				item(1, (n, i) -> i.shortName(null)).build());

		assertThat(errors.getFieldErrors()).hasSize(1);
		FieldError error = errors.getFieldError("shortName");
		assertThat(error).isNotNull();
		assertThat(error.getRejectedValue()).isEqualTo(null);
		assertThat(messageSource.getMessage(error, Locale.UK))
				.isEqualTo("Short name is required.");
	}

	@Test
	void itemShortNameTooLong() {
		Errors errors = validate(
				item(1, (n, i) -> i.shortName("1234567890123456789012345678901")).build());

		assertThat(errors.getFieldErrors()).hasSize(1);
		FieldError error = errors.getFieldError("shortName");
		assertThat(error).isNotNull();
		assertThat(error.getRejectedValue()).isEqualTo("1234567890123456789012345678901");
		assertThat(messageSource.getMessage(error, Locale.UK))
				.isEqualTo("Short name must be between 3 and 30 characters.");
	}

	@Test
	void itemDescriptionTooShort() {
		Errors errors = validate(
				item(1, (n, i) -> i.description("12")).build());

		assertThat(errors.getFieldErrors()).hasSize(1);
		FieldError error = errors.getFieldError("description");
		assertThat(error).isNotNull();
		assertThat(error.getRejectedValue()).isEqualTo("12");
		assertThat(messageSource.getMessage(error, Locale.UK))
				.isEqualTo("Description must be between 3 and 100 characters.");
	}

	@Test
	void itemMissingPrice() {
		Errors errors = validate(
				item(1, (n, i) -> i.price(null)).build());

		assertThat(errors.getFieldErrors()).hasSize(1);
		FieldError error = errors.getFieldError("price");
		assertThat(error).isNotNull();
		assertThat(error.getRejectedValue()).isEqualTo(null);
		assertThat(messageSource.getMessage(error, Locale.UK))
				.isEqualTo("Price is required.");
	}

	@Test
	void itemPriceNegative() {
		Errors errors = validate(
				item(1, (n, i) -> i.price(BigDecimal.valueOf(-9.95))).build());

		assertThat(errors.getFieldErrors()).hasSize(2);
		List<FieldError> priceErrors = errors.getFieldErrors("price");
		assertThat(priceErrors).hasSize(2);
		FieldError priceRange = priceErrors.get(0);
		assertThat(priceRange.getRejectedValue()).isEqualTo(BigDecimal.valueOf(-9.95));
		FieldError pricePositive = priceErrors.get(1);
		assertThat(List.of(messageSource.getMessage(priceRange, Locale.UK),
				messageSource.getMessage(pricePositive, Locale.UK)))
				.contains("Price must be positive.", "Price must be between 0 and 999,999.");
	}

	@Test
	void itemPriceTooLarge() {
		Errors errors = validate(
				item(1, (n, i) -> i.price(BigDecimal.valueOf(1_000_000.01))).build());

		assertThat(errors.getFieldErrors()).hasSize(1);
		FieldError error = errors.getFieldError("price");
		assertThat(error).isNotNull();
		assertThat(error.getRejectedValue()).isEqualTo(BigDecimal.valueOf(1_000_000.01));
		assertThat(messageSource.getMessage(error, Locale.UK))
				.isEqualTo("Price must be between 0 and 999,999.");
	}

	@Test
	@SuppressWarnings({"unchecked", "rawtypes"})
	void itemInvalidCustomisation() {
		Errors errors = validate(
				item(1, (n, i) -> i.customisation("Foo", "Bar")).build());

		assertThat(errors.getFieldErrors()).hasSize(1);
		FieldError error = errors.getFieldError("customisations");
		assertThat(error).isNotNull();
		assertThat((Map) error.getRejectedValue()).contains(Map.entry("Foo", "Bar"));
		assertThat(messageSource.getMessage(error, Locale.UK))
				.isEqualTo("Customisation type \"Foo\" is not valid.");
	}

	private Errors validate(Item item) {
		return validate(item, "item", new ItemValidator());
	}
}