package uk.co.bluegecko.utility.geo;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

class CoordinateTest {

	@Test
	void stringify() {
		assertThat(Coordinate.builder()
				.latitude(new Latitude(20, 30, 40.50, Compass.S))
				.longitude(new Longitude(30, 40, 50.60, Compass.W))
				.build().toString())
				.isEqualTo("""
						Coordinate(latitude=Latitude(degrees=20,minutes=30,seconds=40.5,hemisphere=S), \
						longitude=Longitude(degrees=30,minutes=40,seconds=50.6,hemisphere=W))""");
	}

}