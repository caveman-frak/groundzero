package uk.co.bluegecko.marine.model.position.partition;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.params.provider.Arguments.arguments;

import java.util.Optional;
import java.util.UUID;
import java.util.stream.Stream;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

class PartitionCondenserTest {

	static Resolution resolution = Resolution.MEDIUM;
	static long cell = 596538831059025919L;
	static long epochIntervals = 438300L;
	static UUID vessel = new UUID(0, 0);

	static VesselLocationTimePartition vesselLocationTime;
	static LocationTimePartition locationTime;
	static LocationPartition location;
	static VesselTimePartition vesselTime;

	@BeforeAll
	static void setUp() {
		vesselLocationTime = new VesselLocationTimePartition(resolution, vessel, cell, epochIntervals);
		locationTime = new LocationTimePartition(resolution, cell, epochIntervals);
		location = new LocationPartition(resolution, cell);
		vesselTime = new VesselTimePartition(resolution, vessel, epochIntervals);
	}

	@ParameterizedTest
	@MethodSource("partitionProvider")
	void condense(Partition<?> partition, Class<? extends Partition<?>> partitionClass, Partition<?> result) {
		assertThat(PartitionCondenser.condense(
				partition, partitionClass)).isEqualTo(Optional.ofNullable(result));
	}

	static Stream<Arguments> partitionProvider() {
		return Stream.of(
				arguments(vesselLocationTime, LocationPartition.class, location),
				arguments(vesselLocationTime, LocationTimePartition.class, locationTime),
				arguments(vesselLocationTime, VesselLocationTimePartition.class, vesselLocationTime),
				arguments(vesselLocationTime, VesselTimePartition.class, vesselTime),
				arguments(locationTime, LocationPartition.class, location),
				arguments(locationTime, LocationTimePartition.class, locationTime),
				arguments(locationTime, VesselLocationTimePartition.class, null),
				arguments(locationTime, VesselTimePartition.class, null),
				arguments(location, LocationPartition.class, location),
				arguments(location, LocationTimePartition.class, null),
				arguments(location, VesselLocationTimePartition.class, null),
				arguments(location, VesselTimePartition.class, null),
				arguments(vesselTime, LocationPartition.class, null),
				arguments(vesselTime, LocationTimePartition.class, null),
				arguments(vesselTime, VesselLocationTimePartition.class, null),
				arguments(vesselTime, VesselTimePartition.class, vesselTime)
		);
	}

}