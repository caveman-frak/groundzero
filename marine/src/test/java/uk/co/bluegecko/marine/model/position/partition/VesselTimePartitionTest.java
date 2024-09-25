package uk.co.bluegecko.marine.model.position.partition;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.IOException;
import java.time.Instant;
import java.util.Collection;
import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import uk.co.bluegecko.marine.model.position.Track;

class VesselTimePartitionTest extends PartitionTest {

	@BeforeEach
	void setUp() throws IOException {
		setUpTraces();
		partitioner = VesselTimePartition.partitioner();
	}

	@Test
	void tracksFine() {
		List<Track> tracks = Track.tracks(Resolution.FINE, traces, partitioner);
		assertThat(tracks).hasSize(11)
				.extracting(t -> ((ByVessel) t.getPartition()).vessel()).as("Vessel")
				.containsOnly(new UUID(0, 0));
		assertThat(tracks).extracting(t -> ((ByTime) t.getPartition()).epochIntervals())
				.containsExactlyInAnyOrder(2629800L, 2629801L, 2629802L, 2629803L, 2629804L, 2629805L, 2629806L,
						2629807L, 2629808L, 2629809L, 2629810L);
		assertThat(tracks).extracting(Track::getTraces).extracting(Collection::size).as("No of traces")
				.containsExactly(1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1);
		assertThat(tracks).extracting(Track::getEarliest).extracting(Instant::toString).as("Earliest")
				.containsOnly("2020-01-01T12:00:00Z", "2020-01-01T12:10:00Z", "2020-01-01T12:20:00Z",
						"2020-01-01T12:30:00Z", "2020-01-01T12:40:00Z", "2020-01-01T12:50:00Z", "2020-01-01T13:00:00Z",
						"2020-01-01T13:10:00Z", "2020-01-01T13:20:00Z", "2020-01-01T13:30:00Z", "2020-01-01T13:40:00Z");
	}

	@Test
	void tracksMedium() {
		List<Track> tracks = Track.tracks(Resolution.MEDIUM, traces, partitioner);
		assertThat(tracks).hasSize(2)
				.extracting(t -> ((ByVessel) t.getPartition()).vessel()).as("Vessel")
				.containsOnly(new UUID(0, 0));
		assertThat(tracks).extracting(t -> ((ByTime) t.getPartition()).epochIntervals()).as("Time")
				.containsOnly(438300L, 438301L);
		assertThat(tracks).extracting(Track::getTraces).extracting(Collection::size).as("No of traces")
				.containsExactlyInAnyOrder(5, 6);
		assertThat(tracks).extracting(Track::getEarliest).extracting(Instant::toString).as("Earliest")
				.containsOnly("2020-01-01T12:00:00Z", "2020-01-01T13:00:00Z");
	}

	@Test
	void tracksCoarse() {
		List<Track> tracks = Track.tracks(Resolution.COARSE, traces, partitioner);
		assertThat(tracks).hasSize(1)
				.extracting(t -> ((ByVessel) t.getPartition()).vessel()).as("Vessel")
				.containsOnly(new UUID(0, 0));
		assertThat(tracks).extracting(t -> ((ByTime) t.getPartition()).epochIntervals()).as("Time")
				.containsExactly(73050L);
		assertThat(tracks).extracting(Track::getTraces).extracting(Collection::size).as("No of Traces")
				.containsExactly(11);
		assertThat(tracks).extracting(Track::getEarliest).extracting(Instant::toString).as("Earliest")
				.containsExactly("2020-01-01T12:00:00Z");
	}

}