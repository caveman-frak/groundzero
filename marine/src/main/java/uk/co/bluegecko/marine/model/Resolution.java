package uk.co.bluegecko.marine.model;

import java.time.Duration;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.UUID;

public interface Resolution {

	long MILLIS_IN_HOUR = Duration.ofHours(1).toMillis();

	int H3_RESOLUTION = 7;

	static Instant start(long epochHours) {
		return Instant.EPOCH.plus(epochHours, ChronoUnit.HOURS);
	}

	static Duration duration() {
		return Duration.ofHours(1);
	}

	static Instant end(long epochHours) {
		return start(epochHours).plus(duration());
	}

	record Partition(UUID id, long epochHours, long h3Cell) {

	}

}