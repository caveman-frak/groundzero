package uk.co.bluegecko.ui.geometry.calc;

import java.awt.Point;
import java.awt.geom.Line2D;
import java.awt.geom.Line2D.Double;
import java.awt.geom.Point2D;
import java.util.function.DoubleUnaryOperator;
import java.util.stream.IntStream;
import java.util.stream.Stream;
import javafx.util.Pair;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.Accessors;
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

	public double calculateLengthOfCubicBezierCurve(Point2D start, Point2D control1, Point2D control2, Point2D end,
			Accuracy accuracy) {
		DoubleUnaryOperator integrand = fraction -> {
			double dx = 3 * Math.pow(1 - fraction, 2) * (control1.getX() - start.getX()) +
					6 * (1 - fraction) * fraction * (control2.getX() - control1.getX()) +
					3 * Math.pow(fraction, 2) * (end.getX() - control2.getX());

			double dy = 3 * Math.pow(1 - fraction, 2) * (control1.getY() - start.getY()) +
					6 * (1 - fraction) * fraction * (control2.getY() - control1.getY()) +
					3 * Math.pow(fraction, 2) * (end.getY() - control2.getY());

			return Math.sqrt(Math.pow(dx, 2) + Math.pow(dy, 2));
		};

		return gaussianQuadrature(integrand, 0, 1, accuracy);
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

	public Stream<Line2D> calculateTangentsAlongQuadraticBezierCurve(Point2D start, Point2D control, Point2D end,
			int numPoints) {
		return IntStream.range(0, numPoints).boxed().map(i -> (double) i / numPoints)
				.map(f -> line(calculatePointOnQuadraticBezierCurve(f, start, control, end),
						calculateTangentOnQuadraticBezierCurve(f, start, control, end)));
	}

	public double calculateLengthOfQuadraticBezierCurve(Point2D start, Point2D control, Point2D end,
			Accuracy accuracy) {
		DoubleUnaryOperator integrand = t -> {
			double dx = 2 * (1 - t) * (control.getX() - start.getX()) + 2 * t * (end.getX() - control.getX());
			double dy = 2 * (1 - t) * (control.getY() - start.getY()) + 2 * t * (end.getY() - control.getY());
			return Math.sqrt(Math.pow(dx, 2) + Math.pow(dy, 2));
		};
		return gaussianQuadrature(integrand, 0, 1, accuracy);
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

	private Point2D calculateTangentOnQuadraticBezierCurve(double fraction, Point2D start, Point2D control,
			Point2D end) {
		return new Point2D.Double(
				2 * (1 - fraction) * (control.getX() - start.getX()) + 2 * fraction * (end.getX() - control.getX()),
				2 * (1 - fraction) * (control.getY() - start.getY()) + 2 * fraction * (end.getY() - control.getY()));
	}

	public Stream<Point2D> calculatePointsAlongEllipticArc(Point2D centre, Point2D radius, double angleStart,
			double angleExtent, int numPoints) {
		return calculatePointsAlongEllipticArc(centre.getX(), centre.getY(), radius.getX(), radius.getY(),
				Math.toRadians(-angleStart), Math.toRadians(angleExtent), numPoints);
	}

	public Stream<Line2D> calculateTangentsAlongEllipticArc(Point2D centre, Point2D radius, double angleStart,
			double angleExtent, int numPoints) {
		return calculateTangentsAlongEllipticArc(centre.getX(), centre.getY(), radius.getX(), radius.getY(),
				Math.toRadians(-angleStart), Math.toRadians(angleExtent), numPoints);
	}

	public double calculateLengthOfEllipticArc(Point2D radius, double angleStart,
			double angleExtent, Accuracy accuracy) {
		return calculateLengthOfEllipticArc(radius.getX(), radius.getY(),
				Math.toRadians(angleStart), Math.toRadians(angleExtent), accuracy);
	}

	private Stream<Point2D> calculatePointsAlongEllipticArc(double centreX, double centreY, double radiusX,
			double radiusY, double thetaStart, double thetaExtent, int numPoints) {
		return IntStream.range(0, numPoints).boxed().map(i -> thetaStart - i * thetaExtent / numPoints)
				.map(f -> calculatePointOnEllipticArc(f, centreX, centreY, radiusX, radiusY));
	}

	private Stream<Line2D> calculateTangentsAlongEllipticArc(double centreX, double centreY, double radiusX,
			double radiusY, double thetaStart, double thetaExtent, int numPoints) {
		return IntStream.range(0, numPoints).boxed().map(i -> thetaStart - i * thetaExtent / numPoints)
				.map(f -> line(calculatePointOnEllipticArc(f, centreX, centreY, radiusX, radiusY),
						calculateTangentOnEllipticArc(f, centreX, centreY, radiusX, radiusY)));
	}

	private Point2D calculatePointOnEllipticArc(double theta, double centreX, double centreY, double radiusX,
			double radiusY) {
		return new Point2D.Double(
				centreX + radiusX * Math.cos(theta),
				centreY + radiusY * Math.sin(theta));
	}

	private Point2D calculateTangentOnEllipticArc(double theta, double centreX, double centreY, double radiusX,
			double radiusY) {
		return new Point2D.Double(
				centreX - radiusX * Math.sin(theta),
				centreY + radiusY * Math.cos(theta));
	}

	private double calculateLengthOfEllipticArc(double radiusX, double radiusY, double thetaStart,
			double thetaExtent, Accuracy accuracy) {
		DoubleUnaryOperator integrand = fraction -> Math.sqrt(
				Math.pow(radiusX, 2) * Math.pow(Math.sin(fraction), 2)
						+ Math.pow(radiusY, 2) * Math.pow(Math.cos(fraction), 2));
		return gaussianQuadrature(integrand, thetaStart, thetaStart + thetaExtent, accuracy);
	}

	public Stream<Point2D> calculatePointsAlongLine(Point2D start, Point2D end, int numPoints) {
		return IntStream.range(0, numPoints).boxed().map(i -> (double) i / numPoints)
				.map(f -> calculatePointOnLine(f, start, end));

	}

	public double calculateLengthOfLine(Point start, Point end) {
		return Math.sqrt(Math.pow(end.getX() - start.getX(), 2) + Math.pow(end.getY() - start.getY(), 2));
	}

	private Point2D calculatePointOnLine(double fraction, Point2D start, Point2D end) {
		return new Point2D.Double(
				start.getX() + fraction * (end.getX() - start.getX()),
				start.getY() + fraction * (end.getY() - start.getY()));
	}

	protected double gaussianQuadrature(DoubleUnaryOperator integrand, double thetaStart, double thetaEnd,
			Accuracy accuracy) {
		// Nodes (key) and Weights (value) for n-point Gaussian quadrature
		Pair<double[], double[]> node = switch (accuracy) {
			case LOW -> new Pair<>(
					new double[]{-0.5773502691896257, 0.5773502691896257},
					new double[]{1.0, 1.0});
			case MEDIUM -> new Pair<>(
					new double[]{-0.7745966692414834, 0.0, 0.7745966692414834},
					new double[]{0.5555555555555556, 0.8888888888888888, 0.5555555555555556});
			case HIGH -> new Pair<>(
					new double[]{-0.8611363115940526, -0.3399810435848563, 0.3399810435848563, 0.8611363115940526},
					new double[]{0.3478548451374538, 0.6521451548625461, 0.6521451548625461, 0.3478548451374538});
			case MAX -> new Pair<>(
					new double[]{-0.9061798459386640, -0.5384693101056831, 0.0, 0.5384693101056831, 0.9061798459386640},
					new double[]{0.2369268850561891, 0.4786286704993665, 0.5688888888888889, 0.4786286704993665,
							0.2369268850561891});
		};

		// Change of interval
		double deltaM = 0.5 * (thetaEnd + thetaStart);
		double deltaR = 0.5 * (thetaEnd - thetaStart);

		double integral = 0.0;
		for (int i = 0; i < accuracy.iterations(); i++) {
			double deltaX = deltaR * node.getKey()[i];
			integral += node.getValue()[i] * integrand.applyAsDouble(deltaM + deltaX);
		}
		integral *= deltaR;

		return integral;
	}

	private Line2D line(Point2D start, Point2D end) {
		return new Double(start.getX(), start.getY(), end.getX(), end.getY());
	}

	@RequiredArgsConstructor
	@Getter
	@Accessors(fluent = true)
	public enum Accuracy {
		LOW(2),
		MEDIUM(3),
		HIGH(4),
		MAX(5);

		private final int iterations;
	}

}