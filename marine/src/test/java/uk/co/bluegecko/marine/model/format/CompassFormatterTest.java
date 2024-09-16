package uk.co.bluegecko.marine.model.format;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatException;
import static org.assertj.core.api.Assertions.offset;

import java.text.ParseException;
import java.util.Locale;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.format.Formatter;
import uk.co.bluegecko.marine.model.compass.Bearing;
import uk.co.bluegecko.marine.model.compass.Compass;
import uk.co.bluegecko.marine.model.compass.Latitude;
import uk.co.bluegecko.marine.model.compass.Longitude;

class CompassFormatterTest {

	Formatter<Compass> formatter;

	@BeforeEach
	void setUp() {
		formatter = new CompassFormatter();
	}

	@Test
	void printBearing() {
		assertThat(formatter.print(Bearing.asDegrees(10.34168056), Locale.UK))
				.isEqualTo("10°20'30.05\"");
	}

	@Test
	void printLatitude() {
		assertThat(formatter.print(Latitude.asDegrees(10.34168056), Locale.UK))
				.isEqualTo("10°20'30.05\"N");
	}

	@Test
	void printLongitude() {
		assertThat(formatter.print(Longitude.asDegrees(-10.34168056), Locale.UK))
				.isEqualTo("10°20'30.05\"W");
	}

	@Test
	void parseBearing() throws ParseException {
		assertThat(formatter.parse("10°20'30.05\"", Locale.UK).getValue().doubleValue())
				.isCloseTo(10.3416805, offset(0.0000001));
	}

	@Test
	void parseLatitude() throws ParseException {
		assertThat(formatter.parse("10°20'30.05\"N", Locale.UK).getValue().doubleValue())
				.isCloseTo(10.3416805, offset(0.0000001));
	}

	@Test
	void parseLongitude() throws ParseException {
		assertThat(formatter.parse("10°20'30.05\"W", Locale.UK).getValue().doubleValue())
				.isCloseTo(-10.3416805, offset(0.0000001));
	}

	@Test
	void parseBearingJustDegree() throws ParseException {
		assertThat(formatter.parse("10°", Locale.UK).getValue().doubleValue())
				.isCloseTo(10.0, offset(0.0000001));
	}

	@Test
	void parseBearingJustMinutes() throws ParseException {
		assertThat(formatter.parse("10'", Locale.UK).getValue().doubleValue())
				.isCloseTo(0.1666667, offset(0.0000001));
	}

	@Test
	void parseBearingJustSeconds() throws ParseException {
		assertThat(formatter.parse("1.05\"", Locale.UK).getValue().doubleValue())
				.isCloseTo(0.00029167, offset(0.0000001));
	}

	@Test
	void parseBearingDecimal() throws ParseException {
		assertThat(formatter.parse("10.5°", Locale.UK).getValue().doubleValue())
				.isCloseTo(10.5, offset(0.0000001));
	}


	@Test
	void invalidDegree() {
		assertThatException().isThrownBy(() -> formatter.parse("1A°20'30.05\"N", Locale.UK))
				.isInstanceOf(ParseException.class)
				.withMessage("Cannot parse '1A°20'30.05\"N', error at 0:1.");
	}

	@Test
	void invalidMinute() {
		assertThatException().isThrownBy(() -> formatter.parse("10°2A'30.05\"N", Locale.UK))
				.isInstanceOf(ParseException.class)
				.withMessage("Cannot parse '10°2A'30.05\"N', error at 3:4.");
	}

	@Test
	void invalidSecond() {
		assertThatException().isThrownBy(() -> formatter.parse("10°20'3A.05\"N", Locale.UK))
				.isInstanceOf(ParseException.class)
				.withMessage("Cannot parse '10°20'3A.05\"N', error at 6:7.");
	}

	@Test
	void invalidHemisphere() {
		assertThatException().isThrownBy(() -> formatter.parse("10°20'30.05\"B", Locale.UK))
				.isInstanceOf(ParseException.class)
				.withMessage("Invalid compass direction 'B' at 12");
	}

	@Test
	void nonCardinalHemisphere() {
		assertThatException().isThrownBy(() -> formatter.parse("10°20'30.05\"NE", Locale.UK))
				.isInstanceOf(ParseException.class)
				.withMessage("Invalid compass direction 'NE' at 12");
	}


}