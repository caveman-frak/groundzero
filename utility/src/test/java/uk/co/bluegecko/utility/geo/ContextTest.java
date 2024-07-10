package uk.co.bluegecko.utility.geo;


import static javax.measure.MetricPrefix.KILO;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.within;
import static systems.uom.ucum.UCUM.METER;
import static systems.uom.ucum.UCUM.MILE_BRITISH;
import static systems.uom.ucum.UCUM.NAUTICAL_MILE_BRITISH;

import javax.measure.Quantity;
import javax.measure.quantity.Length;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import tech.uom.lib.assertj.Assertions;

class ContextTest {

	Context ctx;

	@BeforeEach
	void setUp() {
		ctx = new Context();
	}

	@Test
	void equatorialRadius() {
		Assertions.assertThat(ctx.equatorialRadius())
				.hasUnit(METER)
				.hasValue(6_378_137);
	}

	@Test
	void equatorialRadiusInKilometers() {
		Quantity<Length> actual = ctx.equatorialRadius().to(KILO(METER));
		Assertions.assertThat(actual).hasUnit(KILO(METER));
		assertThat(actual.getValue().doubleValue()).isEqualTo(6_378.137, within(0.00001));
	}

	@Test
	void equatorialRadiusInMiles() {
		Quantity<Length> actual = ctx.equatorialRadius().to(MILE_BRITISH);
		Assertions.assertThat(actual).hasUnit(MILE_BRITISH);
		assertThat(actual.getValue().doubleValue()).isEqualTo(3_963.19371, within(0.00001));
	}

	@Test
	void equatorialRadiusInNauticalMiles() {
		Quantity<Length> actual = ctx.equatorialRadius().to(NAUTICAL_MILE_BRITISH);
		Assertions.assertThat(actual).hasUnit(NAUTICAL_MILE_BRITISH);
		assertThat(actual.getValue().doubleValue()).isEqualTo(3_441.72085, within(0.00001));
	}

	@Test
	void polarRadius() {
		Quantity<Length> actual = ctx.polarRadius();
		Assertions.assertThat(actual).hasUnit(METER);
		assertThat(actual.getValue().doubleValue()).isEqualTo(6_356_752.314140, within(0.00001));
	}

	@Test
	void polarRadiusInKilometers() {
		Quantity<Length> actual = ctx.polarRadius().to(KILO(METER));
		Assertions.assertThat(actual).hasUnit(KILO(METER));
		assertThat(actual.getValue().doubleValue()).isEqualTo(6_356.75231, within(0.00001));
	}

	@Test
	void polarRadiusInMiles() {
		Quantity<Length> actual = ctx.polarRadius().to(MILE_BRITISH);
		Assertions.assertThat(actual).hasUnit(MILE_BRITISH);
		assertThat(actual.getValue().doubleValue()).isEqualTo(3_949.90587, within(0.00001));
	}

	@Test
	void polarRadiusInNauticalMiles() {
		Quantity<Length> actual = ctx.polarRadius().to(NAUTICAL_MILE_BRITISH);
		Assertions.assertThat(actual).hasUnit(NAUTICAL_MILE_BRITISH);
		assertThat(actual.getValue().doubleValue()).isEqualTo(3_430.18142, within(0.00001));
	}

}