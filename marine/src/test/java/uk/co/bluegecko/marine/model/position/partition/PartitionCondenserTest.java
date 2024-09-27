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
	static UUID vessel1 = new UUID(0, 0);

	static LocationPartition location;
	static LocationTimePartition locationTime;
	static TimePartition time;
	static TimeLocationPartition timeLocation;
	static VesselPartition vessel;
	static VesselLocationTimePartition vesselLocationTime;
	static VesselTimePartition vesselTime;
	static VesselTimeLocationPartition vesselTimeLocation;

	@BeforeAll
	static void setUp() {
		location = new LocationPartition(resolution, cell);
		locationTime = new LocationTimePartition(resolution, cell, epochIntervals);
		time = new TimePartition(resolution, epochIntervals);
		timeLocation = new TimeLocationPartition(resolution, epochIntervals, cell);
		vessel = new VesselPartition(resolution, vessel1);
		vesselLocationTime = new VesselLocationTimePartition(resolution, vessel1, cell, epochIntervals);
		vesselTime = new VesselTimePartition(resolution, vessel1, epochIntervals);
		vesselTimeLocation = new VesselTimeLocationPartition(resolution, vessel1, epochIntervals, cell);
	}

	@ParameterizedTest
	@MethodSource("partitionProvider")
	void condense(Partition<?> partition, Class<? extends Partition<?>> partitionClass, Partition<?> result) {
		assertThat(PartitionCondenser.condense(
				partition, partitionClass)).isEqualTo(Optional.ofNullable(result));
	}

	static Stream<Arguments> partitionProvider() {
		return Stream.of(
				// location
				arguments(location, LocationPartition.class, location),
				arguments(location, LocationTimePartition.class, null),
				arguments(location, TimeLocationPartition.class, null),
				arguments(location, TimePartition.class, null),
				arguments(location, VesselLocationTimePartition.class, null),
				arguments(location, VesselTimeLocationPartition.class, null),
				arguments(location, VesselTimePartition.class, null),
				arguments(location, VesselPartition.class, null),
				// location time
				arguments(locationTime, LocationPartition.class, location),
				arguments(locationTime, LocationTimePartition.class, locationTime),
				arguments(locationTime, TimeLocationPartition.class, timeLocation),
				arguments(locationTime, TimePartition.class, time),
				arguments(locationTime, VesselLocationTimePartition.class, null),
				arguments(locationTime, VesselTimeLocationPartition.class, null),
				arguments(locationTime, VesselTimePartition.class, null),
				arguments(locationTime, VesselPartition.class, null),
				// time
				arguments(time, LocationPartition.class, null),
				arguments(time, LocationTimePartition.class, null),
				arguments(time, TimeLocationPartition.class, null),
				arguments(time, TimePartition.class, time),
				arguments(time, VesselLocationTimePartition.class, null),
				arguments(time, VesselTimeLocationPartition.class, null),
				arguments(time, VesselTimePartition.class, null),
				arguments(time, VesselPartition.class, null),
				// time location
				arguments(timeLocation, LocationPartition.class, location),
				arguments(timeLocation, LocationTimePartition.class, locationTime),
				arguments(timeLocation, TimeLocationPartition.class, timeLocation),
				arguments(timeLocation, TimePartition.class, time),
				arguments(timeLocation, VesselLocationTimePartition.class, null),
				arguments(timeLocation, VesselTimeLocationPartition.class, null),
				arguments(timeLocation, VesselTimePartition.class, null),
				arguments(timeLocation, VesselPartition.class, null),
				// vessel
				arguments(vessel, LocationPartition.class, null),
				arguments(vessel, LocationTimePartition.class, null),
				arguments(vessel, TimeLocationPartition.class, null),
				arguments(vessel, TimePartition.class, null),
				arguments(vessel, VesselLocationTimePartition.class, null),
				arguments(vessel, VesselTimeLocationPartition.class, null),
				arguments(vessel, VesselTimePartition.class, null),
				arguments(vessel, VesselPartition.class, vessel),
				// vessel location time
				arguments(vesselLocationTime, LocationPartition.class, location),
				arguments(vesselLocationTime, LocationTimePartition.class, locationTime),
				arguments(vesselLocationTime, TimeLocationPartition.class, timeLocation),
				arguments(vesselLocationTime, TimePartition.class, time),
				arguments(vesselLocationTime, VesselLocationTimePartition.class, vesselLocationTime),
				arguments(vesselLocationTime, VesselTimeLocationPartition.class, vesselTimeLocation),
				arguments(vesselLocationTime, VesselTimePartition.class, vesselTime),
				arguments(vesselLocationTime, VesselPartition.class, vessel),
				// vessel time
				arguments(vesselTime, LocationPartition.class, null),
				arguments(vesselTime, LocationTimePartition.class, null),
				arguments(vesselTime, TimeLocationPartition.class, null),
				arguments(vesselTime, TimePartition.class, time),
				arguments(vesselTime, VesselLocationTimePartition.class, null),
				arguments(vesselTime, VesselTimeLocationPartition.class, null),
				arguments(vesselTime, VesselTimePartition.class, vesselTime),
				arguments(vesselTime, VesselPartition.class, vessel),
				// vessel time location
				arguments(vesselTimeLocation, LocationPartition.class, location),
				arguments(vesselTimeLocation, LocationTimePartition.class, locationTime),
				arguments(vesselTimeLocation, TimeLocationPartition.class, timeLocation),
				arguments(vesselTimeLocation, TimePartition.class, time),
				arguments(vesselTimeLocation, VesselLocationTimePartition.class, vesselLocationTime),
				arguments(vesselTimeLocation, VesselTimeLocationPartition.class, vesselTimeLocation),
				arguments(vesselTimeLocation, VesselTimePartition.class, vesselTime),
				arguments(vesselTimeLocation, VesselPartition.class, vessel)
		);
	}

}