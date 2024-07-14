package uk.co.bluegecko.utility.spatial;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.withinPercentage;
import static si.uom.NonSI.KNOT;

import java.time.Duration;
import org.junit.jupiter.api.Test;
import tech.units.indriya.quantity.Quantities;
import uk.co.bluegecko.utility.spatial.Vessel.VesselBuilder;

class VesselTest {

	@Test
	void calcDistanceAtZeroForHour() {
		assertThat(Vessel.builder().build().distance(Duration.ofHours(1)).getValue().doubleValue())
				.isCloseTo(0, withinPercentage(0.1));
	}

	@Test
	void calcDistanceAtTenForHour() {
		VesselBuilder<Object> objectVesselBuilder = Vessel.builder();
		assertThat(objectVesselBuilder.knots(Quantities.getQuantity((double) 10, KNOT)).build()
				.distance(Duration.ofHours(1)).getValue().doubleValue())
				.isCloseTo(10, withinPercentage(0.1));
	}

	@Test
	void calcDistanceAtTenFor90Minutes() {
		VesselBuilder<Object> objectVesselBuilder = Vessel.builder();
		assertThat(objectVesselBuilder.knots(Quantities.getQuantity((double) 10, KNOT)).build()
				.distance(Duration.ofMinutes(90)).getValue().doubleValue())
				.isCloseTo(15, withinPercentage(0.1));
	}

	@Test
	void calcDistanceAtTenFor2Days() {
		VesselBuilder<Object> objectVesselBuilder = Vessel.builder();
		assertThat(objectVesselBuilder.knots(Quantities.getQuantity((double) 10, KNOT)).build()
				.distance(Duration.ofDays(2)).getValue().doubleValue())
				.isCloseTo(480, withinPercentage(0.1));
	}

}