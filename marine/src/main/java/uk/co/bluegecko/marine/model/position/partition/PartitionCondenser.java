package uk.co.bluegecko.marine.model.position.partition;

import java.util.Map;
import java.util.Optional;
import java.util.function.Function;

public class PartitionCondenser {

	private static final Map<Class<? extends Partition>, Function<Partition, Optional<Partition>>> condensors =
			Map.of(LocationTimeVesselPartition.class, LocationTimeVesselPartition::from,
					LocationTimePartition.class, LocationTimePartition::from,
					LocationPartition.class, LocationPartition::from,
					VesselTimePartition.class, VesselTimePartition::from);

	public static Optional<Partition> condense(Partition partition, Class<? extends Partition> partitionClass) {
		var f = condensors.get(partitionClass);

		return f != null ? f.apply(partition) : Optional.empty();
	}

}