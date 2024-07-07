package uk.co.bluegecko.common.utility.function;

import java.util.function.BiConsumer;

@FunctionalInterface
public interface ThrowingBiConsumer<T, U, E extends Exception> {

	void accept(T t, U u) throws E;

	static <T, U, E extends Exception> BiConsumer<T, U> sneakyThrows(ThrowingBiConsumer<T, U, E> consumer) {
		return (t, u) -> {
			try {
				consumer.accept(t, u);
			} catch (RuntimeException e) {
				throw e;
			} catch (Exception e) {
				throw new RuntimeException(e.getMessage(), e);
			}
		};
	}

}