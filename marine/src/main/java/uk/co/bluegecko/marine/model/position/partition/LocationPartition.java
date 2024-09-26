package uk.co.bluegecko.marine.model.position.partition;

import com.uber.h3core.H3Core;
import java.util.Optional;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class LocationPartition extends AbstractPartition implements ByLocation {

	private final long cell;

	public LocationPartition(Resolution resolution, long cell) {
		super(resolution);

		this.cell = cell;
	}

	@Override
	public long cell() {
		return cell;
	}

	public static Partitioner partitioner(H3Core core) {
		return (r, t) -> new LocationPartition(r, core.latLngToCell(t.latitude(), t.longitude(), r.h3()));
	}

	static Optional<Partition> from(Partition partition) {
		if (partition instanceof ByLocation pL) {
			return Optional.of(new LocationPartition(partition.resolution(), pL.cell()));
		}
		return Optional.empty();
	}

}