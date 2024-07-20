package uk.co.bluegecko.ui.geometry.calc;

import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.List;

public class GeometryCalculator {

	public static List<Point2D> calculatePointsOnCubicBezierCurve(Point2D start, Point2D control1, Point2D control2,
			Point2D end, int numPoints) {
		List<Point2D> points = new ArrayList<>();

		for (int i = 0; i <= numPoints; i++) {
			double fraction = (double) i / numPoints;
			points.add(calculatePointOnCubicBezierCurve(fraction, start, control1, control2, end));
		}

		return points;
	}

	public static Point2D calculatePointOnCubicBezierCurve(double fraction, Point2D start, Point2D control1,
			Point2D control2,
			Point2D end) {
		double x = Math.pow(1 - fraction, 3) * start.getX() +
				3 * Math.pow(1 - fraction, 2) * fraction * control1.getX() +
				3 * (1 - fraction) * Math.pow(fraction, 2) * control2.getX() +
				Math.pow(fraction, 3) * end.getX();

		double y = Math.pow(1 - fraction, 3) * start.getY() +
				3 * Math.pow(1 - fraction, 2) * fraction * control1.getY() +
				3 * (1 - fraction) * Math.pow(fraction, 2) * control2.getY() +
				Math.pow(fraction, 3) * end.getY();

		return new Point2D.Double(x, y);
	}

	public static List<Point2D> calculatePointsOnQuadraticBezierCurve(Point2D start, Point2D control, Point2D end,
			int numPoints) {
		List<Point2D> points = new ArrayList<>();

		for (int i = 0; i <= numPoints; i++) {
			double fraction = (double) i / numPoints;
			points.add(calculatePointOnQuadraticBezierCurve(fraction, start, control, end));
		}

		return points;
	}

	public static Point2D calculatePointOnQuadraticBezierCurve(double fraction, Point2D start, Point2D control,
			Point2D end) {
		double x = Math.pow(1 - fraction, 2) * start.getX() +
				2 * (1 - fraction) * fraction * control.getX() +
				Math.pow(fraction, 2) * end.getX();

		double y = Math.pow(1 - fraction, 2) * start.getY() +
				2 * (1 - fraction) * fraction * control.getY() +
				Math.pow(fraction, 2) * end.getY();

		return new Point2D.Double(x, y);
	}

	public static List<Point2D> calculatePointsOnEllipticArc(Point2D centre, Point2D radius, double startAngleDeg,
			double angleExtentDeg, int numPoints) {
		return calculatePointsOnEllipticArc(centre.getX(), centre.getY(), radius.getX(), radius.getY(),
				Math.toRadians(-startAngleDeg), Math.toRadians(angleExtentDeg), numPoints);
	}

	public static List<Point2D> calculatePointsOnEllipticArc(double centreX, double centreY, double radiusX,
			double radiusY, double startAngleRad, double angleExtentRad, int numPoints) {
		List<Point2D> points = new ArrayList<>();

		for (int i = 0; i <= numPoints; i++) {
			double fraction = startAngleRad - i * angleExtentRad / numPoints;
			points.add(calculatePointOnEllipticArc(centreX, centreY, radiusX, radiusY, fraction));
		}

		return points;
	}

	public static Point2D calculatePointOnEllipticArc(double centreX, double centreY, double radiusX, double radiusY,
			double fraction) {
		double x = centreX + radiusX * Math.cos(fraction);
		double y = centreY + radiusY * Math.sin(fraction);

		return new Point2D.Double(x, y);
	}

	public static List<Point2D> calculatePointsOnLine(Point2D start, Point2D end, int numPoints) {
		List<Point2D> points = new ArrayList<>();

		for (int i = 0; i <= numPoints; i++) {
			double fraction = (double) i / numPoints;
			points.add(calculatePointOnLine(start, end, fraction));
		}

		return points;
	}

	public static Point2D calculatePointOnLine(Point2D start, Point2D end, double fraction) {
		double x = start.getX() + fraction * (end.getX() - start.getX());
		double y = start.getY() + fraction * (end.getY() - start.getY());
		return new Point2D.Double(x, y);
	}
}