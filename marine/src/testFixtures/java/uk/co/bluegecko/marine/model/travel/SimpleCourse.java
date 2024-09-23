package uk.co.bluegecko.marine.model.travel;

import static javax.measure.MetricPrefix.KILO;
import static systems.uom.ucum.UCUM.DEGREE;
import static systems.uom.ucum.UCUM.METER;
import static systems.uom.ucum.UCUM.RADIAN;
import static systems.uom.ucum.UCUM.SECOND;

import java.awt.geom.Point2D;
import java.time.Clock;
import java.time.Duration;
import java.time.Instant;
import java.util.Map;
import java.util.function.UnaryOperator;
import javax.measure.Quantity;
import javax.measure.quantity.Length;
import javax.measure.quantity.Speed;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.Accessors;
import lombok.experimental.FieldDefaults;
import org.locationtech.spatial4j.context.SpatialContext;
import org.locationtech.spatial4j.context.SpatialContextFactory;
import org.locationtech.spatial4j.distance.DistanceUtils;
import org.locationtech.spatial4j.shape.Point;
import tech.units.indriya.quantity.Quantities;
import uk.co.bluegecko.marine.model.compass.Bearing;
import uk.co.bluegecko.marine.model.compass.Coordinate;

@Getter
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
@Accessors(fluent = true)
public class SimpleCourse implements Course {

	Clock clock;
	Quantity<Speed> speed;
	Point2D vector;
	@Getter(lazy = true)
	Bearing bearing = new Bearing(
			Quantities.getQuantity(Math.atan2(vector.getX(), vector.getY()), RADIAN).to(DEGREE));
	@Getter(lazy = true)
	SpatialContext ctx = SpatialContextFactory.makeSpatialContext(Map.of("geo", "true"), null);


	@Override
	public UnaryOperator<Trace> next() {
		return t -> {
			Instant now = clock.instant();
			double seconds = (double) Duration.between(t.getTimestamp(), now).toMillis() / 1000;
			Quantity<Length> distance = speed().multiply(Quantities.getQuantity(seconds, SECOND))
					.asType(Length.class);
			double degrees = distance.to(KILO(METER)).getValue().doubleValue() * DistanceUtils.KM_TO_DEG;
			Point destination = ctx().getDistCalc()
					.pointOnBearing(t.getCoordinate().toPoint(ctx()), degrees,
							bearing().to(DEGREE).getValue().doubleValue(), ctx(), null);
			return t.toBuilder()
					.timestamp(now)
					.coordinate(new Coordinate(destination))
					.speed(speed)
					.bearing(bearing())
					.build();
		};
	}

}