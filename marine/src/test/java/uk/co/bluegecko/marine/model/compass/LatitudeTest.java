package uk.co.bluegecko.marine.model.compass;

import static org.assertj.core.api.Assertions.assertThat;
import static systems.uom.ucum.UCUM.DEGREE;

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
	void wrap() {
		QuantityAssert.assertThat(new Latitude(10).wrap(Quantities.getQuantity(10, DEGREE)))
				.isInstanceOf(Latitude.class)
				.hasUnit(DEGREE)
				.hasValue(10);
	}

	@Test
	void limit() {
		assertThat(new Latitude(10).getLimit()).isEqualTo(Limit.LATITUDE);
	}

	@Test
	void isSpheriod() {
		assertThat(new Latitude(10)).isInstanceOf(Spheriod.class);
	}

}