package uk.co.bluegecko.utility.geo;

import org.apache.commons.lang3.builder.ToStringBuilder;

public interface Hemisphere extends DMS {

	Compass getHemisphere();

	@Override
	default ToStringBuilder getToStringBuilder() {
		return DMS.super.getToStringBuilder().append("hemisphere", getHemisphere());
	}

}