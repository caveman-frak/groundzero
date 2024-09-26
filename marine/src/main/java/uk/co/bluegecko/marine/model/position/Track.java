package uk.co.bluegecko.marine.model.position;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Stream;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.Value;
import uk.co.bluegecko.marine.model.position.partition.Partition;
import uk.co.bluegecko.marine.model.position.partition.Partitioner;
import uk.co.bluegecko.marine.model.position.partition.Resolution;

@RequiredArgsConstructor(access = AccessLevel.PACKAGE)
@Value
public class Track {

	Partition partition;
	List<Trace> traces;
	Instant earliest;

	Track(Map.Entry<Partition, List<Trace>> entry) {
		List<Trace> traces = entry.getValue();
		Instant earliest = traces.stream().map(Trace::getTimestamp).sorted().findFirst().orElse(null);
		this(entry.getKey(), traces, earliest);
	}

	public Optional<Track> merge(Track track) {
		if (partition.equals(track.partition)) {
			return Optional.of(new Track(partition,
					Stream.concat(traces.stream(), track.traces.stream()).distinct().toList(),
					earliest.isBefore(track.earliest) ? earliest : track.earliest));
		} else {
			return Optional.empty();
		}
	}

	public Optional<Track> withPartition(Class<? extends Partition> partitionClass) {
		return getPartition().to(partitionClass).map(p -> new Track(p, traces, earliest));
	}

	public static List<Track> tracks(Resolution resolution, Collection<Trace> traces, Partitioner partitioner) {
		return traces.stream().collect(() -> new HashMap<Partition, List<Trace>>(),
						(r, t) -> r.compute(partitioner.apply(resolution, t),
								(_, v) -> (v == null ? new ArrayList<>() : v)).add(t),
						Map::putAll)
				.entrySet().stream().map(Track::new).toList();
	}

}