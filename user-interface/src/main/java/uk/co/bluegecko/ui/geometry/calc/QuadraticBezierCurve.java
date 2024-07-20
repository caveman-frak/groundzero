package uk.co.bluegecko.ui.geometry.calc;

import java.awt.geom.Point2D;
import java.util.List;

public class QuadraticBezierCurve extends GeometryCalculator {

	public static void main(String[] args) {
		// Define control points
		Point2D start = new Point2D.Double(0, 0);
		Point2D control = new Point2D.Double(2, 4);
		Point2D end = new Point2D.Double(4, 0);

		// Calculate points along the curve
		List<Point2D> points = calculatePointsOnQuadraticBezierCurve(start, control, end, 100);

		// Print points
		for (Point2D point : points) {
			System.out.println(point);
		}
	}

}