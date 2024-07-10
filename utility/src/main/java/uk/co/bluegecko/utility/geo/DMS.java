package uk.co.bluegecko.utility.geo;

import org.apache.commons.lang3.builder.ToStringBuilder;
import uk.co.bluegecko.common.style.LombokStyle;

public interface DMS extends Angle {

	int getDegrees();

	int getMinutes();

	double getSeconds();

	default ToStringBuilder getToStringBuilder() {
		return new ToStringBuilder(this, new LombokStyle())
				.append("degrees", getDegrees())
				.append("minutes", getMinutes())
				.append("seconds", getSeconds());
	}

}