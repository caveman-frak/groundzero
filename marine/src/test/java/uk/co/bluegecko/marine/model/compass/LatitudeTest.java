package uk.co.bluegecko.marine.model.compass;

import static org.assertj.core.api.Assertions.assertThat;
import static systems.uom.ucum.UCUM.DEGREE;
import static systems.uom.ucum.UCUM.RADIAN;

import org.junit.jupiter.api.Test;
import tech.units.indriya.quantity.Quantities;
import tech.uom.lib.assertj.assertions.QuantityAssert;
import uk.co.bluegecko.marine.model.compass.Hemisphere.Spheriod;

class LatitudeTest {

	@Test
	void constructorQuantity() {
		QuantityAssert.assertThat(new Latitude(Quantities.getQuantity(10, DEGREE)))
				.hasUnit(DEGREE)
				.hasValue(10);
	}

	@Test
	void constructorDegrees() {
		QuantityAssert.assertThat(Latitude.asDegrees(10))
				.hasUnit(DEGREE)
				.hasValue(10)
				.isInstanceOf(Latitude.class);
	}

	@Test
	void constructorRadians() {
		QuantityAssert.assertThat(Latitude.asRadians(Math.PI / 4))
				.hasUnit(RADIAN)
				.hasValue(Math.PI / 4)
				.isInstanceOf(Latitude.class);
	}

	@Test
	void wrap() {
		QuantityAssert.assertThat(Latitude.asDegrees(10).wrap(Quantities.getQuantity(10, DEGREE)))
				.isInstanceOf(Latitude.class)
				.hasUnit(DEGREE)
				.hasValue(10);
	}

	@Test
	void limit() {
		assertThat(Latitude.asDegrees(10).getLimit()).isEqualTo(Limit.LATITUDE);
	}

	@Test
	void isSpheriod() {
		assertThat(Latitude.asDegrees(10)).isInstanceOf(Spheriod.class);
	}

}