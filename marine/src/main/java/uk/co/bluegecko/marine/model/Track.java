package uk.co.bluegecko.marine.model;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Value;
import uk.co.bluegecko.marine.model.Resolution.Bucket;

@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
@Value
public class Track {

	UUID vesselId;
	long h3Cell;
	long epochHours;
	List<Trace> traces;
	@Getter(lazy = true)
	Instant earliest = traces.stream().map(Trace::getTimestamp).sorted().findFirst().orElse(null);

	private Track(Map.Entry<Bucket, List<Trace>> entry) {
		Bucket bucket = entry.getKey();
		this(bucket.id(), bucket.h3Cell(), bucket.epochHours(), entry.getValue());
	}

	public static List<Track> tracks(Collection<Trace> traces) {
		return traces.stream().collect(() -> new HashMap<Bucket, List<Trace>>(),
						(r, t) -> r.compute(t.bucket(), (_, v) -> (v == null ? new ArrayList<>() : v)).add(t),
						Map::putAll)
				.entrySet().stream().map(Track::new).toList();
	}

}