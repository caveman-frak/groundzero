package uk.co.bluegecko.marine.model;

import com.uber.h3core.H3Core;
import java.time.Duration;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
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

	FINE(9, Duration.ofMinutes(15)),
	MEDIUM(7, Duration.ofHours(1)),
	COARSE(5, Duration.ofHours(6));

	int h3;
	Duration duration;

	public long millis() {
		return duration.toMillis();
	}

	public Instant start(long epochHours) {
		return Instant.EPOCH.plus(epochHours, ChronoUnit.HOURS);
	}

	public Instant end(long epochHours) {
		return start(epochHours).plus(duration());
	}

	public Partition partition(H3Core h3Core, Trace trace) {
		long h3Cell = h3Core.latLngToCell(trace.getLatitude(), trace.getLongitude(), h3());
		long epochHours = trace.getTimestamp().toEpochMilli() / millis();

		return new Partition(this, trace.getVesselId(), epochHours, h3Cell);
	}

	public record Partition(Resolution resolution, UUID id, long epochHours, long h3Cell) {

	}

}