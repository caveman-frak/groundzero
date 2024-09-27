package uk.co.bluegecko.marine.model.position.partition;

import com.uber.h3core.H3Core;
import java.util.Optional;
import java.util.UUID;
import org.apache.commons.lang3.builder.CompareToBuilder;
import org.jetbrains.annotations.NotNull;

public record VesselLocationTimePartition(Resolution resolution, UUID vessel, long cell, long epochIntervals)
		implements Partition<VesselLocationTimePartition>, ByVessel, ByLocation, ByTime {

	@Override
	public int compareTo(@NotNull VesselLocationTimePartition o) {
		return new CompareToBuilder()
				.append(resolution, o.resolution)
				.append(vessel, o.vessel)
				.append(cell, o.cell)
				.append(epochIntervals, o.epochIntervals)
				.build();
	}

	public static Partitioner partitioner(H3Core core) {
		return (r, t) -> new VesselLocationTimePartition(r, t.getVesselId(),
				core.latLngToCell(t.latitude(), t.longitude(), r.h3()), r.epochIntervals(t.getTimestamp()));
	}

	static Optional<Partition<?>> from(Partition<?> p) {
		if ((p instanceof ByLocation pL) && (p instanceof ByTime pT) && (p instanceof ByVessel pV)) {
			return Optional.of(new VesselLocationTimePartition(p.resolution(), pV.vessel(), pL.cell(),
					pT.epochIntervals()));
		}
		return Optional.empty();
	}

}