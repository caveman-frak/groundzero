package uk.co.bluegecko.common.style;

import org.apache.commons.lang3.builder.ToStringStyle;

public class LombokStyle extends ToStringStyle {

	public LombokStyle() {
		setUseShortClassName(true);
		setUseIdentityHashCode(false);
		setContentStart("(");
		setContentEnd(")");
	}

}