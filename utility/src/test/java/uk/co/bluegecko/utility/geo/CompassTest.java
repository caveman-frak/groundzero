package uk.co.bluegecko.utility.geo;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.offset;
import static systems.uom.ucum.UCUM.DEGREE;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junitpioneer.jupiter.params.IntRangeSource;
import tech.units.indriya.quantity.Quantities;

class CompassTest {

	@Test
	void cardinalPoints() {
		assertThat(Compass.cardinal())
				.containsExactly(Compass.N, Compass.E, Compass.S, Compass.W);
	}

	@Test
	void cardinalEightWinds() {
		assertThat(Compass.eightWinds())
				.containsExactly(Compass.N, Compass.NE, Compass.E, Compass.SE,
						Compass.S, Compass.SW, Compass.W, Compass.NW);
	}

	@Test
	void cardinalSixteenPoints() {
		assertThat(Compass.sixteenWinds())
				.hasSize(16)
				.containsExactly(Compass.values());
	}

	@Test
	void latitudePoints() {
		assertThat(Compass.latitude())
				.containsExactly(Compass.N, Compass.S);
	}

	@Test
	void longitudePoints() {
		assertThat(Compass.longitude())
				.containsExactly(Compass.E, Compass.W);
	}

	@Test
	void nearestPointTo180() {
		assertThat(Compass.nearestPoint(Quantities.getQuantity(180.0, DEGREE))).isEqualTo(Compass.S);
	}

	@Test
	void nearestPointTo190() {
		assertThat(Compass.nearestPoint(Quantities.getQuantity(190.0, DEGREE))).isEqualTo(Compass.S);
	}

	@Test
	void nearestPointTo210() {
		assertThat(Compass.nearestPoint(Quantities.getQuantity(210.0, DEGREE))).isEqualTo(Compass.SSW);
	}

	@Test
	void nearestEightPointTo200() {
		assertThat(Compass.nearestPoint(Quantities.getQuantity(210.0, DEGREE), Compass.eightWinds())).isEqualTo(
				Compass.SW);
	}

	@ParameterizedTest
	@IntRangeSource(from = 0, to = 16)
	void pointBearing(int index) {
		assertThat(Compass.values()[index].decimal().getValue().doubleValue())
				.describedAs("point %s should have bearing of %1.2f", Compass.values()[index], 22.5 * index)
				.isEqualTo(22.5 * index, offset(0.00001));
	}

}