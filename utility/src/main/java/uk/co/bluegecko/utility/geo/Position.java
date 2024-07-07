package uk.co.bluegecko.utility.geo;

import lombok.Builder;
import lombok.Value;

@Builder
@Value
public class Position {

	Latitude latitude;
	Longitude longitude;
	
}