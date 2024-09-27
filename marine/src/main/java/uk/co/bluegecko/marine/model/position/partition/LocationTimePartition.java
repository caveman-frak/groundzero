package uk.co.bluegecko.marine.model.position.partition;

import com.uber.h3core.H3Core;
import java.util.Optional;
import org.apache.commons.lang3.builder.CompareToBuilder;
import org.jetbrains.annotations.NotNull;

public record LocationTimePartition(Resolution resolution, long cell, long epochIntervals)
		implements Partition<LocationTimePartition>, ByLocation, ByTime {

	@Override
	public int compareTo(@NotNull LocationTimePartition o) {
		return new CompareToBuilder()
				.append(resolution, o.resolution)
				.append(cell, o.cell)
				.append(epochIntervals, o.epochIntervals)
				.build();
	}

	public static Partitioner partitioner(H3Core core) {
		return (r, t) -> new LocationTimePartition(
				r, core.latLngToCell(t.latitude(), t.longitude(), r.h3()), r.epochIntervals(t.getTimestamp()));
	}

	static Optional<Partition<?>> from(Partition<?> p) {
		if ((p instanceof ByLocation pL) && (p instanceof ByTime pT)) {
			return Optional.of(new LocationTimePartition(p.resolution(), pL.cell(), pT.epochIntervals()));
		}
		return Optional.empty();
	}

}