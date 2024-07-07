package uk.co.bluegecko.utility.geo.validate;

import static org.assertj.core.api.Assertions.assertThat;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.constraints.NotNull;
import java.util.Set;
import lombok.Builder;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import uk.co.bluegecko.utility.geo.Compass;

class AllowedCompassPointValidatorTest {

	private static Validator validator;

	@BeforeAll
	public static void setUp() {
		validator = Validation.buildDefaultValidatorFactory().getValidator();
	}

	@Test
	void validLatitude() {
		Foo foo = Foo.builder().latitude(Compass.N).build();

		Set<ConstraintViolation<Foo>> violations = validator.validate(foo);
		assertThat(violations).isEmpty();
	}

	@Test
	void validLatitudeAndLongitude() {
		Foo foo = Foo.builder().latitude(Compass.N).longitude(Compass.W).build();

		Set<ConstraintViolation<Foo>> violations = validator.validate(foo);
		assertThat(violations).isEmpty();
	}

	@Test
	void missingLatitude() {
		Foo foo = Foo.builder().build();

		Set<ConstraintViolation<Foo>> violations = validator.validate(foo);
		assertThat(violations).hasSize(1)
				.anyMatch(v -> v.getPropertyPath().toString().equals("latitude"))
				.anyMatch(v -> v.getMessage().equals("must not be null"));
	}

	@Test
	void invalidLatitude() {
		Foo foo = Foo.builder().latitude(Compass.NE).build();

		Set<ConstraintViolation<Foo>> violations = validator.validate(foo);
		assertThat(violations).hasSize(1)
				.anyMatch(v -> v.getPropertyPath().toString().equals("latitude"))
				.anyMatch(v -> v.getMessage().equals("must be one of [N, S]"));
	}

	@Test
	void invalidLongitude() {
		Foo foo = Foo.builder().latitude(Compass.N).longitude(Compass.NE).build();

		Set<ConstraintViolation<Foo>> violations = validator.validate(foo);
		assertThat(violations).hasSize(1)
				.anyMatch(v -> v.getPropertyPath().toString().equals("longitude"))
				.anyMatch(v -> v.getMessage().equals("must be one of [E, W]"));
	}

	@Builder
	private static class Foo {

		@NotNull
		@AllowedCompassPoint(oneOf = {Compass.N, Compass.S})
		Compass latitude;

		@AllowedCompassPoint(oneOf = {Compass.E, Compass.W})
		Compass longitude;

	}

}