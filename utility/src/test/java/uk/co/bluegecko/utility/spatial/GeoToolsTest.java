package uk.co.bluegecko.utility.spatial;

import static com.javadocmd.simplelatlng.LatLngTool.distance;
import static com.javadocmd.simplelatlng.LatLngTool.initialBearing;
import static com.javadocmd.simplelatlng.LatLngTool.travel;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.withinPercentage;

import com.javadocmd.simplelatlng.LatLng;
import com.javadocmd.simplelatlng.util.LengthUnit;
import java.time.Duration;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class GeoToolsTest {

	private Vessel<LatLng> vessel;

	@BeforeEach
	void setUp() {
		vessel = Vessel.<LatLng>builder()
				.position(new LatLng(0.0, 0.0))
				.speed(10.0)
				.bearing(0.0)
				.rateOfTurn(0.0)
				.build();
	}

	@Test
	void calcDestination() {
		assertThat(travel(vessel.getPosition(), vessel.getBearing(), vessel.distance(Duration.ofHours(1)),
				LengthUnit.NAUTICAL_MILE))
				.isEqualTo(new LatLng(0.166554, 0.0));
	}

	@Test
	void calcDistance() {
		assertThat(distance(vessel.getPosition(), new LatLng(0.166554, 0.0), LengthUnit.NAUTICAL_MILE))
				.isCloseTo(10.0, withinPercentage(0.01));
	}

	@Test
	void calcBearing() {
		assertThat(initialBearing(vessel.getPosition(), new LatLng(0.166554, 0.0)))
				.isCloseTo(0.0, withinPercentage(0.01));
	}

}