package uk.co.bluegecko.marine.model.position.partition;

import java.util.UUID;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class VesselTimePartition extends AbstractPartition implements ByVessel, ByTime {

	private final UUID vessel;
	private final long epochIntervals;


	protected VesselTimePartition(Resolution resolution, UUID vessel, long epochIntervals) {
		super(resolution);

		this.vessel = vessel;
		this.epochIntervals = epochIntervals;
	}

	@Override
	public UUID vessel() {
		return vessel;
	}

	@Override
	public long epochIntervals() {
		return epochIntervals;
	}

	public static Partitioner partitioner() {
		return (r, t) -> new VesselTimePartition(r, t.getVesselId(), r.epochIntervals(t.getTimestamp()));
	}

}