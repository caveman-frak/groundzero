package uk.co.bluegecko.marine.model.position;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.Value;
import uk.co.bluegecko.marine.model.position.partition.Partition;
import uk.co.bluegecko.marine.model.position.partition.Partitioner;
import uk.co.bluegecko.marine.model.position.partition.Resolution;

@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
@Value
public class Track {

	Partition partition;
	List<Trace> traces;
	Instant earliest;

	private Track(Map.Entry<Partition, List<Trace>> entry) {
		List<Trace> traces = entry.getValue();
		Instant earliest = traces.stream().map(Trace::getTimestamp).sorted().findFirst().orElse(null);
		this(entry.getKey(), traces, earliest);
	}

	public static List<Track> tracks(Resolution resolution, Collection<Trace> traces, Partitioner partitioner) {
		return traces.stream().collect(() -> new HashMap<Partition, List<Trace>>(),
						(r, t) -> r.compute(partitioner.apply(resolution, t),
								(_, v) -> (v == null ? new ArrayList<>() : v)).add(t),
						Map::putAll)
				.entrySet().stream().map(Track::new).toList();
	}

}