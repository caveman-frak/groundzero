package uk.co.bluegecko.ui.geometry.calc;

import java.awt.geom.Line2D;
import java.awt.geom.Line2D.Double;
import java.awt.geom.Point2D;
import java.util.stream.IntStream;
import java.util.stream.Stream;
import org.springframework.stereotype.Service;

@Service
public class GeometryCalculator {

	public Stream<Point2D> calculatePointsAlongCubicBezierCurve(Point2D start, Point2D control1,
			Point2D control2, Point2D end, int numPoints) {
		return IntStream.range(0, numPoints).boxed().map(i -> (double) i / numPoints)
				.map(f -> calculatePointOnCubicBezierCurve(f, start, control1, control2, end));
	}

	public Stream<Line2D> calculateTangentsAlongCubicBezierCurve(Point2D start, Point2D control1,
			Point2D control2, Point2D end, int numPoints) {
		return IntStream.range(0, numPoints).boxed().map(i -> (double) i / numPoints)
				.map(f -> line(calculatePointOnCubicBezierCurve(f, start, control1, control2, end),
						calculateTangentOnCubicBezierCurve(f, start, control1, control2, end)));
	}

	private Point2D calculatePointOnCubicBezierCurve(double fraction, Point2D start, Point2D control1,
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

	private Point2D calculateTangentOnCubicBezierCurve(double fraction, Point2D start, Point2D control1, Point2D P2,
			Point2D end) {
		return new Point2D.Double(
				3 * Math.pow(1 - fraction, 2) * (control1.getX() - start.getX()) +
						6 * (1 - fraction) * fraction * (P2.getX() - control1.getX()) +
						3 * Math.pow(fraction, 2) * (end.getX() - P2.getX()),
				3 * Math.pow(1 - fraction, 2) * (control1.getY() - start.getY()) +
						6 * (1 - fraction) * fraction * (P2.getY() - control1.getY()) +
						3 * Math.pow(fraction, 2) * (end.getY() - P2.getY()));
	}

	public Stream<Point2D> calculatePointsAlongQuadraticBezierCurve(Point2D start, Point2D control, Point2D end,
			int numPoints) {
		return IntStream.range(0, numPoints).boxed().map(i -> (double) i / numPoints)
				.map(f -> calculatePointOnQuadraticBezierCurve(f, start, control, end));
	}


	private Point2D calculatePointOnQuadraticBezierCurve(double fraction, Point2D start, Point2D control,
			Point2D end) {
		return new Point2D.Double(
				Math.pow(1 - fraction, 2) * start.getX() +
						2 * (1 - fraction) * fraction * control.getX() +
						Math.pow(fraction, 2) * end.getX(),
				Math.pow(1 - fraction, 2) * start.getY() +
						2 * (1 - fraction) * fraction * control.getY() +
						Math.pow(fraction, 2) * end.getY());
	}

	public Stream<Point2D> calculatePointsAlongEllipticArc(Point2D centre, Point2D radius, double startAngleDeg,
			double angleExtentDeg, int numPoints) {
		return calculatePointsAlongEllipticArc(centre.getX(), centre.getY(), radius.getX(), radius.getY(),
				Math.toRadians(-startAngleDeg), Math.toRadians(angleExtentDeg), numPoints);
	}

	private Stream<Point2D> calculatePointsAlongEllipticArc(double centreX, double centreY, double radiusX,
			double radiusY, double startAngleRad, double angleExtentRad, int numPoints) {
		return IntStream.range(0, numPoints).boxed().map(i -> startAngleRad - i * angleExtentRad / numPoints)
				.map(f -> calculatePointOnEllipticArc(f, centreX, centreY, radiusX, radiusY));
	}

	private Point2D calculatePointOnEllipticArc(double fraction, double centreX, double centreY, double radiusX,
			double radiusY) {
		return new Point2D.Double(
				centreX + radiusX * Math.cos(fraction),
				centreY + radiusY * Math.sin(fraction));
	}

	public Stream<Point2D> calculatePointsAlongLine(Point2D start, Point2D end, int numPoints) {
		return IntStream.range(0, numPoints).boxed().map(i -> (double) i / numPoints)
				.map(f -> calculatePointOnLine(f, start, end));

	}

	private Point2D calculatePointOnLine(double fraction, Point2D start, Point2D end) {
		return new Point2D.Double(
				start.getX() + fraction * (end.getX() - start.getX()),
				start.getY() + fraction * (end.getY() - start.getY()));
	}

	private Line2D line(Point2D start, Point2D end) {
		return new Double(start.getX(), start.getY(), end.getX(), end.getY());
	}

}