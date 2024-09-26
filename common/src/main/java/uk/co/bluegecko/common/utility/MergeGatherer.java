package uk.co.bluegecko.common.utility;

import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.BiConsumer;
import java.util.function.Supplier;
import java.util.stream.Gatherer;

public class MergeGatherer<T extends Mergeable<T>> implements Gatherer<T, AtomicReference<T>, T> {

	@Override
	public Supplier<AtomicReference<T>> initializer() {
		return AtomicReference::new;
	}

	@Override
	public Integrator<AtomicReference<T>, T, T> integrator() {
		return (state, track, downstream) -> {
			state.getAndAccumulate(track, (o, n) -> {
				if (o == null) {
					return n;
				}
				Optional<T> merged = o.merge(n);
				if (merged.isEmpty()) {
					downstream.push(o);
				}
				return merged.orElse(n);
			});
			return true;
		};
	}

	@Override
	public BiConsumer<AtomicReference<T>, Downstream<? super T>> finisher() {
		return (state, downstream) -> {
			T track = state.get();
			if (track != null) {
				downstream.push(track);
			}
		};
	}

}