package uk.co.bluegecko.marine.model.position.partition;

import com.uber.h3core.H3Core;
import java.util.Optional;
import java.util.UUID;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class LocationTimeVesselPartition extends LocationTimePartition implements ByVessel {

	private final UUID vessel;

	public LocationTimeVesselPartition(Resolution resolution, long cell, long epochIntervals, UUID vessel) {
		super(resolution, cell, epochIntervals);

		this.vessel = vessel;
	}

	@Override
	public UUID vessel() {
		return vessel;
	}

	public static Partitioner partitioner(H3Core core) {
		return (r, t) -> new LocationTimeVesselPartition(r, core.latLngToCell(t.latitude(), t.longitude(), r.h3()),
				r.epochIntervals(t.getTimestamp()), t.getVesselId());
	}

	static Optional<Partition> from(Partition partition) {
		if ((partition instanceof ByLocation pL) && (partition instanceof ByTime pT)
				&& (partition instanceof ByVessel pV)) {
			return Optional.of(new LocationTimeVesselPartition(partition.resolution(), pL.cell(), pT.epochIntervals(),
					pV.vessel()));
		}
		return Optional.empty();
	}

}