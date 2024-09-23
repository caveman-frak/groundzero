package uk.co.bluegecko.marine.model.position;

import static javax.measure.MetricPrefix.KILO;
import static javax.measure.MetricPrefix.MILLI;
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
import org.locationtech.spatial4j.distance.DistanceUtils;
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

	public Quantity<Angle> distanceToDegrees(Quantity<Length> distance) {
		return Quantities.getQuantity(distance.to(KILO(METER)).multiply(DistanceUtils.KM_TO_DEG).getValue(),
				DEGREE);
	}

	public Coordinate pointOnBearing(Coordinate initial, Quantity<Length> distance, Bearing bearing) {
		Quantity<Angle> degrees = distanceToDegrees(distance);
		return pointOnBearing(initial.toPoint(ctx), degrees, bearing);
	}

	public Coordinate pointOnBearing(Point point, Quantity<Angle> degrees, Bearing bearing) {
		return new Coordinate(ctx.getDistCalc().pointOnBearing(point, degrees.getValue().doubleValue(),
				bearing.to(DEGREE).getValue().doubleValue(), ctx, point));
	}

}