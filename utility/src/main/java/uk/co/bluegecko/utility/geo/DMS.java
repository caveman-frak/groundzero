package uk.co.bluegecko.utility.geo;

import org.apache.commons.lang3.builder.ToStringBuilder;
import uk.co.bluegecko.common.style.LombokStyle;

public interface DMS extends Angle {

	int getDegrees();

	int getMinutes();

	double getSeconds();

	@Override
	double decimal();

	@Override
	default double radians() {
		return Math.toRadians(decimal());
	}

	default ToStringBuilder getToStringBuilder() {
		return new ToStringBuilder(this, new LombokStyle())
				.append("degrees", getDegrees())
				.append("minutes", getMinutes())
				.append("seconds", getSeconds());
	}

}