package uk.co.bluegecko.utility.geo;

import static org.assertj.core.api.Assertions.assertThat;
import static systems.uom.ucum.UCUM.DEGREE;

import org.junit.jupiter.api.Test;
import tech.units.indriya.quantity.Quantities;

class BearingTest {

	@Test
	void lowerBound() {
		assertThat(new Bearing(10, 20, 30).getLower().getValue().intValue())
				.isEqualTo(0);
	}

	@Test
	void upperBound() {
		assertThat(new Bearing(10, 20, 30).getUpper().getValue().intValue())
				.isEqualTo(360);
	}

	@Test
	void create() {
		assertThat(new Bearing(10, 20, 30).create(20, 30, 40))
				.isInstanceOf(Bearing.class);
	}

	@Test
	void fromAngle() {
		assertThat(Bearing.fromAngle(Quantities.getQuantity(10.341666667, DEGREE)))
				.isEqualTo(new Bearing(10, 20, 30));
	}

	@Test
	void stringify() {
		assertThat(Bearing.fromAngle(Quantities.getQuantity(10.341666667, DEGREE)).toString())
				.isEqualTo("Bearing(degrees=10,minutes=20,seconds=30.0)");
	}

}