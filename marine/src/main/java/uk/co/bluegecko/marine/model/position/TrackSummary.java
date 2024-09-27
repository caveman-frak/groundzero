package uk.co.bluegecko.marine.model.position;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import uk.co.bluegecko.common.utility.MergeGatherer;
import uk.co.bluegecko.marine.model.position.partition.LocationPartition;
import uk.co.bluegecko.marine.model.position.partition.Partition;

public class TrackSummary {

	public List<Track> condense(Collection<Track> tracks, Class<? extends Partition<?>> partitionClass) {
		return tracks.stream().map(t -> t.withPartition(partitionClass))
				.filter(Optional::isPresent).map(Optional::get)
				.gather(new MergeGatherer<>())
				.toList();
	}

	public List<Track> condenseRoute(Collection<Track> tracks) {
		return condense(tracks, LocationPartition.class);
	}

}