package uk.co.bluegecko.marine.model.position;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.InstanceOfAssertFactories.COLLECTION;

import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class TrackSummaryTest extends AbstractTrackTest {

	TrackSummary trackSummary;
	private UUID vessel1;
	private UUID vessel2;

	@BeforeEach
	void setUp() {
		setUpClock();
		trackSummary = new TrackSummary();
		vessel1 = new UUID(0, 1);
		vessel2 = new UUID(0, 2);
	}

	@Test
	void condenseEmptyRoute() {
		assertThat(trackSummary.condenseRoute(List.of())).as("Tracks").isEmpty();
	}

	@Test
	void condenseSingleRoute() {
		List<Track> tracks = List.of(buildLocationTrack(10, buildTrace(vessel1)));
		assertThat(trackSummary.condenseRoute(tracks)).as("Tracks").hasSize(1);
	}

	@Test
	void condenseSimpleRoute() {
		List<Track> tracks = List.of(
				buildLocationTrack(10, buildTrace(vessel1)),
				buildLocationTrack(11, buildTrace(vessel2)));
		assertThat(trackSummary.condenseRoute(tracks)).as("Tracks").hasSize(2);
	}

	@Test
	void condenseMergeableRoute() {
		List<Track> tracks = List.of(
				buildLocationTrack(10, buildTrace(vessel1)),
				buildLocationTrack(10, buildTrace(vessel2)));
		List<Track> result = trackSummary.condenseRoute(tracks);
		assertThat(result).as("Tracks").hasSize(1);
		assertThat(result.getFirst())
				.extracting(Track::getTraces, COLLECTION).as("Traces").hasSize(2);
	}

	@Test
	void condenseMergeableRouteWithTime() {
		List<Track> tracks = List.of(
				buildLocationTimeTrack(10, 10, buildTrace(vessel1)),
				buildLocationTimeTrack(10, 20, buildTrace(vessel1)));
		List<Track> result = trackSummary.condenseRoute(tracks);
		assertThat(result).as("Tracks").hasSize(1);
		assertThat(result.getFirst())
				.extracting(Track::getTraces, COLLECTION).as("Traces").hasSize(1);
	}

	@Test
	void condenseMergeableRouteDuplicateTrace() {
		List<Track> tracks = List.of(
				buildLocationTrack(10, buildTrace(vessel1)),
				buildLocationTrack(10, buildTrace(vessel1)));
		List<Track> result = trackSummary.condenseRoute(tracks);
		assertThat(result).as("Tracks").hasSize(1);
		assertThat(result.getFirst())
				.extracting(Track::getTraces, COLLECTION).as("Traces").hasSize(1);
	}

}