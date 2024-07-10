package uk.co.bluegecko.utility.geo;

import lombok.Builder;
import lombok.Value;

@Builder
@Value
public class Coordinate {

	Latitude latitude;
	Longitude longitude;

}