package uk.co.bluegecko.utility.geo;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.withinPercentage;
import static systems.uom.ucum.UCUM.DEGREE;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;
import uk.co.bluegecko.common.test.InputOutputSource;

class CalculatorTest {

	private static final String POSITION_ARGUMENTS = """
			0,  0,  1,  0
			0,  0,  0,  1
			0,  0, -1,  0
			0,  0,  0, -1
			0,  0,  0,  45
			0,  0,  45, 0
			0,  0,  45, 45
			45, 0,  45, 45
			45, 0,  0,  45
			""";
	Calculator calculator;

	@BeforeEach
	void setUp() {
		calculator = new Calculator(new Context());
	}

	@ParameterizedTest
	@InputOutputSource(
			inputs = @CsvSource(textBlock = POSITION_ARGUMENTS),
			outputs = @ValueSource(doubles = {0.017453, 0.017453, 0.017453, 0.017453, 0.785398, 0.785398, 1.047197,
					0.548028, 1.047197}))
	void haversine(int startLat, int startLng, int endLat, int endLng, double radians) {
		assertThat(calculator.haversine(
				Math.toRadians(startLat), Math.toRadians(startLng),
				Math.toRadians(endLat), Math.toRadians(endLng)))
				.isCloseTo(radians, withinPercentage(0.01));
	}

	@ParameterizedTest
	@InputOutputSource(
			inputs = @CsvSource(textBlock = POSITION_ARGUMENTS),
			outputs = @ValueSource(doubles = {0.017453, 0.017453, 0.017453, 0.017453, 0.785398, 0.785398, 1.047197,
					0.548028, 1.047197}))
	void sphericalLawOfCosines(int startLat, int startLng, int endLat, int endLng, double radians) {
		assertThat(calculator.sphericalLawOfCosines(
				Math.toRadians(startLat), Math.toRadians(startLng),
				Math.toRadians(endLat), Math.toRadians(endLng)))
				.isCloseTo(radians, withinPercentage(0.01));
	}

	@ParameterizedTest
	@InputOutputSource(
			inputs = @CsvSource(textBlock = POSITION_ARGUMENTS),
			outputs = @ValueSource(ints = {1, 1, 1, 1, 45, 45, 60, 31, 60}))
	void calculateAngleDegrees(int startLat, int startLng, int endLat, int endLng, int degrees) {

		assertThat(Math.round(calculator.distance(
						coordinate(startLat, startLng),
						coordinate(endLat, endLng))
				.to(DEGREE).getValue().doubleValue()))
				.isEqualTo(degrees);
	}

	@ParameterizedTest
	@InputOutputSource(
			inputs = @CsvSource(textBlock = POSITION_ARGUMENTS),
			outputs = @ValueSource(doubles = {0, 1.570796, 3.141592, 1.570796, 1.570796, 0, 0.615479, 1.285872,
					2.186276}))
	void bearing(int startLat, int startLng, int endLat, int endLng, double radians) {
		assertThat(calculator.bearing(
				Math.toRadians(startLat), Math.toRadians(startLng),
				Math.toRadians(endLat), Math.toRadians(endLng)))
				.isCloseTo(radians, withinPercentage(0.01));
	}

	@ParameterizedTest
	@InputOutputSource(
			inputs = @CsvSource(textBlock = POSITION_ARGUMENTS),
			outputs = @ValueSource(ints = {0, 90, 180, 90, 90, 0, 35, 74, 125}))
	void calculateBearing(int startLat, int startLng, int endLat, int endLng, int degrees) {

		assertThat(Math.round(calculator.initialBearing(
						coordinate(startLat, startLng),
						coordinate(endLat, endLng))
				.decimal().getValue().doubleValue()))
				.isEqualTo(degrees);
	}

	private Coordinate coordinate(int latitude, int longitude) {
		return Coordinate.builder()
				.latitude(new Latitude(latitude, 0, 0))
				.longitude(new Longitude(longitude, 0, 0))
				.build();
	}

}