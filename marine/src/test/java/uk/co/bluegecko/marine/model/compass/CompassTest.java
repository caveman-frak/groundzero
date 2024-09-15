package uk.co.bluegecko.marine.model.compass;

import static javax.measure.Quantity.Scale.ABSOLUTE;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.offset;
import static systems.uom.ucum.UCUM.DEGREE;
import static systems.uom.ucum.UCUM.RADIAN;
import static uk.co.bluegecko.marine.model.compass.Limit.BEARING;
import static uk.co.bluegecko.marine.model.compass.Limit.LATITUDE;
import static uk.co.bluegecko.marine.model.compass.Limit.LONGITUDE;
import static uk.co.bluegecko.marine.model.compass.Limit.UNLIMITED;

import javax.measure.Quantity;
import javax.measure.quantity.Angle;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import si.uom.quantity.AngularSpeed;
import tech.units.indriya.ComparableQuantity;
import tech.units.indriya.quantity.Quantities;
import tech.uom.lib.assertj.assertions.QuantityAssert;

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

	@Test
	void add() {
		QuantityAssert.assertThat(new Foo(20).add(new Foo(5)))
				.isEqualTo(new Foo(25))
				.isInstanceOf(Foo.class);
	}

	@Test
	void subtract() {
		QuantityAssert.assertThat(new Foo(20).subtract(new Foo(5))).isEqualTo(new Foo(15));
	}

	@Test
	void divide() {
		QuantityAssert.assertThat(new Foo(20).divide(2)).isEqualTo(new Foo(10));
	}

	@Test
	void multiply() {
		QuantityAssert.assertThat(new Foo(20).multiply(2)).isEqualTo(new Foo(40));
	}

	@Test
	void toRadians() {
		Quantity<Angle> actual = new Foo(90).to(RADIAN);
		assertThat(actual.getUnit()).isEqualTo(RADIAN);
		assertThat(actual.getValue().doubleValue())
				.isCloseTo(Math.PI / 2, offset(0.000001));
	}

	@Test
	void inverse() {
		assertThat(new Foo(20).inverse().getValue().doubleValue())
				.isEqualTo(new Foo(0.05).getValue().doubleValue());
	}

	@Test
	void negate() {
		QuantityAssert.assertThat(new Foo(20).negate()).isEqualTo(new Foo(-20));
	}

	@Test
	void asType() {
		QuantityAssert.assertThat(new Foo(20).asType(AngularSpeed.class))
				.isEqualTo(new Foo(20));
	}

	@Test
	void toSystemUnit() {
		Quantity<Angle> actual = new Foo(90).toSystemUnit();
		assertThat(actual.getUnit()).isEqualTo(RADIAN);
		assertThat(actual.getValue().doubleValue())
				.isCloseTo(Math.PI / 2, offset(0.000001));
	}

	@Test
	void isGreaterThan() {
		assertThat(new Foo(20).isGreaterThan(new Foo(25))).isFalse();
	}

	@Test
	void isGreaterThanOrEqualTo() {
		assertThat(new Foo(20).isGreaterThanOrEqualTo(new Foo(25))).isFalse();
	}

	@Test
	void isLessThan() {
		assertThat(new Foo(20).isLessThan(new Foo(25))).isTrue();
	}

	@Test
	void isLessThanOrEqualTo() {
		assertThat(new Foo(20).isLessThanOrEqualTo(new Foo(25))).isTrue();
	}

	@Test
	void compareTo() {
		assertThat(new Foo(20).compareTo(new Foo(25))).isEqualTo(-1);
	}

	@Test
	void isEquivalentTo() {
		assertThat(new Foo(0).isEquivalentTo(
				Quantities.getQuantity(0, RADIAN))).isTrue();
	}

	@Test
	void getValue() {
		assertThat(new Foo(20).getValue()).isEqualTo(20);
	}

	@Test
	void getUnit() {
		assertThat(new Foo(20).getUnit()).isEqualTo(DEGREE);
	}

	@Test
	void getScale() {
		assertThat(new Foo(20).getScale()).isEqualTo(ABSOLUTE);
	}

	private static class Foo extends Compass {

		protected Foo(ComparableQuantity<Angle> angle) {
			super(angle, UNLIMITED);
		}

		Foo(Number angle) {
			this(Quantities.getQuantity(angle, DEGREE));
		}

		@Override
		protected ComparableQuantity<Angle> wrap(ComparableQuantity<Angle> angle) {
			return new Foo(angle);
		}
	}

}