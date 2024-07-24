package uk.co.bluegecko.ui.geometry.calc;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.offset;

import java.util.function.DoubleUnaryOperator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

class GeometryCalculatorTest {

	GeometryCalculator calculator;

	@BeforeEach
	void setUp() {
		calculator = new GeometryCalculator();
	}

	@ParameterizedTest
	@CsvSource(textBlock = """
			MIN,    6.283185
			POOR,   6.283185
			LOW,    6.283185
			MEDIUM, 6.283185
			HIGH,   6.283185
			MAX,    6.283185
			""")
	void gaussianQuadratureForCircle(Accuracy accuracy, double result) {
		DoubleUnaryOperator f = d -> Math.sqrt(Math.pow(Math.sin(d), 2) + Math.pow(Math.cos(d), 2));

		assertThat(calculator.gaussianQuadrature(f, 0, 2 * Math.PI, accuracy))
				.isCloseTo(result, offset(0.000001));
	}

	@ParameterizedTest
	@CsvSource(textBlock = """
			MIN,    12.290508
			POOR,   8.0506955
			LOW,    10.156072
			MEDIUM, 9.4354411
			HIGH,   9.8076612
			MAX,    9.6225526
			""")
	void gaussianQuadratureForEllipse(Accuracy accuracy, double result) {
		DoubleUnaryOperator f = d -> Math.sqrt(Math.pow(2, 2) * Math.pow(Math.sin(d), 2) + Math.pow(Math.cos(d), 2));

		assertThat(calculator.gaussianQuadrature(f, 0, 2 * Math.PI, accuracy))
				.isCloseTo(result, offset(0.000001));
	}
}