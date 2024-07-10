package uk.co.bluegecko.utility.geo.format;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatException;
import static systems.uom.ucum.UCUM.DEGREE;

import java.text.ParseException;
import java.util.Locale;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.format.Formatter;
import tech.units.indriya.quantity.Quantities;
import uk.co.bluegecko.utility.geo.Angle;
import uk.co.bluegecko.utility.geo.Bearing;
import uk.co.bluegecko.utility.geo.Compass;
import uk.co.bluegecko.utility.geo.DegreeDecimal;
import uk.co.bluegecko.utility.geo.Latitude;
import uk.co.bluegecko.utility.geo.Longitude;

class AngleFormatterTest {

	Formatter<Angle> formatter;

	@BeforeEach
	void setUp() {
		formatter = new AngleFormatter();
	}

	@Test
	void printBearing() {
		assertThat(formatter.print(new Bearing(10, 20, 30.05), Locale.UK))
				.isEqualTo("10°20'30.05\"");
	}

	@Test
	void printLatitude() {
		assertThat(formatter.print(new Latitude(10, 20, 30.05, Compass.N), Locale.UK))
				.isEqualTo("10°20'30.05\"N");
	}

	@Test
	void printLongitude() {
		assertThat(formatter.print(new Longitude(10, 20, 30.05, Compass.W), Locale.UK))
				.isEqualTo("10°20'30.05\"W");
	}

	@Test
	void printDecimal() {
		assertThat(formatter.print(DegreeDecimal.builder()
				.decimal(Quantities.getQuantity(10.341666667, DEGREE)).build(), Locale.UK))
				.isEqualTo("10.341667°");
	}

	@Test
	void parseBearing() throws ParseException {
		assertThat(formatter.parse("10°20'30.05\"", Locale.UK))
				.isEqualTo(new Bearing(10, 20, 30.05));
	}

	@Test
	void parseLatitude() throws ParseException {
		assertThat(formatter.parse("10°20'30.05\"N", Locale.UK))
				.isEqualTo(new Latitude(10, 20, 30.05, Compass.N));
	}

	@Test
	void parseLongitude() throws ParseException {
		assertThat(formatter.parse("10°20'30.05\"W", Locale.UK))
				.isEqualTo(new Longitude(10, 20, 30.05, Compass.W));
	}

	@Test
	void parseBearingJustDegree() throws ParseException {
		assertThat(formatter.parse("10°", Locale.UK))
				.isEqualTo(new Bearing(10, 0, 0));
	}

	@Test
	void parseBearingJustMinutes() throws ParseException {
		assertThat(formatter.parse("10'", Locale.UK))
				.isEqualTo(new Bearing(0, 10, 0));
	}

	@Test
	void parseBearingJustSeconds() throws ParseException {
		assertThat(formatter.parse("1.05\"", Locale.UK))
				.isEqualTo(new Bearing(0, 0, 1.05));
	}

	@Test
	void parseBearingDecimal() throws ParseException {
		assertThat(formatter.parse("10.5°", Locale.UK))
				.isEqualTo(new Bearing(10, 30, 0));
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