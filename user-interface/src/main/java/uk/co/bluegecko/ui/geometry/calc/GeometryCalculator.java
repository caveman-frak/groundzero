package uk.co.bluegecko.ui.geometry.calc;

import java.awt.geom.Point2D;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class GeometryCalculator {

	public static Stream<Point2D> calculatePointsAlongCubicBezierCurve(Point2D start, Point2D control1,
			Point2D control2, Point2D end, int numPoints) {
		return IntStream.range(0, numPoints).boxed().map(i -> (double) i / numPoints)
				.map(f -> calculatePointOnCubicBezierCurve(f, start, control1, control2, end));
	}

	private static Point2D calculatePointOnCubicBezierCurve(double fraction, Point2D start, Point2D control1,
			Point2D control2, Point2D end) {
		return new Point2D.Double(
				Math.pow(1 - fraction, 3) * start.getX() +
						3 * Math.pow(1 - fraction, 2) * fraction * control1.getX() +
						3 * (1 - fraction) * Math.pow(fraction, 2) * control2.getX() +
						Math.pow(fraction, 3) * end.getX(),
				Math.pow(1 - fraction, 3) * start.getY() +
						3 * Math.pow(1 - fraction, 2) * fraction * control1.getY() +
						3 * (1 - fraction) * Math.pow(fraction, 2) * control2.getY() +
						Math.pow(fraction, 3) * end.getY());
	}

	public static Stream<Point2D> calculatePointsAlongQuadraticBezierCurve(Point2D start, Point2D control, Point2D end,
			int numPoints) {
		return IntStream.range(0, numPoints).boxed().map(i -> (double) i / numPoints)
				.map(f -> calculatePointOnQuadraticBezierCurve(f, start, control, end));
	}


	private static Point2D calculatePointOnQuadraticBezierCurve(double fraction, Point2D start, Point2D control,
			Point2D end) {
		return new Point2D.Double(
				Math.pow(1 - fraction, 2) * start.getX() +
						2 * (1 - fraction) * fraction * control.getX() +
						Math.pow(fraction, 2) * end.getX(),
				Math.pow(1 - fraction, 2) * start.getY() +
						2 * (1 - fraction) * fraction * control.getY() +
						Math.pow(fraction, 2) * end.getY());
	}

	public static Stream<Point2D> calculatePointsAlongEllipticArc(Point2D centre, Point2D radius, double startAngleDeg,
			double angleExtentDeg, int numPoints) {
		return calculatePointsAlongEllipticArc(centre.getX(), centre.getY(), radius.getX(), radius.getY(),
				Math.toRadians(-startAngleDeg), Math.toRadians(angleExtentDeg), numPoints);
	}

	private static Stream<Point2D> calculatePointsAlongEllipticArc(double centreX, double centreY, double radiusX,
			double radiusY, double startAngleRad, double angleExtentRad, int numPoints) {
		return IntStream.range(0, numPoints).boxed().map(i -> startAngleRad - i * angleExtentRad / numPoints)
				.map(f -> calculatePointOnEllipticArc(f, centreX, centreY, radiusX, radiusY));
	}

	private static Point2D calculatePointOnEllipticArc(double fraction, double centreX, double centreY, double radiusX,
			double radiusY) {
		return new Point2D.Double(
				centreX + radiusX * Math.cos(fraction),
				centreY + radiusY * Math.sin(fraction));
	}

	public static Stream<Point2D> calculatePointsAlongLine(Point2D start, Point2D end, int numPoints) {
		return IntStream.range(0, numPoints).boxed().map(i -> (double) i / numPoints)
				.map(f -> calculatePointOnLine(f, start, end));

	}

	private static Point2D calculatePointOnLine(double fraction, Point2D start, Point2D end) {
		return new Point2D.Double(
				start.getX() + fraction * (end.getX() - start.getX()),
				start.getY() + fraction * (end.getY() - start.getY()));
	}
}