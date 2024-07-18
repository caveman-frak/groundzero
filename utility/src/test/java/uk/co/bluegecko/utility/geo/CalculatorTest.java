package uk.co.bluegecko.utility.geo;

import static javax.measure.MetricPrefix.KILO;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.within;
import static org.assertj.core.api.Assertions.withinPercentage;
import static si.uom.NonSI.NAUTICAL_MILE;
import static systems.uom.ucum.UCUM.DEGREE;
import static systems.uom.ucum.UCUM.METER;

import java.awt.geom.Point2D;
import java.text.DecimalFormat;
import org.assertj.core.presentation.StandardRepresentation;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;
import tech.units.indriya.quantity.Quantities;
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

	@BeforeAll
	static void setUpFormatters() {
		StandardRepresentation.registerFormatterForType(Double.class,
				n -> new DecimalFormat("0.0###############").format(n));
	}

	@AfterAll
	static void tearDownFormatters() {
		StandardRepresentation.removeAllRegisteredFormatters();
	}

	@BeforeEach
	void setUp() {
		calculator = new Calculator(new Context());
	}

	@ParameterizedTest
	@InputOutputSource(
			inputs = @CsvSource(textBlock = POSITION_ARGUMENTS),
			outputs = @ValueSource(doubles =
					{0.017453, 0.017453, 0.017453, 0.017453, 0.785398, 0.785398, 1.047197, 0.548028, 1.047197}))
	void haversine(int startLat, int startLng, int endLat, int endLng, double radians) {
		assertThat(calculator.haversine(
				Math.toRadians(startLat), Math.toRadians(startLng),
				Math.toRadians(endLat), Math.toRadians(endLng)))
				.isCloseTo(radians, withinPercentage(0.01));
	}

	@ParameterizedTest
	@InputOutputSource(
			inputs = @CsvSource(textBlock = POSITION_ARGUMENTS),
			outputs = @ValueSource(doubles =
					{0.017453, 0.017453, 0.017453, 0.017453, 0.785398, 0.785398, 1.047197, 0.548028, 1.047197}))
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
			outputs = @ValueSource(ints = {60, 60, 60, 60, 2705, 2705, 3606, 1887, 3606}))
	void calculateDistanceNauticalMiles(int startLat, int startLng, int endLat, int endLng, int nauticalMiles) {
		assertThat(Math.round(calculator.distance(
						coordinate(startLat, startLng),
						coordinate(endLat, endLng),
						NAUTICAL_MILE)
				.getValue().doubleValue()))
				.isEqualTo(nauticalMiles);
	}

	@ParameterizedTest
	@InputOutputSource(
			inputs = @CsvSource(textBlock = POSITION_ARGUMENTS),
			outputs = @ValueSource(doubles =
					{0, 1.570796, 3.141592, 1.570796, 1.570796, 0, 0.615479, 1.285872, 2.186276}))
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
	void calculateInitialBearing(int startLat, int startLng, int endLat, int endLng, int degrees) {
		assertThat(Math.round(calculator.initialBearing(
						coordinate(startLat, startLng),
						coordinate(endLat, endLng))
				.decimal().getValue().doubleValue()))
				.isEqualTo(degrees);
	}

	@ParameterizedTest
	@InputOutputSource(
			inputs = @CsvSource(textBlock = POSITION_ARGUMENTS),
			outputs = @ValueSource(ints = {0, 270, 180, 270, 270, 0, 305, 254, 215}))
	void calculateFinalBearing(int startLat, int startLng, int endLat, int endLng, int degrees) {
		assertThat(Math.round(calculator.finalBearing(
						coordinate(startLat, startLng),
						coordinate(endLat, endLng))
				.decimal().getValue().doubleValue()))
				.isEqualTo(degrees);
	}

	@ParameterizedTest
	@CsvSource(textBlock = """
			0,  0,  0,      0.00290,    0.00290,   0.0
			0,  0,  1.5708, 0.00290,    0.0,       0.00290
			""")
	void travel(int startLat, int startLng, double bearing, double distance, double endLat, double endLng) {
		Point2D point = calculator.travel(startLat, startLng, bearing, distance);
		assertThat(point.getY()).describedAs("latitude").isCloseTo(endLat, within(0.0001));
		assertThat(point.getX()).describedAs("longitude").isCloseTo(endLng, within(0.0001));
	}

	@ParameterizedTest
	@CsvSource(textBlock = """
			0,  0,  0,  600, 10,    0
			0,  0,  90, 600,  0,   10
			""")
	void travelInNauticalMiles(int startLat, int startLng, int degrees, int distance, int endLat, int endLng) {
		Coordinate coordinate = calculator.travel(
				coordinate(startLat, startLng),
				new Bearing(degrees, 0, 0),
				Quantities.getQuantity(distance, NAUTICAL_MILE));
		assertThat(Math.round(coordinate.getLatitude().decimal().getValue().doubleValue()))
				.as("latitude").isEqualTo(endLat);
		assertThat(Math.round(coordinate.getLongitude().decimal().getValue().doubleValue()))
				.as("longitude").isEqualTo(endLng);
	}

	@ParameterizedTest
	@CsvSource(textBlock = """
			0,  0,  0,  1112, 10,    0
			0,  0,  90, 1112,  0,   10
			""")
	void travelInKilometers(int startLat, int startLng, int degrees, int distance, int endLat, int endLng) {
		Coordinate coordinate = calculator.travel(
				coordinate(startLat, startLng),
				new Bearing(degrees, 0, 0),
				Quantities.getQuantity(distance, KILO(METER)));
		assertThat(Math.round(coordinate.getLatitude().decimal().getValue().doubleValue()))
				.as("latitude").isEqualTo(endLat);
		assertThat(Math.round(coordinate.getLongitude().decimal().getValue().doubleValue()))
				.as("longitude").isEqualTo(endLng);
	}

	@ParameterizedTest
	@CsvSource(textBlock = """
			 0.0,    3.141
			 1.570,  4.712
			 3.141,  6.283
			 4.712,  1.570
			-0.0,    3.141
			-1.570,  1.572
			-3.141,  0.0
			-4.712, -1.570
			""")
	void reverse(double radians, double reversed) {
		assertThat(calculator.reverse(radians))
				.isCloseTo(reversed, within(0.001));
	}

	@ParameterizedTest
	@CsvSource(textBlock = """
			 0.0,    0.0
			 1.570,  1.570
			 3.141,  3.141
			 4.712, -1.571
			-0.0,   -0.0
			-1.570, -1.570
			-3.141, -3.141
			-4.712,  1.571
			""")
	void normaliseLongitude(double radians, double normalised) {
		assertThat(calculator.normaliseLongitude(radians))
				.isCloseTo(normalised, within(0.001));
	}

	@ParameterizedTest
	@CsvSource(textBlock = """
			 0.0,    0.0
			 1.570,  1.570
			 3.141, -0.001
			 4.712,  1.570
			-0.0,   -0.0
			-1.570, -1.570
			-3.141,  0.001
			-4.712, -1.570
			""")
	void normaliseLatitude(double radians, double normalised) {
		assertThat(calculator.normaliseLatitude(radians))
				.isCloseTo(normalised, within(0.001));
	}

	@ParameterizedTest
	@CsvSource(textBlock = """
			 0.0,    0.0
			 3.141,  3.141
			 6.242,  6.242
			 7.0,    0.716
			-1.570,  1.570
			-3.141,  3.142
			-4.712,  4.712
			-7.0,    0.716
			""")
	void normaliseBearing(double radians, double normalised) {
		assertThat(calculator.normaliseBearing(radians))
				.isCloseTo(normalised, within(0.001));
	}

	private Coordinate coordinate(int latitude, int longitude) {
		return Coordinate.builder()
				.latitude(new Latitude(latitude, 0, 0))
				.longitude(new Longitude(longitude, 0, 0))
				.build();
	}

}