package uk.co.bluegecko.marine.model.position.partition;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.IOException;
import java.time.Instant;
import java.util.Collection;
import java.util.Set;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import uk.co.bluegecko.marine.model.position.Track;

class TimePartitionTest extends AbstractPartitionTest {

	@BeforeEach
	void setUp() throws IOException {
		setUpTraces();
		partitioner = TimePartition.partitioner();
	}

	@Test
	void tracksFine() {
		Set<Track> tracks = Track.fromTraces(Resolution.FINE, traces, partitioner);
		assertThat(tracks).hasSize(11)
				.extracting(t -> ((ByTime) t.getPartition()).epochIntervals()).as("Time")
				.containsExactlyInAnyOrder(2629800L, 2629801L, 2629802L, 2629803L, 2629804L, 2629805L, 2629806L,
						2629807L, 2629808L, 2629809L, 2629810L);
		assertThat(tracks).extracting(Track::getTraces).extracting(Collection::size).as("No of Traces")
				.containsExactly(1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1);
		assertThat(tracks).extracting(Track::getEarliest).extracting(Instant::toString).as("Earliest")
				.containsOnly(
						"2020-01-01T12:00:00Z", "2020-01-01T12:10:00Z", "2020-01-01T12:20:00Z", "2020-01-01T12:30:00Z",
						"2020-01-01T12:40:00Z", "2020-01-01T12:50:00Z", "2020-01-01T13:00:00Z", "2020-01-01T13:10:00Z",
						"2020-01-01T13:20:00Z", "2020-01-01T13:30:00Z", "2020-01-01T13:40:00Z");
	}

	@Test
	void tracksMedium() {
		Set<Track> tracks = Track.fromTraces(Resolution.MEDIUM, traces, partitioner);
		assertThat(tracks).hasSize(2)
				.extracting(t -> ((ByTime) t.getPartition()).epochIntervals()).as("Time")
				.containsOnly(438300L, 438301L);
		assertThat(tracks).extracting(Track::getTraces).extracting(Collection::size).as("No of Traces")
				.containsExactlyInAnyOrder(6, 5);
		assertThat(tracks).extracting(Track::getEarliest).extracting(Instant::toString).as("Earliest")
				.containsOnly("2020-01-01T12:00:00Z", "2020-01-01T13:00:00Z");
	}

	@Test
	void tracksCoarse() {
		Set<Track> tracks = Track.fromTraces(Resolution.COARSE, traces, partitioner);
		assertThat(tracks).hasSize(1)
				.extracting(t -> ((ByTime) t.getPartition()).epochIntervals()).as("Time")
				.containsExactly(73050L);
		assertThat(tracks).extracting(Track::getTraces).extracting(Collection::size).as("No of Traces")
				.containsExactly(11);
		assertThat(tracks).extracting(Track::getEarliest).extracting(Instant::toString).as("Earliest")
				.containsExactly("2020-01-01T12:00:00Z");
	}

}