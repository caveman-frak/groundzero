package uk.co.bluegecko.marine.model.position.partition;

import java.util.Optional;
import java.util.UUID;
import org.apache.commons.lang3.builder.CompareToBuilder;
import org.jetbrains.annotations.NotNull;

public record VesselPartition(Resolution resolution, UUID vessel)
		implements Partition<VesselPartition>, ByVessel {

	@Override
	public int compareTo(@NotNull VesselPartition o) {
		return new CompareToBuilder()
				.append(resolution, o.resolution)
				.append(vessel, o.vessel)
				.build();
	}

	public static Partitioner partitioner() {
		return (r, t) -> new VesselPartition(r, t.getVesselId());
	}

	static Optional<Partition<?>> from(Partition<?> p) {
		if (p instanceof ByVessel pV) {
			return Optional.of(new VesselPartition(p.resolution(), pV.vessel()));
		}
		return Optional.empty();
	}

}