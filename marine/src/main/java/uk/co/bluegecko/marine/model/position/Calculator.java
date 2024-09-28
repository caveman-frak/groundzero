package uk.co.bluegecko.marine.model.position;

import static javax.measure.MetricPrefix.KILO;
import static javax.measure.MetricPrefix.MILLI;
import static org.locationtech.spatial4j.distance.DistanceUtils.KM_TO_DEG;
import static systems.uom.ucum.UCUM.DEGREE;
import static systems.uom.ucum.UCUM.METER;
import static systems.uom.ucum.UCUM.SECOND;

import java.time.Duration;
import java.time.Instant;
import javax.measure.Quantity;
import javax.measure.quantity.Angle;
import javax.measure.quantity.Length;
import javax.measure.quantity.Speed;
import org.locationtech.spatial4j.context.SpatialContext;
import org.locationtech.spatial4j.shape.Point;
import tech.units.indriya.quantity.Quantities;
import uk.co.bluegecko.marine.model.compass.Bearing;
import uk.co.bluegecko.marine.model.compass.Coordinate;

public record Calculator(SpatialContext ctx) {

	public Quantity<Length> distance(Quantity<Speed> speed, Instant start, Instant end) {
		return distance(speed, Duration.between(start, end));
	}

	public Quantity<Length> distance(Quantity<Speed> speed, Duration time) {
		return speed.multiply(Quantities.getQuantity(time.toMillis(), MILLI(SECOND))).asType(Length.class);
	}

	public Quantity<Angle> distanceAsAngle(Quantity<Length> distance) {
		return Quantities.getQuantity(distance.to(KILO(METER)).multiply(KM_TO_DEG).getValue(), DEGREE);
	}

	public Coordinate pointOnBearing(Coordinate initial, Quantity<Length> distance, Bearing bearing) {
		return new Coordinate(pointOnBearing(initial.toPoint(ctx.getShapeFactory()),
				distanceAsAngle(distance), bearing));
	}

	private Point pointOnBearing(Point point, Quantity<Angle> distanceAsAngle, Bearing bearing) {
		return ctx.getDistCalc().pointOnBearing(point, distanceAsAngle.to(DEGREE).getValue().doubleValue(),
				bearing.to(DEGREE).getValue().doubleValue(), ctx, point);
	}

}