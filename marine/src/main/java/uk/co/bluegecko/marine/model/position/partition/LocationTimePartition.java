package uk.co.bluegecko.marine.model.position.partition;

import com.uber.h3core.H3Core;
import java.util.Optional;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class LocationTimePartition extends LocationPartition implements ByTime {

	private final long epochIntervals;

	public LocationTimePartition(Resolution resolution, long cell, long epochIntervals) {
		super(resolution, cell);

		this.epochIntervals = epochIntervals;
	}

	@Override
	public long epochIntervals() {
		return epochIntervals;
	}

	public static Partitioner partitioner(H3Core core) {
		return (r, t) -> new LocationTimePartition(
				r, core.latLngToCell(t.latitude(), t.longitude(), r.h3()), r.epochIntervals(t.getTimestamp()));
	}

	static Optional<Partition> from(Partition partition) {
		if ((partition instanceof ByLocation pL) && (partition instanceof ByTime pT)) {
			return Optional.of(new LocationTimePartition(partition.resolution(), pL.cell(), pT.epochIntervals()));
		}
		return Optional.empty();
	}

}