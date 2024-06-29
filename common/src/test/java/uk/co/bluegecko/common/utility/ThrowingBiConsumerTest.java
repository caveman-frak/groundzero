package uk.co.bluegecko.common.utility;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static uk.co.bluegecko.common.utility.ThrowingBiConsumer.sneakyThrows;

import java.io.IOException;
import java.text.ParseException;
import org.junit.jupiter.api.Test;

class ThrowingBiConsumerTest {

	private ThrowingBiConsumer<String, Integer, IOException> c;

	@Test
	void withIoException() {
		c = (a, b) -> {
			throw new IOException(a);
		};
	}

	@Test
	void withNotIoException() {
		c = (a, b) -> {
			// compiler error if trying to use wrong exception type, as wanted
//			throw new ParseException(a, b);
		};
	}

	@Test
	void withRuntimeException() {
		c = (a, b) -> {
			throw new RuntimeException(new ParseException(a, b));
		};
	}

	@Test
	void doSneakyThrows() {
		c = (a, b) -> {
			throw new IOException(a + "-" + b);
		};
		assertThatThrownBy(() -> sneakyThrows(c).accept("foo", 99))
				.isInstanceOf(RuntimeException.class)
				.hasMessage("foo-99")
				.hasCauseInstanceOf(IOException.class)
				.hasRootCauseMessage("foo-99");
	}

	@Test
	void doSneakyThrowsWithoutException() {
		c = (a, b) -> {
			if (b % 2 == 0) {
				throw new IOException(a + "-" + b);
			}
		};
		sneakyThrows(c).accept("foo", 99);
	}

	@Test
	void doSneakyThrowsWithRuntime() {
		c = (a, b) -> {
			throw new RuntimeException(a + "-" + b);
		};
		assertThatThrownBy(() -> sneakyThrows(c).accept("foo", 99))
				.isInstanceOf(RuntimeException.class)
				.hasMessage("foo-99")
				.hasNoCause();
	}
}