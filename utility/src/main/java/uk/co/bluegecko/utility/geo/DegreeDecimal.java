package uk.co.bluegecko.utility.geo;

import lombok.Builder;
import lombok.Value;
import lombok.experimental.Accessors;

@Builder
@Value
@Accessors(fluent = true)
public class DegreeDecimal implements Angle {

	double decimal;

}