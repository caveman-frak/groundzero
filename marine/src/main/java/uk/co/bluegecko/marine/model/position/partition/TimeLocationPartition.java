package uk.co.bluegecko.marine.model.position.partition;

import com.uber.h3core.H3Core;
import java.util.Optional;
import org.apache.commons.lang3.builder.CompareToBuilder;
import org.jetbrains.annotations.NotNull;

public record TimeLocationPartition(Resolution resolution, long epochIntervals, long cell)
		implements Partition<TimeLocationPartition>, ByTime, ByLocation {

	@Override
	public int compareTo(@NotNull TimeLocationPartition o) {
		return new CompareToBuilder()
				.append(resolution, o.resolution)
				.append(epochIntervals, o.epochIntervals)
				.append(cell, o.cell)
				.build();
	}

	public static Partitioner partitioner(H3Core core) {
		return (r, t) -> new TimeLocationPartition(
				r, r.epochIntervals(t.getTimestamp()), core.latLngToCell(t.latitude(), t.longitude(), r.h3()));
	}

	static Optional<Partition<?>> from(Partition<?> p) {
		if ((p instanceof ByLocation pL) && (p instanceof ByTime pT)) {
			return Optional.of(new TimeLocationPartition(p.resolution(), pT.epochIntervals(), pL.cell()));
		}
		return Optional.empty();
	}

}