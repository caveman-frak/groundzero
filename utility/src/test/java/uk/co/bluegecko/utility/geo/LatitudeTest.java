package uk.co.bluegecko.utility.geo;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

class LatitudeTest {

	@Test
	void lowerBound() {
		assertThat(new Latitude(10, 20, 30).getLower())
				.isEqualTo(-90);
	}

	@Test
	void upperBound() {
		assertThat(new Latitude(10, 20, 30).getUpper())
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
		assertThat(Latitude.fromDecimal(-10.341666667))
				.isEqualTo(new Latitude(10, 20, 30, Compass.S));
	}

	@Test
	void toDecimalReversed() {
		assertThat(new Latitude(10, 20, 30, Compass.S))
				.isEqualTo(Latitude.fromDecimal(-10.341666667));
	}

	@Test
	void fromDecimal() {
		assertThat(Latitude.fromDecimal(10.341666667))
				.isEqualTo(new Latitude(10, 20, 30));
	}

	@Test
	void stringify() {
		assertThat(Latitude.fromDecimal(10.341666667).toString())
				.isEqualTo("Latitude(degrees=10,minutes=20,seconds=30.0,direction=N)");
	}

}