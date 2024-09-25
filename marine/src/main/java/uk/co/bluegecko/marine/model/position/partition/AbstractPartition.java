package uk.co.bluegecko.marine.model.position.partition;

import lombok.EqualsAndHashCode;
import lombok.ToString;

@EqualsAndHashCode
@ToString
public abstract class AbstractPartition implements Partition {

	private final Resolution resolution;

	protected AbstractPartition(Resolution resolution) {
		this.resolution = resolution;
	}

	@Override
	public Resolution resolution() {
		return resolution;
	}

}