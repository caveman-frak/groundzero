package uk.co.bluegecko.marine.model.travel;

import com.uber.h3core.H3Core;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.Value;
import uk.co.bluegecko.marine.model.travel.Resolution.Partition;

@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
@Value
public class Track {

	Resolution resolution;
	UUID vesselId;
	long h3Cell;
	long epochIntervals;
	List<Trace> traces;
	Instant earliest;

	private Track(Map.Entry<Partition, List<Trace>> entry) {
		Partition partition = entry.getKey();
		List<Trace> traces = entry.getValue();
		Instant earliest = traces.stream().map(Trace::getTimestamp).sorted().findFirst().orElse(null);
		this(partition.resolution(), partition.id(), partition.h3Cell(), partition.epochIntervals(), traces, earliest);
	}

	public static List<Track> tracks(H3Core h3Core, Resolution resolution, Collection<Trace> traces) {
		return traces.stream().collect(() -> new HashMap<Partition, List<Trace>>(),
						(r, t) -> r.compute(resolution.partition(h3Core, t),
								(_, v) -> (v == null ? new ArrayList<>() : v)).add(t),
						Map::putAll)
				.entrySet().stream().map(Track::new).toList();
	}

}