package uk.co.bluegecko.utility.spatial;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.withinPercentage;

import java.time.Duration;
import org.junit.jupiter.api.Test;

class VesselTest {

	@Test
	void calcDistanceAtZeroForHour() {
		assertThat(Vessel.builder().build().distance(Duration.ofHours(1)))
				.isCloseTo(0, withinPercentage(0.1));
	}

	@Test
	void calcDistanceAtTenForHour() {
		assertThat(Vessel.builder().speed(10).build().distance(Duration.ofHours(1)))
				.isCloseTo(10, withinPercentage(0.1));
	}

	@Test
	void calcDistanceAtTenFor90Minutes() {
		assertThat(Vessel.builder().speed(10).build().distance(Duration.ofMinutes(90)))
				.isCloseTo(15, withinPercentage(0.1));
	}

	@Test
	void calcDistanceAtTenFor2Days() {
		assertThat(Vessel.builder().speed(10).build().distance(Duration.ofDays(2)))
				.isCloseTo(480, withinPercentage(0.1));
	}

}