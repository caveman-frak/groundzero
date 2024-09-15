package uk.co.bluegecko.marine.model.compass;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.offset;
import static systems.uom.ucum.UCUM.DEGREE;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junitpioneer.jupiter.params.IntRangeSource;
import tech.units.indriya.quantity.Quantities;

class PointTest {

	@Test
	void cardinalPoints() {
		assertThat(Point.cardinal())
				.containsExactly(Point.N, Point.E, Point.S, Point.W);
	}

	@Test
	void cardinalEightWinds() {
		assertThat(Point.eightWinds())
				.containsExactly(Point.N, Point.NE, Point.E, Point.SE,
						Point.S, Point.SW, Point.W, Point.NW);
	}

	@Test
	void cardinalSixteenPoints() {
		assertThat(Point.sixteenWinds())
				.hasSize(16)
				.containsExactly(Point.values());
	}

	@Test
	void latitudePoints() {
		assertThat(Point.latitude())
				.containsExactly(Point.N, Point.S);
	}

	@Test
	void longitudePoints() {
		assertThat(Point.longitude())
				.containsExactly(Point.E, Point.W);
	}

	@Test
	void nearestPointTo180() {
		assertThat(Point.nearest(Quantities.getQuantity(180.0, DEGREE))).isEqualTo(Point.S);
	}

	@Test
	void nearestPointTo190() {
		assertThat(Point.nearest(Quantities.getQuantity(190.0, DEGREE))).isEqualTo(Point.S);
	}

	@Test
	void nearestPointTo210() {
		assertThat(Point.nearest(Quantities.getQuantity(210.0, DEGREE))).isEqualTo(Point.SSW);
	}

	@Test
	void nearestEightPointTo200() {
		assertThat(Point.nearest(Quantities.getQuantity(210.0, DEGREE), Point.eightWinds())).isEqualTo(
				Point.SW);
	}

	@ParameterizedTest
	@IntRangeSource(from = 0, to = 16)
	void pointBearing(int index) {
		assertThat(Point.values()[index].bearing().getValue().doubleValue())
				.describedAs("point %s should have bearing of %1.2f", Point.values()[index], 22.5 * index)
				.isEqualTo(22.5 * index, offset(0.00001));
	}

}