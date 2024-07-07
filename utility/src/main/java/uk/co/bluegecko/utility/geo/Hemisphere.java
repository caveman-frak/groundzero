package uk.co.bluegecko.utility.geo;

import org.apache.commons.lang3.builder.ToStringBuilder;

public interface Hemisphere extends Angle {

	Compass getHemisphere();

	@Override
	default ToStringBuilder getToStringBuilder() {
		return Angle.super.getToStringBuilder().append("direction", getHemisphere());
	}


}