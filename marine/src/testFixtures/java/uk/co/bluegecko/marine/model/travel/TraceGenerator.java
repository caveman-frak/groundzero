package uk.co.bluegecko.marine.model.travel;

import java.awt.Shape;
import java.time.Clock;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicReference;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import uk.co.bluegecko.common.generate.Generator;

@RequiredArgsConstructor(staticName = "generator")
public class TraceGenerator implements Generator<Trace> {

	@NonNull
	private final UUID vessel;
	@NonNull
	private final Clock clock;
	@NonNull
	private final Shape bounds;
	@NonNull
	private final Course course;
	private final AtomicReference<Trace> last = new AtomicReference<>(initialTrace());

	private Trace initialTrace() {
		return Trace.builder()
				.vesselId(vessel)
				.timestamp(clock.instant())
				.coordinates(bounds.getBounds().getCenterY(), bounds.getBounds().getCenterX())
				.build();
	}

	@Override
	public Optional<Trace> generate() {
		Trace trace = last.getAndUpdate(course.next());
		if (bounds.contains(trace.position())) {
			return Optional.of(trace);
		} else {
			return Optional.empty();
		}
	}

}