package uk.co.bluegecko.beanio;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Set;
import java.util.TreeSet;
import org.beanio.types.TypeConversionException;
import org.beanio.types.TypeHandler;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ToSetTypeHandlerTest {

	private TypeHandler handler;

	@BeforeEach
	void setUp() {
		handler = new ToSetTypeHandler();
	}

	@Test
	@SuppressWarnings("unchecked")
	void toSetWithOne() throws TypeConversionException {
		assertThat((Set<String>) handler.parse("en")).containsExactly("en");
	}

	@Test
	@SuppressWarnings("unchecked")
	void toSetWithMany() throws TypeConversionException {
		assertThat((Set<String>) handler.parse("de,en,fr,pt,sp"))
				.containsExactlyInAnyOrder("de", "en", "fr", "pt", "sp");
	}

	@Test
	@SuppressWarnings("unchecked")
	void toSetWithEmpty() throws TypeConversionException {
		assertThat((Set<String>) handler.parse("")).isEmpty();
	}

	@Test
	@SuppressWarnings("unchecked")
	void toSetWithNull() throws TypeConversionException {
		assertThat((Set<String>) handler.parse(null)).isEmpty();
	}

	@Test
	void fromSetWithOne() {
		assertThat(handler.format(Set.of("en"))).isEqualTo("en");
	}

	@Test
	void fromSetWithMany() {
		assertThat(handler.format(new TreeSet<>(Set.of("de", "en", "fr", "pt", "sp")))).isEqualTo("de,en,fr,pt,sp");
	}

	@Test
	void fromSetWithEmpty() {
		assertThat(handler.format(Set.of())).isEqualTo("");
	}

	@Test
	void fromSetWithNull() {
		assertThat(handler.format(null)).isNull();
	}

}