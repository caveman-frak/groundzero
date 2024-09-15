package uk.co.bluegecko.marine.model.compass;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.offset;
import static systems.uom.ucum.UCUM.DEGREE;
import static systems.uom.ucum.UCUM.RADIAN;
import static uk.co.bluegecko.marine.model.compass.Limit.BEARING;
import static uk.co.bluegecko.marine.model.compass.Limit.LATITUDE;
import static uk.co.bluegecko.marine.model.compass.Limit.LONGITUDE;

import javax.measure.Quantity;
import javax.measure.quantity.Angle;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import tech.units.indriya.quantity.Quantities;

class CompassTest {

	@ParameterizedTest
	@CsvSource(textBlock = """
			-450, 270
			-360,   0
			-270,  90
			-180, 180
			 -90, 270
			   0,   0
			  90,  90
			 180, 180
			 270, 270
			 360, 360
			 450,  90
			 540, 180
			 630, 270
			 720,   0
			""")
	void bearingLimitInDegrees(double initial, double expected) {
		Quantity<Angle> actual = Compass.limit(Quantities.getQuantity(initial, DEGREE), BEARING);

		assertThat(actual.getUnit()).isEqualTo(DEGREE);
		assertThat(actual.getValue().doubleValue())
				.isCloseTo(expected, offset(0.0000001));
	}

	@ParameterizedTest
	@CsvSource(textBlock = """
			-2, 0
			-1, 1
			 0, 0
			 1, 1
			 2, 2
			 3, 1
			 4, 0
			""")
	void bearingLimitInRadians(double initial, double expected) {
		Quantity<Angle> actual = Compass.limit(Quantities.getQuantity(initial * Math.PI, RADIAN),
				BEARING);

		assertThat(actual.getUnit()).isEqualTo(RADIAN);
		assertThat(actual.getValue().doubleValue())
				.isCloseTo(expected * Math.PI, offset(0.0000001));
	}

	@ParameterizedTest
	@CsvSource(textBlock = """
			-180,   0
			-135, -45
			 -90, -90
			 -45, -45
			   0,   0
			  45,  45
			  90,  90
			 135,  45
			 180,   0
			""")
	void latitudeLimitInDegrees(double initial, double expected) {
		Quantity<Angle> actual = Compass.limit(Quantities.getQuantity(initial, DEGREE), LATITUDE);

		assertThat(actual.getUnit()).isEqualTo(DEGREE);
		assertThat(actual.getValue().doubleValue())
				.isCloseTo(expected, offset(0.0000001));
	}

	@ParameterizedTest
	@CsvSource(textBlock = """
			-1.0,  0.0
			-0.5, -0.5
			 0.0,  0.0
			 0.5,  0.5
			 1.0,  0.0
			 1.5,  0.5
			""")
	void latitudeLimitInRadians(double initial, double expected) {
		Quantity<Angle> actual = Compass.limit(Quantities.getQuantity(initial * Math.PI, RADIAN),
				LATITUDE);

		assertThat(actual.getUnit()).isEqualTo(RADIAN);
		assertThat(actual.getValue().doubleValue())
				.isCloseTo(expected * Math.PI, offset(0.0000001));
	}

	@ParameterizedTest
	@CsvSource(textBlock = """
			-450,  -90
			-360,    0
			-270,  -90
			-180, -180
			 -90,  -90
			   0,    0
			  90,   90
			 180,  180
			 270,   90
			 360,    0
			 450,   90
			 540,  180
			 630,   90
			 720,    0
			""")
	void longitudeLimitInDegrees(double initial, double expected) {
		Quantity<Angle> actual = Compass.limit(Quantities.getQuantity(initial, DEGREE), LONGITUDE);

		assertThat(actual.getUnit()).isEqualTo(DEGREE);
		assertThat(actual.getValue().doubleValue())
				.isCloseTo(expected, offset(0.0000001));
	}

	@ParameterizedTest
	@CsvSource(textBlock = """
			-2,  0
			-1, -1
			 0,  0
			 1,  1
			 2,  0
			 3,  1
			 4,  0
			""")
	void longitudeLimitInRadians(double initial, double expected) {
		Quantity<Angle> actual = Compass.limit(Quantities.getQuantity(initial * Math.PI, RADIAN),
				LONGITUDE);

		assertThat(actual.getUnit()).isEqualTo(RADIAN);
		assertThat(actual.getValue().doubleValue())
				.isCloseTo(expected * Math.PI, offset(0.0000001));
	}

}