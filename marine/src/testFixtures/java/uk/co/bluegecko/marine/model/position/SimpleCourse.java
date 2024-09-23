package uk.co.bluegecko.marine.model.position;

import static systems.uom.ucum.UCUM.DEGREE;
import static systems.uom.ucum.UCUM.RADIAN;

import java.awt.geom.Point2D;
import java.time.Clock;
import java.time.Instant;
import java.util.function.UnaryOperator;
import javax.measure.Quantity;
import javax.measure.quantity.Length;
import javax.measure.quantity.Speed;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.Accessors;
import lombok.experimental.FieldDefaults;
import tech.units.indriya.quantity.Quantities;
import uk.co.bluegecko.marine.model.compass.Bearing;
import uk.co.bluegecko.marine.model.compass.Coordinate;

@Getter
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
@Accessors(fluent = true)
public class SimpleCourse implements Course {

	Calculator calculator;
	Clock clock;
	Quantity<Speed> speed;
	Point2D vector;
	@Getter(lazy = true)
	Bearing bearing = new Bearing(
			Quantities.getQuantity(Math.atan2(vector.getX(), vector.getY()), RADIAN).to(DEGREE));


	@Override
	public UnaryOperator<Trace> next() {
		return t -> {
			Instant now = clock.instant();
			Quantity<Length> distance = calculator.distance(speed(), t.getTimestamp(), now);
			Coordinate destination = calculator.pointOnBearing(t.getCoordinate(), distance, bearing());

			return t.toBuilder()
					.timestamp(now)
					.coordinate(destination)
					.speed(speed())
					.bearing(bearing())
					.build();
		};
	}

}