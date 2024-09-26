package uk.co.bluegecko.marine.model.position.partition;

import static com.uber.h3core.AreaUnit.km2;
import static com.uber.h3core.AreaUnit.m2;
import static com.uber.h3core.LengthUnit.km;
import static com.uber.h3core.LengthUnit.m;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.data.Percentage.withPercentage;
import static uk.co.bluegecko.marine.model.position.partition.Resolution.COARSE;
import static uk.co.bluegecko.marine.model.position.partition.Resolution.COARSEST;
import static uk.co.bluegecko.marine.model.position.partition.Resolution.FINE;
import static uk.co.bluegecko.marine.model.position.partition.Resolution.FINER;
import static uk.co.bluegecko.marine.model.position.partition.Resolution.FINEST;
import static uk.co.bluegecko.marine.model.position.partition.Resolution.MEDIUM;

import com.uber.h3core.H3Core;
import java.io.IOException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ResolutionTest {

	H3Core core;

	@BeforeEach
	void setUp() throws IOException {
		core = H3Core.newInstance();
	}

	@Test
	void finest() {
		assertThat(core.getHexagonEdgeLengthAvg(FINEST.h3(), m)).isCloseTo(65.90, withPercentage(1));
		assertThat(core.getHexagonAreaAvg(FINEST.h3(), m2)).isCloseTo(15_040, withPercentage(1));
	}

	@Test
	void finer() {
		assertThat(core.getHexagonEdgeLengthAvg(FINER.h3(), m)).isCloseTo(461.3, withPercentage(1));
		assertThat(core.getHexagonAreaAvg(FINER.h3(), km2)).isCloseTo(0.7372, withPercentage(1));
	}

	@Test
	void fine() {
		assertThat(core.getHexagonEdgeLengthAvg(FINE.h3(), km)).isCloseTo(3.229, withPercentage(1));
		assertThat(core.getHexagonAreaAvg(FINE.h3(), km2)).isCloseTo(36.12, withPercentage(1));
	}

	@Test
	void medium() {
		assertThat(core.getHexagonEdgeLengthAvg(MEDIUM.h3(), km)).isCloseTo(22.60, withPercentage(1));
		assertThat(core.getHexagonAreaAvg(MEDIUM.h3(), km2)).isCloseTo(1770, withPercentage(1));
	}

	@Test
	void coarse() {
		assertThat(core.getHexagonEdgeLengthAvg(COARSE.h3(), km)).isCloseTo(158.2, withPercentage(1));
		assertThat(core.getHexagonAreaAvg(COARSE.h3(), km2)).isCloseTo(8_6800, withPercentage(1));
	}

	@Test
	void coarsest() {
		assertThat(core.getHexagonEdgeLengthAvg(COARSEST.h3(), km)).isCloseTo(1107, withPercentage(1));
		assertThat(core.getHexagonAreaAvg(COARSEST.h3(), km2)).isCloseTo(4_357_000, withPercentage(1));
	}

	@Test
	void finestToFiner() {
		assertThat(compareTime(FINEST, Resolution.FINER)).isEqualTo(6.0);
		assertThat(compareSide(FINEST, Resolution.FINER)).isCloseTo(7.0, withPercentage(1));
		assertThat(compareArea(FINEST, Resolution.FINER)).isCloseTo(49.0, withPercentage(1));
	}

	@Test
	void finerToFine() {
		assertThat(compareTime(Resolution.FINER, FINE)).isCloseTo(1.6667, withPercentage(1));
		assertThat(compareSide(Resolution.FINER, FINE)).isCloseTo(7.0, withPercentage(1));
		assertThat(compareArea(Resolution.FINER, FINE)).isCloseTo(49.0, withPercentage(1));
	}

	@Test
	void fineToMedium() {
		assertThat(compareTime(FINE, Resolution.MEDIUM)).isEqualTo(6.0);
		assertThat(compareSide(FINE, Resolution.MEDIUM)).isCloseTo(7.0, withPercentage(1));
		assertThat(compareArea(FINE, Resolution.MEDIUM)).isCloseTo(49.0, withPercentage(1));
	}

	@Test
	void mediumToCoarse() {
		assertThat(compareTime(Resolution.MEDIUM, Resolution.COARSE)).isEqualTo(6.0);
		assertThat(compareSide(Resolution.MEDIUM, Resolution.COARSE)).isCloseTo(7.0, withPercentage(1));
		assertThat(compareArea(Resolution.MEDIUM, Resolution.COARSE)).isCloseTo(49.0, withPercentage(1));
	}

	@Test
	void coarseToCoarsest() {
		assertThat(compareTime(Resolution.COARSE, Resolution.COARSEST)).isEqualTo(4.0);
		assertThat(compareSide(Resolution.COARSE, Resolution.COARSEST)).isCloseTo(7.0, withPercentage(1));
		assertThat(compareArea(Resolution.COARSE, Resolution.COARSEST)).isCloseTo(50.0, withPercentage(1));
	}

	private double compareTime(Resolution r1, Resolution r2) {
		return (double) r2.millis() / r1.millis();
	}

	private double compareSide(Resolution r1, Resolution r2) {
		return core.getHexagonEdgeLengthAvg(r2.h3(), km) / core.getHexagonEdgeLengthAvg(r1.h3(),
				km);
	}

	private double compareArea(Resolution r1, Resolution r2) {
		return core.getHexagonAreaAvg(r2.h3(), km2) / core.getHexagonAreaAvg(r1.h3(), km2);
	}


}