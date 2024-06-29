package uk.co.bluegecko.utility.convert;

import static org.assertj.core.api.Assertions.assertThat;

import java.awt.Point;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.ConversionService;
import org.springframework.core.convert.converter.ConverterRegistry;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import org.springframework.web.reactive.config.EnableWebFlux;

@SpringJUnitConfig
@EnableWebFlux
class PointConvertorTest {

	@Autowired
	private ConversionService conversionService;
	@Autowired
	private ConverterRegistry converterRegistry;

	private PointConvertor convertor;

	@BeforeEach
	void setUp() {
		convertor = new PointConvertor();
	}

	@AfterEach
	void tearDown() {
		converterRegistry.removeConvertible(Point.class, String.class);
		converterRegistry.removeConvertible(String.class, Point.class);
	}

	@Test
	void convertToPoint() {
		assertThat(convertor.to("[101;102]")).isEqualTo(new Point(101, 102));
	}

	@Test
	void convertFromPoint() {
		assertThat(convertor.from(new Point(101, 102))).isEqualTo("[101;102]");
	}

	@Test
	void unregistered() {
		assertThat(conversionService.canConvert(Point.class, String.class)).describedAs("from").isFalse();
		assertThat(conversionService.canConvert(String.class, Point.class)).describedAs("to").isFalse();
	}

	@Test
	void registerConverters() {
		convertor.register(converterRegistry);

		assertThat(conversionService.canConvert(Point.class, String.class)).describedAs("from").isTrue();
		assertThat(conversionService.canConvert(String.class, Point.class)).describedAs("to").isTrue();
	}


}