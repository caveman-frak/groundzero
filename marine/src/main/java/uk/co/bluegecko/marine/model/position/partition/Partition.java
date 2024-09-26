package uk.co.bluegecko.marine.model.position.partition;

import java.util.Optional;

public interface Partition {

	Resolution resolution();

	default Optional<Partition> to(Class<? extends Partition> partitionClass) {
		return PartitionCondenser.condense(this, partitionClass);
	}

}