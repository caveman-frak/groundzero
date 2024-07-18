package uk.co.bluegecko.utility.geo;

import static java.lang.Math.PI;
import static java.lang.Math.abs;
import static java.lang.Math.acos;
import static java.lang.Math.asin;
import static java.lang.Math.atan2;
import static java.lang.Math.cos;
import static java.lang.Math.pow;
import static java.lang.Math.sin;
import static java.lang.Math.sqrt;
import static tech.units.indriya.unit.Units.RADIAN;

import java.awt.geom.Point2D;
import javax.measure.Quantity;
import javax.measure.Unit;
import javax.measure.quantity.Angle;
import javax.measure.quantity.Length;
import tech.units.indriya.quantity.Quantities;

/**
 * Heavily influenced by the calculations described in
 * <a href="https://www.movable-type.co.uk/scripts/latlong.html">Calculate distance, bearing and more between
 * Latitude/Longitude points</a> by <a href="https://github.com/chrisveness/geodesy">Chris Veness</a>.
 */
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
	 * <p>Formula: <pre>
	 *     a = sin²(Δφ/2) + cos φ1 ⋅ cos φ2 ⋅ sin²(Δλ/2)
	 *     c = 2 ⋅ atan2( √a, √(1−a) )
	 *     d = R ⋅ c
	 * </pre></p>
	 * <p>Where:<pre>
	 *     φ is latitude, λ is longitude, R is earth’s radius (mean radius = 6,371km).
	 * </p></pre>
	 * <p>Note:<pre>
	 *     that angles need to be in radians to pass to trig functions!
	 * </pre></p>
	 */
	public double haversine(double startLat, double startLng, double endLat, double endLng) {
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
	 * <p>Formula: <pre>
	 *     d = acos( sin φ1 ⋅ sin φ2 + cos φ1 ⋅ cos φ2 ⋅ cos Δλ ) ⋅ R
	 * </pre></p>
	 * <p>Where:<pre>
	 *     φ is latitude, λ is longitude, Δλ is the difference in longitude, R is earth’s radius.
	 * </pre></p>
	 */
	public double sphericalLawOfCosines(double startLat, double startLng, double endLat, double endLng) {
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
	 * Calculate the final bearing in degrees on a great-circle course from one point to another.
	 * </p>
	 * <p>
	 * Remember, you can calculate the final bearing by swapping the start/end points and reversing the resulting
	 * "initial" bearing by adding or subtracting 180 degrees.
	 * </p>
	 *
	 * @param start the starting point.
	 * @param end   the ending point.
	 * @return the final bearing in degrees, normalized to the 0 to +360 range.
	 */
	public Bearing finalBearing(Coordinate start, Coordinate end) {
		double radians = bearing(
				end.getLatitude().radians().getValue().doubleValue(),
				end.getLongitude().radians().getValue().doubleValue(),
				start.getLatitude().radians().getValue().doubleValue(),
				start.getLongitude().radians().getValue().doubleValue()
		);
		return Bearing.fromAngle(Quantities.getQuantity(reverse(radians), RADIAN));
	}

	/**
	 * <p>
	 * Calculate the Forward Azimuth for initial bearing of great circle path (othodrone).
	 * </p>
	 * <p>Formula: <pre>
	 *     d = acos( sin φ1 ⋅ sin φ2 + cos φ1 ⋅ cos φ2 ⋅ cos Δλ ) ⋅ R
	 * </pre></p>
	 * <p>Where:<pre>
	 *     φ1,λ1 is the start point, φ2,λ2 the end point (Δλ is the difference in longitude).
	 * </pre></p>
	 */
	public double bearing(double startLat, double startLng, double endLat, double endLng) {
		double deltaLng = abs(endLng - startLng);
		return atan2(sin(deltaLng) * cos(endLat),
				cos(startLat) * sin(endLat) - sin(startLat) * cos(endLat) * cos(deltaLng));
	}

	/**
	 * <p>
	 * Calculate an intermediate point along a great circle path (othodrone).
	 * </p>
	 * <p>Formula: <pre>
	 *     a = sin((1−f)⋅δ) / sin δ
	 *     b = sin(f⋅δ) / sin δ
	 *     x = a ⋅ cos φ1 ⋅ cos λ1 + b ⋅ cos φ2 ⋅ cos λ2
	 *     y = a ⋅ cos φ1 ⋅ sin λ1 + b ⋅ cos φ2 ⋅ sin λ2
	 *     z = a ⋅ sin φ1 + b ⋅ sin φ2
	 *     φi = atan2(z, √x² + y²)
	 *     λi = atan2(y, x)
	 * </pre></p>
	 * <p>Where: <pre>
	 *     φ1,λ1 is the start point, φ2,λ2 the end point.
	 *     f is fraction along great circle route (f=0 is start point, f=1 is end point).
	 *     δ is the angular distance d/R between the two points.
	 * </pre></p>
	 */
	public Point2D intermediatePoint(double startLat, double startLng, double endLat, double endLng, double fraction) {
		double angle = haversine(startLat, startLng, endLat, endLng);
		double a = sin(1 - fraction) * angle;
		double b = sin(fraction - angle) / sin(angle);
		double x = a * cos(startLat) * cos(startLng) + b * cos(endLat) * cos(endLng);
		double y = a * cos(startLat) * sin(startLng) + b * cos(endLat) * sin(endLng);
		double z = a * sin(startLat) + b * sin(endLat);
		double lat = atan2(z, sqrt(pow(x, 2) + pow(y, 2)));
		double lng = atan2(y, x);
		return point(lat, lng);
	}

	/**
	 * <p>
	 * Calculate the end point of travel along a great-circle path from a given starting point with a given initial
	 * bearing for a known distance.
	 * </p>
	 *
	 * @param start          the starting point.
	 * @param initialBearing the initial bearing.
	 * @param distance       the distance to travel.
	 * @return the end point.
	 */
	public Coordinate travel(Coordinate start, Bearing initialBearing, Quantity<Length> distance) {
		Point2D point = travel(
				start.getLatitude().radians().getValue().doubleValue(),
				start.getLongitude().radians().getValue().doubleValue(),
				initialBearing.radians().getValue().doubleValue(),
				distance.divide(ctx.getRadius().to(distance.getUnit())).getValue().doubleValue()
		);
		return Coordinate.from(point);
	}

	/**
	 * <p>
	 * Calculate the destination point, given a start point, bearing and angular distance traveled along a great circle
	 * path.
	 * </p>
	 * <p>Formula:<pre>
	 *      φ2 = asin( sin φ1 ⋅ cos δ + cos φ1 ⋅ sin δ ⋅ cos θ )
	 *      λ2 = λ1 + atan2( sin θ ⋅ sin δ ⋅ cos φ1, cos δ − sin φ1 ⋅ sin φ2 )
	 * </pre></p>
	 * <p>Where:<pre>
	 *      φ is latitude, λ is longitude, θ is the bearing (clockwise from north).
	 *      δ is the angular distance d/R; d being the distance travelled, R the earth’s radius.
	 * </pre></p>
	 */
	public Point2D travel(double startLat, double startLng, double bearing, double distance) {
		double endLat = asin(sin(startLat) * cos(distance) + cos(startLat) * sin(distance) * cos(bearing));
		double endLng = startLng + atan2(sin(bearing) * sin(distance) * cos(startLat),
				cos(distance) - sin(startLat) * sin(endLat));
		return point(endLat, endLng);
	}

	/**
	 * <p>
	 * Calculate the intersection point of two converging paths, given start point 1 and initial bearing 1 and start
	 * point 2 and initial bearing 2.
	 * </p>
	 * <p>Formula:<pre>
	 *      δ12 = 2⋅asin( √(sin²(Δφ/2) + cos φ1 ⋅ cos φ2 ⋅ sin²(Δλ/2)) )
	 *      θa = acos( ( sin φ2 − sin φ1 ⋅ cos δ12 ) / ( sin δ12 ⋅ cos φ1 ) )
	 *      θb = acos( ( sin φ1 − sin φ2 ⋅ cos δ12 ) / ( sin δ12 ⋅ cos φ2 ) )
	 *
	 *      if sin(λ2−λ1) > 0
	 *          θ12 = θa
	 *          θ21 = 2π − θb
	 *      else
	 *          θ12 = 2π − θa
	 *          θ21 = θb
	 *      α1 = θ13 − θ12
	 *      α2 = θ21 − θ23
	 *
	 *      α3 = acos( −cos α1 ⋅ cos α2 + sin α1 ⋅ sin α2 ⋅ cos δ12 )
	 *      δ13 = atan2( sin δ12 ⋅ sin α1 ⋅ sin α2 , cos α2 + cos α1 ⋅ cos α3 )
	 *      φ3 = asin( sin φ1 ⋅ cos δ13 + cos φ1 ⋅ sin δ13 ⋅ cos θ13 )
	 *      Δλ13 = atan2( sin θ13 ⋅ sin δ13 ⋅ cos φ1 , cos δ13 − sin φ1 ⋅ sin φ3 )
	 *      λ3 = λ1 + Δλ13
	 * </pre></p>
	 * <p>Where:<pre>
	 *      φ1, λ1, θ13 : 1st start point & (initial) bearing from 1st point towards intersection point
	 *      φ2, λ2, θ23 : 2nd start point & (initial) bearing from 2nd point towards intersection point
	 *      φ3, λ3 : intersection point
	 * </pre></p>
	 * <p>Notes:<pre>
	 *      If sin α1 = 0 and sin α2 = 0: infinite solutions.
	 *      If sin α1 ⋅ sin α2 < 0: ambiguous solution.
	 *      This formulation is not always well-conditioned for meridional or equatorial lines.
	 * </pre></p>
	 */
	public Point2D intersection(double lat1, double lng1, double bearing1,
			double lat2, double lng2, double bearing2) {
		double a = sqrt(pow(sin(abs(lat2 - lat1) / 2), 2));
		double b = pow(sin(abs(lng2 - lng1) / 2), 2);
		double dist12 = 2 * asin(a + cos(lat1) * cos(lat2) * b);
//		δ12 = 2⋅asin( √(sin²(Δφ/2) + cos φ1 ⋅ cos φ2 ⋅ sin²(Δλ/2)) )
		double bearingA = acos((sin(lat2) - sin(lat1) * cos(dist12)) / (sin(dist12) * cos(lat1)));
//	 *      θa = acos( ( sin φ2 − sin φ1 ⋅ cos δ12 ) / ( sin δ12 ⋅ cos φ1 ) )
//	 *      θb = acos( ( sin φ1 − sin φ2 ⋅ cos δ12 ) / ( sin δ12 ⋅ cos φ2 ) )
		return null;
	}

	/**
	 * The radian equivalent of (degrees + 180°) mod 360°.
	 */
	public double reverse(double radians) {
		return (radians + PI) % (2 * PI);
	}

	/**
	 * Fix latitude between -90° (S) and +90° (N).
	 */
	public double normaliseLatitude(double radians) {
		return (radians + 3 * PI / 2) % (PI) - PI / 2;
	}

	/**
	 * Fix longitude between -180° (W) and +180° (E).
	 */
	public double normaliseLongitude(double radians) {
		return (radians + 3 * PI) % (2 * PI) - PI;
	}

	/**
	 * Fix bearing between 0° and +360°.
	 */
	public double normaliseBearing(double radians) {
		return abs(radians % (2 * PI));
	}

	/**
	 * Coordinates quote latitude (N/S) then longitude (E/W), cartesian points are in the reverse order of x, y.
	 */
	private Point2D point(double latitude, double longitude) {
		return new Point2D.Double(longitude, latitude);
	}

}