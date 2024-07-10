package uk.co.bluegecko.utility.geo;

import java.math.BigDecimal;
import javax.measure.Quantity;
import javax.measure.Unit;
import javax.measure.quantity.Length;
import lombok.Value;

@Value
public class Calculator {

	/**
	 * Distance between two points.
	 *
	 * @param start the starting position.
	 * @param end   the ending position.
	 * @param unit  the unit of measure in which to receive the result.
	 * @return the distance in the chosen unit of measure.
	 */
	public Quantity<Length> distance(Coordinate start, Coordinate end, Unit<Length> unit) {
		return null;
	}

	/**
	 * <p>
	 * This "distance" function is mostly for internal use. Most users will simply rely upon
	 * {@link #distance(Coordinate, Coordinate, Unit)}
	 * </p>
	 * <p>
	 * Yields the internal angle for an arc between two points on the surface of a sphere in radians. This angle is in
	 * the plane of the great circle connecting the two points measured from an axis through one of the points and the
	 * center of the Earth. Multiply this value by the sphere's radius to get the length of the arc.
	 * </p>
	 *
	 * @param start the first point.
	 * @param end   the second point.
	 * @return the internal angle for the arc connecting the two points in radians.
	 */
	public BigDecimal distanceInRadians(Coordinate start, Coordinate end) {
		return null;
	}

	/**
	 * <p>
	 * Calculate the initial bearing in degrees on a great-circle course from one point to another.
	 * </p>
	 * <p>
	 * Remember, you can calculate the final bearing by swapping the start/end points and reversing the resulting
	 * "initial" bearing by adding or subtracting 180 degrees.
	 * </p>
	 *
	 * @param start the starting point.
	 * @param end   the ending point.
	 * @return the initial bearing in degrees, normalized to the 0 to +360 range.
	 */
	public Bearing initialBearing(Coordinate start, Coordinate end) {
		return null;
	}

	/**
	 * <p>
	 * Calculate the end point of traveling along a great-circle path from a given starting point with a given intitial
	 * bearing for a known distance.
	 * </p>
	 *
	 * @param start          the starting point.
	 * @param initialBearing the initial bearing.
	 * @param distance       the distance to travel.
	 * @return the end point.
	 */
	public static Coordinate travel(Coordinate start, Bearing initialBearing, Quantity<Length> distance) {
		return null;
	}

}