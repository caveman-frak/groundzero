package uk.co.bluegecko.marine.model.compass;

import static org.assertj.core.api.Assertions.assertThat;
import static systems.uom.ucum.UCUM.DEGREE;
import static systems.uom.ucum.UCUM.RADIAN;

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
		QuantityAssert.assertThat(Bearing.asDegrees(10))
				.hasUnit(DEGREE)
				.hasValue(10)
				.isInstanceOf(Bearing.class);
	}

	@Test
	void constructorRadians() {
		QuantityAssert.assertThat(Bearing.asRadians(Math.PI))
				.hasUnit(RADIAN)
				.hasValue(Math.PI)
				.isInstanceOf(Bearing.class);
	}

	@Test
	void constructorDegreesAndMinutes() {
		QuantityAssert.assertThat(Bearing.asDegreeMinute(10, 30))
				.hasUnit(DEGREE)
				.hasValue(10.5)
				.isInstanceOf(Bearing.class);
	}

	@Test
	void constructorDegreesMinutesAndSeconds() {
		QuantityAssert.assertThat(Bearing.asDegreeMinuteSecond(10, 30, 30))
				.hasUnit(DEGREE)
				.hasValue(10.508333333333333)
				.isInstanceOf(Bearing.class);
	}

	@Test
	void wrap() {
		QuantityAssert.assertThat(Bearing.asDegrees(10).wrap(Quantities.getQuantity(10, DEGREE)))
				.isInstanceOf(Bearing.class)
				.hasUnit(DEGREE)
				.hasValue(10);
	}

	@Test
	void limit() {
		assertThat(Bearing.asDegrees(10).getLimit()).isEqualTo(Limit.BEARING);
	}

	@Test
	void isSpheriod() {
		assertThat(Bearing.asDegrees(10)).isNotInstanceOf(Spheriod.class);
	}

}