package uk.co.bluegecko.csv.beanio.handler;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import org.beanio.types.TypeConversionException;
import org.beanio.types.TypeHandler;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ListOfIntTypeHandlerTest {

	private TypeHandler handler;

	@BeforeEach
	void setUp() {
		handler = new ListOfIntTypeHandler();
	}

	@Test
	@SuppressWarnings("unchecked")
	void toListWithOne() throws TypeConversionException {
		assertThat((List<Integer>) handler.parse("1809")).containsExactly(1809);
	}

	@Test
	@SuppressWarnings("unchecked")
	void toListWithMany() throws TypeConversionException {
		assertThat((List<Integer>) handler.parse("1809,1829,1849"))
				.containsExactlyInAnyOrder(1809, 1829, 1849);
	}

	@Test
	@SuppressWarnings("unchecked")
	void toListWithEmpty() throws TypeConversionException {
		assertThat((List<Integer>) handler.parse("")).isEmpty();
	}

	@Test
	@SuppressWarnings("unchecked")
	void toListWithNull() throws TypeConversionException {
		assertThat((List<Integer>) handler.parse(null)).isEmpty();
	}

	@Test
	void fromListWithOne() {
		assertThat(handler.format(List.of(1809))).isEqualTo("1809");
	}

	@Test
	void fromListWithMany() {
		assertThat(handler.format(List.of(1809, 1829, 1849))).isEqualTo("1809,1829,1849");
	}

	@Test
	void fromListWithEmpty() {
		assertThat(handler.format(List.of())).isEqualTo("");
	}

	@Test
	void fromListWithNull() {
		assertThat(handler.format(null)).isNull();
	}

}