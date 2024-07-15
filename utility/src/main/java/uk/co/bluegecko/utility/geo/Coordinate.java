package uk.co.bluegecko.utility.geo;

import static systems.uom.ucum.UCUM.RADIAN;

import java.awt.geom.Point2D;
import java.awt.geom.Point2D.Double;
import lombok.Builder;
import lombok.Value;
import tech.units.indriya.quantity.Quantities;

@Builder
@Value
public class Coordinate {

	Latitude latitude;
	Longitude longitude;

	/**
	 * Create a {@link Coordinate} from a {@link Point2D}. Remember to transpose Y to latitude and X to longitude.
	 *
	 * @param point the point.
	 * @return the coordinate.
	 */
	public static Coordinate from(Point2D point) {
		return Coordinate.builder()
				.latitude(Latitude.fromAngle(Quantities.getQuantity(point.getY(), RADIAN)))
				.longitude(Longitude.fromAngle(Quantities.getQuantity(point.getX(), RADIAN)))
				.build();
	}

	/**
	 * Create a point representing this coordinate. Remember to transpose longitude to X and latitude to Y.
	 *
	 * @return the point.
	 */
	public Point2D toPoint() {
		return new Double(
				longitude.decimal().getValue().doubleValue(),
				latitude.decimal().getValue().doubleValue());
	}

}