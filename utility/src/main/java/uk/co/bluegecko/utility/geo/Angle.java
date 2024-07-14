package uk.co.bluegecko.utility.geo;

import static tech.units.indriya.unit.Units.RADIAN;

import tech.units.indriya.ComparableQuantity;

public interface Angle {

	ComparableQuantity<javax.measure.quantity.Angle> decimal();

	default ComparableQuantity<javax.measure.quantity.Angle> radians() {
		return decimal().to(RADIAN);
	}

}