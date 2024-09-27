package uk.co.bluegecko.marine.model.position.partition;

import java.time.Instant;

public interface ByTime extends ByResolution {

	long epochIntervals();

	default Instant start() {
		return resolution().start(epochIntervals());
	}

	default Instant end() {
		return resolution().end(epochIntervals());
	}

}