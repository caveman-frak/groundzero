package uk.co.bluegecko.marine.model.position.partition;

import java.util.Optional;
import org.apache.commons.lang3.builder.CompareToBuilder;
import org.jetbrains.annotations.NotNull;

public record TimePartition(Resolution resolution, long epochIntervals)
		implements Partition<TimePartition>, ByTime {

	@Override
	public int compareTo(@NotNull TimePartition o) {
		return new CompareToBuilder()
				.append(resolution, o.resolution)
				.append(epochIntervals, o.epochIntervals)
				.build();
	}

	public static Partitioner partitioner() {
		return (r, t) -> new TimePartition(r, r.epochIntervals(t.getTimestamp()));
	}

	static Optional<Partition<?>> from(Partition<?> p) {
		if (p instanceof ByTime pT) {
			return Optional.of(new TimePartition(p.resolution(), pT.epochIntervals()));
		}
		return Optional.empty();
	}

}