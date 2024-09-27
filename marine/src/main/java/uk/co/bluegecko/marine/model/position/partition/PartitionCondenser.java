package uk.co.bluegecko.marine.model.position.partition;

import java.util.Map;
import java.util.Optional;
import java.util.function.Function;

public class PartitionCondenser {

	private static final Map<Class<? extends Partition<?>>, Function<Partition<?>, Optional<Partition<?>>>> CONDENSERS =
			Map.of(
					LocationPartition.class, LocationPartition::from,
					LocationTimePartition.class, LocationTimePartition::from,
					TimePartition.class, TimePartition::from,
					TimeLocationPartition.class, TimeLocationPartition::from,
					VesselPartition.class, VesselPartition::from,
					VesselLocationTimePartition.class, VesselLocationTimePartition::from,
					VesselTimePartition.class, VesselTimePartition::from,
					VesselTimeLocationPartition.class, VesselTimeLocationPartition::from
			);

	public static Optional<Partition<?>> condense(Partition<?> partition,
			Class<? extends Partition<?>> partitionClass) {
		var f = CONDENSERS.get(partitionClass);

		return f != null ? f.apply(partition) : Optional.empty();
	}

}