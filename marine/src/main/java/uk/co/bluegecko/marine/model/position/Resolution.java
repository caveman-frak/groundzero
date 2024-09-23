package uk.co.bluegecko.marine.model.position;

import com.uber.h3core.H3Core;
import java.time.Duration;
import java.time.Instant;
import java.util.UUID;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.Accessors;
import lombok.experimental.FieldDefaults;

@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
@Getter
@Accessors(fluent = true)
public enum Resolution {

	FINE(6, Duration.ofMinutes(10)),
	MEDIUM(4, Duration.ofHours(1)),
	COARSE(2, Duration.ofHours(6));

	int h3;
	Duration duration;

	public long millis() {
		return duration.toMillis();
	}

	public Instant start(long epochIntervals) {
		return Instant.EPOCH.plus(duration.multipliedBy(epochIntervals));
	}

	public Instant end(long epochIntervals) {
		return start(epochIntervals).plus(duration());
	}

	public Partition partition(H3Core h3Core, Trace trace) {
		long h3Cell = h3Core.latLngToCell(trace.latitude(), trace.longitude(), h3());
		long epochIntervals = trace.getTimestamp().toEpochMilli() / millis();

		return new Partition(this, trace.getVesselId(), epochIntervals, h3Cell);
	}

	public record Partition(Resolution resolution, UUID id, long epochIntervals, long h3Cell) {

	}

}