package uk.co.bluegecko.utility.spatial;

import static com.javadocmd.simplelatlng.LatLngTool.distance;
import static com.javadocmd.simplelatlng.LatLngTool.initialBearing;
import static com.javadocmd.simplelatlng.LatLngTool.travel;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.withinPercentage;

import com.javadocmd.simplelatlng.LatLng;
import com.javadocmd.simplelatlng.util.LengthUnit;
import java.time.Duration;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.Test;

public class SimpleLatingTest extends AbstractSpatialTest<LatLng> {

	@NotNull
	@Override
	protected LatLng position(double lat, double lng) {
		return new LatLng(lat, lng);
	}

	@Test
	@Override
	void calcDestinationFromPointWithBearingAndDistance() {
		assertThat(travel(vessel.getPosition(), vessel.getBearing(),
				vessel.distance(Duration.ofHours(1)).getValue().doubleValue(),
				LengthUnit.NAUTICAL_MILE))
				.isEqualTo(position(0.166554, 0.0));
	}

	@Test
	@Override
	void calcDistanceFromPointToPoint() {
		assertThat(distance(vessel.getPosition(), position(0.166554, 0.0), LengthUnit.NAUTICAL_MILE))
				.isCloseTo(10.0, withinPercentage(0.01));
	}

	@Test
	@Override
	void calcBearingFromPointToPoint() {
		assertThat(initialBearing(vessel.getPosition(), position(0.166554, 0.0)))
				.isCloseTo(0.0, withinPercentage(0.01));
	}

}