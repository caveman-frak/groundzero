package uk.co.bluegecko.marine.model.position;

import java.time.Instant;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.stream.Collectors;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.Value;
import org.apache.commons.lang3.builder.CompareToBuilder;
import org.jetbrains.annotations.NotNull;
import uk.co.bluegecko.common.utility.Mergeable;
import uk.co.bluegecko.marine.model.position.partition.Partition;
import uk.co.bluegecko.marine.model.position.partition.Partitioner;
import uk.co.bluegecko.marine.model.position.partition.Resolution;

@RequiredArgsConstructor(access = AccessLevel.PACKAGE)
@Value
public class Track implements Mergeable<Track>, Comparable<Track> {

	Partition<?> partition;
	SortedSet<Trace> traces;
	Instant earliest;

	Track(Map.Entry<Partition<?>, SortedSet<Trace>> entry) {
		SortedSet<Trace> traces = entry.getValue();
		Instant earliest = traces.isEmpty() ? null : traces.first().getTimestamp();
		this(entry.getKey(), traces, earliest);
	}

	@Override
	public Optional<Track> merge(Track track) {
		if (partition.equals(track.partition)) {
			SortedSet<Trace> traces = new TreeSet<>(this.traces);
			traces.addAll(track.traces);
			return Optional.of(new Track(partition,
					traces,
					earliest.isBefore(track.earliest) ? earliest : track.earliest));
		} else {
			return Optional.empty();
		}
	}

	public Optional<Track> withPartition(Class<? extends Partition<?>> partitionClass) {
		return getPartition().to(partitionClass).map(p -> new Track(p, traces, earliest));
	}

	@Override
	public int compareTo(@NotNull Track o) {
		return new CompareToBuilder()
				.append(partition, o.partition)
				.append(earliest, o.earliest)
				.append(traces.size(), o.traces.size())
				.build();
	}

	public static SortedSet<Track> fromTraces(Resolution resolution, Collection<Trace> traces,
			Partitioner partitioner) {
		return traces.stream().collect(() -> new HashMap<Partition<?>, SortedSet<Trace>>(),
						(r, t) -> r.compute(partitioner.apply(resolution, t),
								(_, v) -> (v == null ? new TreeSet<>() : v)).add(t),
						Map::putAll)
				.entrySet().stream().map(Track::new).sorted().collect(Collectors.toCollection(TreeSet::new));
	}

	public static SortedSet<Track> fromTracks(Resolution resolution, Collection<Track> tracks,
			Partitioner partitioner) {
		return fromTraces(resolution, tracks.stream().flatMap(t -> t.getTraces().stream()).toList(), partitioner);
	}
}