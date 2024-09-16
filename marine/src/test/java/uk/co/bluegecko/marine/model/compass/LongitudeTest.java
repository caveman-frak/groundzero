package uk.co.bluegecko.marine.model.compass;

import static org.assertj.core.api.Assertions.assertThat;
import static systems.uom.ucum.UCUM.DEGREE;

import org.junit.jupiter.api.Test;
import tech.units.indriya.quantity.Quantities;
import tech.uom.lib.assertj.assertions.QuantityAssert;
import uk.co.bluegecko.marine.model.compass.Hemisphere.Spheriod;

class LongitudeTest {

	@Test
	void constructorQuantity() {
		QuantityAssert.assertThat(new Longitude(Quantities.getQuantity(10, DEGREE)))
				.hasUnit(DEGREE)
				.hasValue(10);
	}

	@Test
	void wrap() {
		QuantityAssert.assertThat(Longitude.asDegrees(10).wrap(Quantities.getQuantity(10, DEGREE)))
				.isInstanceOf(Longitude.class)
				.hasUnit(DEGREE)
				.hasValue(10);
	}

	@Test
	void limit() {
		assertThat(Longitude.asDegrees(10).getLimit()).isEqualTo(Limit.LONGITUDE);
	}

	@Test
	void isSpheriod() {
		assertThat(Longitude.asDegrees(10)).isInstanceOf(Spheriod.class);
	}

}