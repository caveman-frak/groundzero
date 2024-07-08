package uk.co.bluegecko.utility.geo;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

class LongitudeTest {

	@Test
	void lowerBound() {
		assertThat(new Longitude(10, 20, 30).getLower())
				.isEqualTo(-180);
	}

	@Test
	void upperBound() {
		assertThat(new Longitude(10, 20, 30).getUpper())
				.isEqualTo(180);
	}

	@Test
	void withDirectionEast() {
		assertThat(new Longitude(10, 20, 30, Compass.E))
				.isEqualTo(new Longitude(10, 20, 30));
	}

	@Test
	void withDirectionWest() {
		assertThat(new Longitude(10, 20, 30, Compass.W))
				.isEqualTo(new Longitude(-10, 20, 30));
	}

	@Test
	void create() {
		assertThat(new Longitude(10, 20, 30).create(20, 30, 40))
				.isInstanceOf(Longitude.class);
	}

	@Test
	void deriveHemisphere() {
		assertThat(Longitude.fromDecimal(-10.341666667))
				.isEqualTo(new Longitude(10, 20, 30, Compass.W));
	}

	@Test
	void toDecimalReversed() {
		assertThat(new Longitude(10, 20, 30, Compass.W))
				.isEqualTo(Longitude.fromDecimal(-10.341666667));
	}

	@Test
	void fromDecimal() {
		assertThat(Longitude.fromDecimal(10.341666667))
				.isEqualTo(new Longitude(10, 20, 30));
	}

	@Test
	void stringify() {
		assertThat(Longitude.fromDecimal(10.341666667).toString())
				.isEqualTo("Longitude(degrees=10,minutes=20,seconds=30.0,hemisphere=E)");
	}

}