package uk.co.bluegecko.marine.model.travel;

import java.awt.geom.Point2D;
import java.time.Clock;
import java.time.Duration;
import java.time.Instant;
import java.util.function.UnaryOperator;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class SimpleCourse implements Course {

	private final Clock clock;
	private final double speed;
	private final Point2D vector;
	@Getter(lazy = true)
	private final double bearing = (Math.toDegrees(Math.atan2(vector.getX(), vector.getY())) + 360) % 360;


	@Override
	public UnaryOperator<Trace> next() {
		return t -> {
			Instant now = clock.instant();
			double seconds = (double) Duration.between(t.getTimestamp(), now).toMillis() / 1000;
			return t.toBuilder()
					.timestamp(now)
					.latitude(t.getLatitude() + vector.getY() * seconds * speed)
					.longitude(t.getLongitude() + vector.getX() * seconds * speed)
					.speed(speed)
					.bearing(getBearing())
					.build();
		};
	}

}