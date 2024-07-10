package uk.co.bluegecko.utility.geo;

import static org.assertj.core.api.Assertions.assertThat;
import static systems.uom.ucum.UCUM.DEGREE;

import org.junit.jupiter.api.Test;
import tech.units.indriya.quantity.Quantities;

class LatitudeTest {

	@Test
	void lowerBound() {
		assertThat(new Latitude(10, 20, 30).getLower().getValue().intValue())
				.isEqualTo(-90);
	}

	@Test
	void upperBound() {
		assertThat(new Latitude(10, 20, 30).getUpper().getValue().intValue())
				.isEqualTo(90);
	}

	@Test
	void withDirectionNorth() {
		assertThat(new Latitude(10, 20, 30, Compass.N))
				.isEqualTo(new Latitude(10, 20, 30));
	}

	@Test
	void withDirectionSouth() {
		assertThat(new Latitude(10, 20, 30, Compass.S))
				.isEqualTo(new Latitude(-10, 20, 30));
	}

	@Test
	void create() {
		assertThat(new Latitude(10, 20, 30).create(20, 30, 40))
				.isInstanceOf(Latitude.class);
	}

	@Test
	void deriveHemisphere() {
		assertThat(Latitude.fromAngle(Quantities.getQuantity(-10.341666667, DEGREE)))
				.isEqualTo(new Latitude(10, 20, 30, Compass.S));
	}

	@Test
	void toDecimalReversed() {
		assertThat(new Latitude(10, 20, 30, Compass.S))
				.isEqualTo(Latitude.fromAngle(Quantities.getQuantity(-10.341666667, DEGREE)));
	}

	@Test
	void fromAngle() {
		assertThat(Latitude.fromAngle(Quantities.getQuantity(10.341666667, DEGREE)))
				.isEqualTo(new Latitude(10, 20, 30));
	}

	@Test
	void stringify() {
		assertThat(Latitude.fromAngle(Quantities.getQuantity(10.341666667, DEGREE)).toString())
				.isEqualTo("Latitude(degrees=10,minutes=20,seconds=30.0,hemisphere=N)");
	}

}