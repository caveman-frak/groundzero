package uk.co.bluegecko.marine.model.position.partition;

import com.uber.h3core.H3Core;
import java.util.Optional;
import java.util.UUID;
import org.apache.commons.lang3.builder.CompareToBuilder;
import org.jetbrains.annotations.NotNull;

public record VesselTimeLocationPartition(Resolution resolution, UUID vessel, long epochIntervals, long cell)
		implements Partition<VesselTimeLocationPartition>, ByVessel, ByTime, ByLocation {

	@Override
	public int compareTo(@NotNull VesselTimeLocationPartition o) {
		return new CompareToBuilder()
				.append(resolution, o.resolution)
				.append(vessel, o.vessel)
				.append(epochIntervals, o.epochIntervals)
				.append(cell, o.cell)
				.build();
	}

	public static Partitioner partitioner(H3Core core) {
		return (r, t) -> new VesselTimeLocationPartition(r, t.getVesselId(),
				r.epochIntervals(t.getTimestamp()), core.latLngToCell(t.latitude(), t.longitude(), r.h3()));
	}

	static Optional<Partition<?>> from(Partition<?> p) {
		if ((p instanceof ByLocation pL) && (p instanceof ByTime pT) && (p instanceof ByVessel pV)) {
			return Optional.of(
					new VesselTimeLocationPartition(p.resolution(), pV.vessel(), pT.epochIntervals(), pL.cell()
					));
		}
		return Optional.empty();
	}

}