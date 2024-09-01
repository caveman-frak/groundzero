package uk.co.bluegecko.marine.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import lombok.AccessLevel;
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

	private Track(Bucket bucket, List<Trace> traces) {
		this(bucket.id(), bucket.h3Cell(), bucket.epochHours(), traces);
	}

	public static List<Track> tracks(Collection<Trace> traces) {
		Map<Bucket, List<Trace>> buckets = new HashMap<>();
		traces.forEach(t -> buckets.compute(t.bucket(), (_, v) -> (v == null ? new ArrayList<>() : v)).add(t));

		return buckets.entrySet().stream().map(e -> new Track(e.getKey(), e.getValue())).toList();
	}

}