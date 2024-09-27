package uk.co.bluegecko.marine.model.position.partition;

import com.uber.h3core.H3Core;
import java.util.Optional;
import org.apache.commons.lang3.builder.CompareToBuilder;
import org.jetbrains.annotations.NotNull;

public record LocationPartition(Resolution resolution, long cell)
		implements Partition<LocationPartition>, ByLocation {

	@Override
	public int compareTo(@NotNull LocationPartition o) {
		return new CompareToBuilder()
				.append(resolution, o.resolution)
				.append(cell, o.cell)
				.build();
	}

	public static Partitioner partitioner(H3Core core) {
		return (r, t) -> new LocationPartition(r, core.latLngToCell(t.latitude(), t.longitude(), r.h3()));
	}

	static Optional<Partition<?>> from(Partition<?> p) {
		if (p instanceof ByLocation pL) {
			return Optional.of(new LocationPartition(p.resolution(), pL.cell()));
		}
		return Optional.empty();
	}

}