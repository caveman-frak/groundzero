package uk.co.bluegecko.marine.model.position.partition;

import java.time.Duration;
import java.time.Instant;
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

	FINEST(10, Duration.ofMinutes(1)),
	FINER(8, Duration.ofMinutes(6)),
	FINE(6, Duration.ofMinutes(10)),
	MEDIUM(4, Duration.ofHours(1)),
	COARSE(2, Duration.ofHours(6)),
	COARSEST(0, Duration.ofDays(1));

	int h3;
	Duration duration;

	public long millis() {
		return duration.toMillis();
	}

	public long epochIntervals(Instant timestamp) {
		return timestamp.toEpochMilli() / millis();
	}

	public Instant start(long epochIntervals) {
		return Instant.EPOCH.plus(duration.multipliedBy(epochIntervals));
	}

	public Instant end(long epochIntervals) {
		return start(epochIntervals).plus(duration());
	}

}