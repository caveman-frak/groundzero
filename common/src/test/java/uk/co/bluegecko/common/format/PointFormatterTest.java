package uk.co.bluegecko.common.format;

import static org.assertj.core.api.Assertions.assertThat;

import java.awt.Point;
import java.text.ParseException;
import java.util.Locale;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.ConversionService;
import org.springframework.format.Formatter;
import org.springframework.format.FormatterRegistry;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import org.springframework.web.reactive.config.EnableWebFlux;

@SpringJUnitConfig
@EnableWebFlux
class PointFormatterTest {

	@Autowired
	private ConversionService conversionService;
	@Autowired
	private FormatterRegistry formatterRegistry;

	@AfterEach
	void tearDown() {
		formatterRegistry.removeConvertible(Point.class, String.class);
		formatterRegistry.removeConvertible(String.class, Point.class);
	}

	@Test
	void parsePoint() throws ParseException {
		Formatter<Point> formatter = new PointFormatter();

		assertThat(formatter.parse("[101;102]", Locale.ENGLISH)).isEqualTo(new Point(101, 102));
	}

	@Test
	void printPoint() {
		Formatter<Point> formatter = new PointFormatter();

		assertThat(formatter.print(new Point(101, 102), Locale.ENGLISH)).isEqualTo("[101;102]");
	}

	@Test
	void unregistered() {
		assertThat(conversionService.canConvert(Point.class, String.class)).describedAs("print").isFalse();
		assertThat(conversionService.canConvert(String.class, Point.class)).describedAs("parse").isFalse();
	}

	@Test
	void registerConverters() {
		formatterRegistry.addFormatter(new PointFormatter());

		assertThat(conversionService.canConvert(Point.class, String.class)).describedAs("print").isTrue();
		assertThat(conversionService.canConvert(String.class, Point.class)).describedAs("parse").isTrue();
	}

	@Test
	void pointFormatter() {
		formatterRegistry.addFormatter(new PointFormatter());

		assertThat(conversionService.convert(new Point(101, 102), String.class)).describedAs("print")
				.isEqualTo("[101;102]");
		assertThat(conversionService.convert("[101;102]", Point.class)).describedAs("parse")
				.isEqualTo(new Point(101, 102));
	}

}