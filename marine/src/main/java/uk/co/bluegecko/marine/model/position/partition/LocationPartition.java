package uk.co.bluegecko.marine.model.position.partition;

import com.uber.h3core.H3Core;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class LocationPartition extends AbstractPartition implements ByLocation {

	private final long cell;

	protected LocationPartition(Resolution resolution, long cell) {
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

}