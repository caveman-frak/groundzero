package uk.co.bluegecko.marine.model.compass;

import static org.assertj.core.api.Assertions.assertThat;
import static systems.uom.ucum.UCUM.DEGREE;

import org.junit.jupiter.api.Test;
import tech.units.indriya.quantity.Quantities;
import tech.uom.lib.assertj.assertions.QuantityAssert;
import uk.co.bluegecko.marine.model.compass.Hemisphere.Spheriod;

class BearingTest {

	@Test
	void constructorQuantity() {
		QuantityAssert.assertThat(new Bearing(Quantities.getQuantity(10, DEGREE)))
				.hasUnit(DEGREE)
				.hasValue(10);
	}

	@Test
	void constructorDegrees() {
		QuantityAssert.assertThat(new Bearing(10))
				.hasUnit(DEGREE)
				.hasValue(10);
	}

	@Test
	void constructorDegreesAndMinutes() {
		QuantityAssert.assertThat(new Bearing(10, 30))
				.hasUnit(DEGREE)
				.hasValue(10.5);
	}

	@Test
	void constructorDegreesMinutesAndSeconds() {
		QuantityAssert.assertThat(new Bearing(10, 30, 30))
				.hasUnit(DEGREE)
				.hasValue(10.508333333333333);
	}

	@Test
	void wrap() {
		QuantityAssert.assertThat(new Bearing(10).wrap(Quantities.getQuantity(10, DEGREE)))
				.isInstanceOf(Bearing.class)
				.hasUnit(DEGREE)
				.hasValue(10);
	}

	@Test
	void limit() {
		assertThat(new Bearing(10).getLimit()).isEqualTo(Limit.BEARING);
	}

	@Test
	void isSpheriod() {
		assertThat(new Bearing(10)).isNotInstanceOf(Spheriod.class);
	}

}