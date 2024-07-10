package uk.co.bluegecko.utility.geo;

import lombok.Builder;
import lombok.Value;
import lombok.experimental.Accessors;
import tech.units.indriya.ComparableQuantity;

@Builder
@Value
@Accessors(fluent = true)
public class DegreeDecimal implements Angle {

	ComparableQuantity<javax.measure.quantity.Angle> decimal;

}