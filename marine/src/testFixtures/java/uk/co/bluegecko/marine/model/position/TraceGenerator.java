package uk.co.bluegecko.marine.model.position;

import java.awt.Shape;
import java.time.Duration;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicReference;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Builder.Default;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import uk.co.bluegecko.common.clock.SteppingClock;
import uk.co.bluegecko.common.generate.Generator;

@RequiredArgsConstructor(staticName = "generator")
@FieldDefaults(makeFinal = true, level = AccessLevel.PROTECTED)
@Builder
public class TraceGenerator implements Generator<Trace> {

	@NonNull
	UUID vessel;
	@NonNull
	SteppingClock clock;
	@NonNull
	@Default
	Duration interval = Duration.ofMinutes(10);
	@NonNull
	Shape bounds;
	@NonNull
	Course course;
	AtomicReference<Trace> last = new AtomicReference<>(initialTrace());

	protected Trace initialTrace() {
		return Trace.builder()
				.vesselId(vessel)
				.timestamp(clock.instant())
				.coordinates(bounds.getBounds().getCenterY(), bounds.getBounds().getCenterX())
				.build();
	}

	@Override
	public Optional<Trace> generate() {
		clock.tick(interval);
		Trace trace = last.getAndUpdate(course.next());
		if (bounds.contains(trace.position())) {
			return Optional.of(trace);
		} else {
			return Optional.empty();
		}
	}

}