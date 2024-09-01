package uk.co.bluegecko.marine.model;

import com.uber.h3core.H3Core;
import java.time.Duration;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.UUID;

public interface Resolution {

	long MILLIS_IN_HOUR = Duration.ofHours(1).toMillis();

	int H3_RESOLUTION = 7;

	H3Core H3_CORE = h3Core();

	static H3Core h3Core() {
		try {
			return H3Core.newInstance();
		} catch (Exception ex) {
			ex.printStackTrace();
			throw new RuntimeException(ex);
		}
	}

	UUID getVesselId();

	long getH3Cell();

	long getEpochHours();

	default Bucket bucket() {
		return new Bucket(getVesselId(), getEpochHours(), getH3Cell());
	}

	default long toEpochHours(Instant timestamp) {
		return timestamp.toEpochMilli() / MILLIS_IN_HOUR;
	}

	default long h3Cell(double latitude, double longitude) {
		return H3_CORE.latLngToCell(latitude, longitude, H3_RESOLUTION);
	}

	static Instant start(long epochHours) {
		return Instant.EPOCH.plus(epochHours, ChronoUnit.HOURS);
	}

	static Instant end(long epochHours) {
		return start(epochHours).plus(Duration.ofHours(1));
	}

	record Bucket(UUID id, long epochHours, long h3Cell) {

	}

}