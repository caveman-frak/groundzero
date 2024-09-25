package uk.co.bluegecko.common.generate;

import java.util.Arrays;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.stream.IntStream;
import java.util.stream.Stream;

@SuppressWarnings("unchecked")
@FunctionalInterface
public interface Generator<T> {

	Optional<T> generate();

	default Optional<T> generate(Consumer<T>... consumers) {
		return generate().map(r -> {
			Arrays.stream(consumers).forEach(c -> c.accept(r));
			return r;
		});
	}

	default Stream<T> generate(int count) {
		return generate(count, new Consumer[0]);
	}

	default Stream<T> generate(int count, Consumer<T>... consumers) {
		return IntStream.range(0, count).boxed().map(_ -> generate(consumers))
				.filter(Optional::isPresent).map(Optional::get);
	}

}