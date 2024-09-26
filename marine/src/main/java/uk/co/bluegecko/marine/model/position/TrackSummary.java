package uk.co.bluegecko.marine.model.position;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.BiConsumer;
import java.util.stream.Gatherer;
import java.util.stream.Gatherer.Downstream;
import org.jetbrains.annotations.NotNull;
import uk.co.bluegecko.marine.model.position.partition.ByLocation;
import uk.co.bluegecko.marine.model.position.partition.LocationPartition;
import uk.co.bluegecko.marine.model.position.partition.Partition;

public class TrackSummary {

	public List<Track> condenseRoute(List<Track> tracks) {
		return tracks.stream().filter(t -> t.getPartition() instanceof ByLocation)
				.map(t -> t.withPartition(LocationPartition.class))
				.filter(Optional::isPresent).map(Optional::get)
				.gather(routeGatherer())
				.toList();
	}

	public List<Track> condense(List<Track> tracks, Class<? extends Partition> partitionClass) {
		return tracks.stream().map(t -> t.withPartition(partitionClass))
				.filter(Optional::isPresent).map(Optional::get)
				.collect(() -> new HashMap<Partition, List<Trace>>(),
						(r, t) -> r.compute(t.getPartition(),
								(_, v) -> (v == null ? new ArrayList<>() : v)).addAll(t.getTraces()),
						Map::putAll)
				.entrySet().stream().map(Track::new)
				.toList();
	}

	@NotNull
	private Gatherer<Track, AtomicReference<Track>, Track> routeGatherer() {
		return Gatherer.ofSequential(AtomicReference::new,
				(state, track, downstream) -> {
					state.getAndAccumulate(track, (o, n) -> {
						if (o == null) {
							return n;
						}
						Optional<Track> merged = o.merge(n);
						if (merged.isEmpty()) {
							downstream.push(o);
						}
						return merged.orElse(n);
					});
					return true;
				}, atomicFinisher());
	}

	@NotNull
	private BiConsumer<AtomicReference<Track>, Downstream<? super Track>> atomicFinisher() {
		return (state, downstream) -> {
			Track track = state.get();
			if (track != null) {
				downstream.push(track);
			}
		};
	}


}