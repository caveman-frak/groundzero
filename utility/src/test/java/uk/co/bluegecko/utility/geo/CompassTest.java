package uk.co.bluegecko.utility.geo;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.offset;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junitpioneer.jupiter.params.IntRangeSource;

class CompassTest {

	@Test
	void cardinalPoints() {
		assertThat(Compass.cardinal())
				.containsExactly(Compass.NORTH, Compass.EAST, Compass.SOUTH, Compass.WEST);
	}

	@Test
	void cardinalEightWinds() {
		assertThat(Compass.eightWinds())
				.containsExactly(Compass.NORTH, Compass.NE, Compass.EAST, Compass.SE,
						Compass.SOUTH, Compass.SW, Compass.WEST, Compass.NW);
	}

	@Test
	void cardinalSixteenPoints() {
		assertThat(Compass.sixteenWinds())
				.hasSize(16)
				.containsExactly(Compass.values());
	}

	@Test
	void nearestPointTo180() {
		assertThat(Compass.nearestPoint(180.0)).isEqualTo(Compass.SOUTH);
	}

	@Test
	void nearestPointTo190() {
		assertThat(Compass.nearestPoint(190.0)).isEqualTo(Compass.SOUTH);
	}

	@Test
	void nearestPointTo210() {
		assertThat(Compass.nearestPoint(210.0)).isEqualTo(Compass.SSW);
	}

	@Test
	void nearestEightPointTo200() {
		assertThat(Compass.nearestPoint(210.0, Compass.eightWinds())).isEqualTo(Compass.SW);
	}

	@ParameterizedTest
	@IntRangeSource(from = 0, to = 16)
	void pointBearing(int index) {
		assertThat(Compass.values()[index].decimal())
				.describedAs("point %s should have bearing of %1.2f", Compass.values()[index], 22.5 * index)
				.isEqualTo(22.5 * index, offset(0.00001));
	}

}