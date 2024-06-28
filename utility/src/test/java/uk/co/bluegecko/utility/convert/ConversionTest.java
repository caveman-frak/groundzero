package uk.co.bluegecko.utility.convert;

import static org.assertj.core.api.Assertions.assertThat;

import java.awt.Point;
import java.math.BigDecimal;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import org.assertj.core.data.Percentage;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.ConversionService;
import org.springframework.core.convert.TypeDescriptor;
import org.springframework.core.convert.converter.ConverterRegistry;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import org.springframework.web.reactive.config.EnableWebFlux;

@SpringJUnitConfig
@EnableWebFlux
public class ConversionTest {

	@Autowired
	private ConversionService conversionService;
	@Autowired
	private ConverterRegistry converterRegistry;

	@Test
	void hasConversionService() {
		assertThat(conversionService).isNotNull();
		assertThat(converterRegistry).isNotNull();
	}

	@Test
	void examineType() {
		assertThat(TypeDescriptor.valueOf(UUID.class).getType()).isEqualTo(UUID.class);
		assertThat(TypeDescriptor.valueOf(UUID.class).getElementTypeDescriptor()).isNull();
	}

	@Test
	void examineCollectionType() {
		TypeDescriptor typeDescriptor = TypeDescriptor.collection(Set.class, TypeDescriptor.valueOf(UUID.class));
		assertThat(typeDescriptor.getType()).isEqualTo(Set.class);
		assertThat(typeDescriptor.getElementTypeDescriptor()).isNotNull();
		assertThat(typeDescriptor.getElementTypeDescriptor().getType()).isEqualTo(UUID.class);
	}

	@Test
	void convertToBigDecimal() {
		assertThat(conversionService.canConvert(String.class, BigDecimal.class)).isTrue();
		assertThat(conversionService.convert("12.95", BigDecimal.class)).isCloseTo(new BigDecimal("12.95"),
				Percentage.withPercentage(1));
	}

	@Test
	@SuppressWarnings("unchecked")
	void convertToList() {
		assertThat(conversionService.canConvert(String.class, List.class)).isTrue();
		assertThat(conversionService.convert("1,2,3,4", List.class)).contains("1", "2", "3", "4");
	}

	@Test
	void convertToTypedList() {
		assertThat(conversionService.canConvert(TypeDescriptor.valueOf(String.class),
				TypeDescriptor.collection(List.class, TypeDescriptor.valueOf(String.class)))).isTrue();
		assertThat(conversionService.convert("1,2,3,4", TypeDescriptor.valueOf(String.class),
				TypeDescriptor.collection(List.class, TypeDescriptor.valueOf(String.class))))
				.isEqualTo(List.of("1", "2", "3", "4"));
	}

	@Test
	void convertToFloatList() {
		assertThat(conversionService.canConvert(TypeDescriptor.valueOf(String.class),
				TypeDescriptor.collection(List.class, TypeDescriptor.valueOf(Float.class)))).isTrue();
		assertThat(conversionService.convert("1,2,3,4", TypeDescriptor.valueOf(String.class),
				TypeDescriptor.collection(List.class, TypeDescriptor.valueOf(Float.class))))
				.isEqualTo(List.of(1f, 2f, 3f, 4f));
	}

	@Test
	void convertFromList() {
		assertThat(conversionService.canConvert(List.class, String.class)).isTrue();
		assertThat(conversionService.convert(List.of(1f, 2f, 3f, 4f), String.class)).isEqualTo("1.0,2.0,3.0,4.0");
	}

	@Test
	void convertFromTypedList() {
		assertThat(conversionService.canConvert(
				TypeDescriptor.collection(List.class, TypeDescriptor.valueOf(Float.class)),
				TypeDescriptor.valueOf(String.class))).isTrue();
		assertThat(conversionService.convert(List.of(1f, 2f, 3f, 4f),
				TypeDescriptor.collection(List.class, TypeDescriptor.valueOf(Float.class)),
				TypeDescriptor.valueOf(String.class)))
				.isEqualTo("1.0,2.0,3.0,4.0");
	}

	@Test
	void convertToPoint() {
		converterRegistry.addConverter(new StringToPointConverter());
		assertThat(conversionService.canConvert(String.class, Point.class)).isTrue();
		assertThat(conversionService.convert("[10;20]", Point.class))
				.isEqualTo(new Point(10, 20));
	}

	@Test
	void convertFromPoint() {
		converterRegistry.addConverter(new PointToStringConverter());
		assertThat(conversionService.canConvert(Point.class, String.class)).isTrue();
		assertThat(conversionService.convert(new Point(10, 20), String.class))
				.isEqualTo("[10;20]");
	}

	@Test
	void convertFromListToPoint() {
		converterRegistry.addConverter(new StringToPointConverter());
		assertThat(conversionService.canConvert(TypeDescriptor.valueOf(String.class),
				TypeDescriptor.collection(List.class, TypeDescriptor.valueOf(Point.class)))).isTrue();
		assertThat(conversionService.convert("[10;20],[40;50]", TypeDescriptor.valueOf(String.class),
				TypeDescriptor.collection(List.class, TypeDescriptor.valueOf(Point.class))))
				.isEqualTo(List.of(new Point(10, 20), new Point(40, 50)));
	}

}