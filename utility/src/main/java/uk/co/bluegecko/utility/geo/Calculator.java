package uk.co.bluegecko.utility.geo;

import static java.lang.Math.abs;
import static java.lang.Math.acos;
import static java.lang.Math.atan2;
import static java.lang.Math.cos;
import static java.lang.Math.pow;
import static java.lang.Math.sin;
import static java.lang.Math.sqrt;
import static tech.units.indriya.unit.Units.RADIAN;

import javax.measure.Quantity;
import javax.measure.Unit;
import javax.measure.quantity.Angle;
import javax.measure.quantity.Length;
import tech.units.indriya.quantity.Quantities;

public record Calculator(Context ctx) {

	/**
	 * Distance between two points.
	 *
	 * @param start the starting position.
	 * @param end   the ending position.
	 * @param unit  the unit of measure in which to receive the result.
	 * @return the distance in the chosen unit of measure.
	 */
	public Quantity<Length> distance(Coordinate start, Coordinate end, Unit<Length> unit) {
		return distance(start, end).multiply(ctx.getRadius()).asType(Length.class).to(unit);
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
	public Quantity<Angle> distance(Coordinate start, Coordinate end) {
		return Quantities.getQuantity(
				haversine(
						start.getLatitude().radians().getValue().doubleValue(),
						start.getLongitude().radians().getValue().doubleValue(),
						end.getLatitude().radians().getValue().doubleValue(),
						end.getLongitude().radians().getValue().doubleValue()
				), RADIAN);
	}

	/**
	 * <p>
	 * Haversine formula for calculating Great Circle Path (orthodrome) distances.
	 * </p>
	 * <p>
	 * Formula: <pre>
	 *     a = sin²(Δφ/2) + cos φ1 ⋅ cos φ2 ⋅ sin²(Δλ/2)
	 *     c = 2 ⋅ atan2( √a, √(1−a) )
	 *     d = R ⋅ c
	 * </pre>
	 * </p>
	 * <p>
	 * Where φ is latitude, λ is longitude, R is earth’s radius (mean radius = 6,371km).
	 * </p>
	 * Note that angles need to be in radians to pass to trig functions!
	 */
	double haversine(double startLat, double startLng, double endLat, double endLng) {
		double halfDeltaLat = abs(endLat - startLat) / 2;
		double halfDeltaLng = abs(endLng - startLng) / 2;
		double a = pow(sin(halfDeltaLat), 2)
				+ cos(startLat) * cos(endLat) * sin(halfDeltaLng) * sin(halfDeltaLng);
		return 2 * atan2(sqrt(a), sqrt(1 - a));
	}

	/**
	 * <p>
	 * Spherical law of Cosines.
	 * </p>
	 * <p>
	 * Formula: <pre>
	 *     d = acos( sin φ1 ⋅ sin φ2 + cos φ1 ⋅ cos φ2 ⋅ cos Δλ ) ⋅ R
	 * </pre>
	 * </p>
	 * <p>
	 * Where φ is latitude, λ is longitude, Δλ is the difference in longitude, R is earth’s radius.
	 * </p>
	 */
	double sphericalLawOfCosines(double startLat, double startLng, double endLat, double endLng) {
		double deltaLng = abs(endLng - startLng);
		return acos(sin(startLat) * sin(endLat) + cos(startLat) * cos(endLat) * cos(deltaLng));
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
		return Bearing.fromAngle(
				Quantities.getQuantity(
						bearing(
								start.getLatitude().radians().getValue().doubleValue(),
								start.getLongitude().radians().getValue().doubleValue(),
								end.getLatitude().radians().getValue().doubleValue(),
								end.getLongitude().radians().getValue().doubleValue()
						), RADIAN));
	}

	/**
	 * <p>
	 * Calculate the Forward Azimuth for initial bearing of great circle path (othodrone).
	 * </p>
	 * <p>
	 * Formula: <pre>
	 *     d = acos( sin φ1 ⋅ sin φ2 + cos φ1 ⋅ cos φ2 ⋅ cos Δλ ) ⋅ R
	 * </pre>
	 * </p>
	 * <p>
	 * Where φ1,λ1 is the start point, φ2,λ2 the end point (Δλ is the difference in longitude).
	 * </p>
	 */
	double bearing(double startLat, double startLng, double endLat, double endLng) {
		double deltaLng = abs(endLng - startLng);
		return atan2(sin(deltaLng) * cos(endLat),
				cos(startLat) * sin(endLat) - sin(startLat) * cos(endLat) * cos(deltaLng));
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