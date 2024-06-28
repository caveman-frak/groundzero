package uk.co.bluegecko.utility.convert;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.ConversionService;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import org.springframework.web.reactive.config.EnableWebFlux;

@SpringJUnitConfig
@EnableWebFlux
class SimpleHandlerTest {

	@Autowired
	private ConversionService conversionService;

	@Test
	void convertInteger() {
		SimpleHandler<Integer> handler = new SimpleHandler<>(conversionService, Integer.class);

		assertThat(handler.supports(Long.class)).isTrue();
		assertThat(handler.parse("101")).isEqualTo(101);
		assertThat(handler.format(101)).isEqualTo("101");
	}

	@Test
	@SuppressWarnings("unchecked")
	void convertListOfInteger() {
		SimpleHandler<List<Integer>> handler = new SimpleHandler<>(conversionService, List.class, Integer.class);

		assertThat(handler.supports(Long.class)).isTrue();
		assertThat(handler.parse("101,102")).contains(101, 102);
		assertThat(handler.format(List.of(101, 102))).isEqualTo("101,102");
	}

}