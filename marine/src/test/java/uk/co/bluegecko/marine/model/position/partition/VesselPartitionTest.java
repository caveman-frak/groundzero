package uk.co.bluegecko.marine.model.position.partition;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.IOException;
import java.time.Instant;
import java.util.Collection;
import java.util.Set;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import uk.co.bluegecko.marine.model.position.Track;

class VesselPartitionTest extends AbstractPartitionTest {

	@BeforeEach
	void setUp() throws IOException {
		setUpTraces();
		partitioner = VesselPartition.partitioner();
	}

	@Test
	void tracksFine() {
		Set<Track> tracks = Track.fromTraces(Resolution.FINE, traces, partitioner);
		assertThat(tracks).hasSize(1)
				.extracting(t -> ((ByVessel) t.getPartition()).vessel()).as("Vessel")
				.containsOnly(new UUID(0, 0));
		assertThat(tracks).extracting(Track::getTraces).extracting(Collection::size).as("No of traces")
				.containsExactly(11);
		assertThat(tracks).extracting(Track::getEarliest).extracting(Instant::toString).as("Earliest")
				.containsOnly("2020-01-01T12:00:00Z");
	}

	@Test
	void tracksMedium() {
		Set<Track> tracks = Track.fromTraces(Resolution.MEDIUM, traces, partitioner);
		assertThat(tracks).hasSize(1)
				.extracting(t -> ((ByVessel) t.getPartition()).vessel()).as("Vessel")
				.containsOnly(new UUID(0, 0));
		assertThat(tracks).extracting(Track::getTraces).extracting(Collection::size).as("No of traces")
				.containsExactly(11);
		assertThat(tracks).extracting(Track::getEarliest).extracting(Instant::toString).as("Earliest")
				.containsOnly("2020-01-01T12:00:00Z");
	}

	@Test
	void tracksCoarse() {
		Set<Track> tracks = Track.fromTraces(Resolution.COARSE, traces, partitioner);
		assertThat(tracks).hasSize(1)
				.extracting(t -> ((ByVessel) t.getPartition()).vessel()).as("Vessel")
				.containsOnly(new UUID(0, 0));
		assertThat(tracks).extracting(Track::getTraces).extracting(Collection::size).as("No of traces")
				.containsExactly(11);
		assertThat(tracks).extracting(Track::getEarliest).extracting(Instant::toString).as("Earliest")
				.containsOnly("2020-01-01T12:00:00Z");
	}

}