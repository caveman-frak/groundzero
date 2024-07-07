package uk.co.bluegecko.utility.geo;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

class BearingTest {

	@Test
	void lowerBound() {
		assertThat(new Bearing(10, 20, 30).getLower())
				.isEqualTo(0);
	}

	@Test
	void upperBound() {
		assertThat(new Bearing(10, 20, 30).getUpper())
				.isEqualTo(360);
	}

	@Test
	void create() {
		assertThat(new Bearing(10, 20, 30).create(20, 30, 40))
				.isInstanceOf(Bearing.class);
	}

	@Test
	void fromDecimal() {
		assertThat(Bearing.fromDecimal(10.341666667))
				.isEqualTo(new Bearing(10, 20, 30));
	}

	@Test
	void stringify() {
		assertThat(Bearing.fromDecimal(10.341666667).toString())
				.isEqualTo("Bearing(degrees=10,minutes=20,seconds=30.0)");
	}

}