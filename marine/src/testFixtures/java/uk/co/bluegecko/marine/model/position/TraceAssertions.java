package uk.co.bluegecko.marine.model.position;

import org.assertj.core.api.AbstractAssert;
import org.assertj.core.api.WritableAssertionInfo;
import org.assertj.core.data.Offset;

public class TraceAssertions extends AbstractAssert<TraceAssertions, Trace> {

	public WritableAssertionInfo info;

	protected TraceAssertions(Trace trace) {
		super(trace, TraceAssertions.class);

		info = new WritableAssertionInfo(null);
	}

	public static TraceAssertions assertThat(Trace actual) {
		return new TraceAssertions(actual);
	}

	public TraceAssertions isEqualTo(Trace expected, Offset<Double> offset) {
		return myself;
	}

}