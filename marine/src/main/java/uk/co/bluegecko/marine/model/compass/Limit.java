package uk.co.bluegecko.marine.model.compass;

import static systems.uom.ucum.UCUM.DEGREE;

import java.util.EnumSet;
import java.util.Set;
import javax.measure.quantity.Angle;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.Accessors;
import lombok.experimental.FieldDefaults;
import tech.units.indriya.quantity.Quantities;
import tech.units.indriya.quantity.QuantityRange;

@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
@Accessors(fluent = true)
@Getter
public enum Limit {
	BEARING(QuantityRange.of(
			Quantities.getQuantity(0, DEGREE), Quantities.getQuantity(360, DEGREE)),
			EnumSet.noneOf(Hemisphere.class)),
	LATITUDE(QuantityRange.of(
			Quantities.getQuantity(-90, DEGREE), Quantities.getQuantity(90, DEGREE)),
			EnumSet.of(Hemisphere.NORTH, Hemisphere.SOUTH)),
	LONGITUDE(QuantityRange.of(
			Quantities.getQuantity(-180, DEGREE), Quantities.getQuantity(180, DEGREE)),
			EnumSet.of(Hemisphere.EAST, Hemisphere.WEST)),
	UNLIMITED(QuantityRange.of(null, null), EnumSet.noneOf(Hemisphere.class));

	QuantityRange<Angle> range;
	Set<Hemisphere> hemispheres;

}