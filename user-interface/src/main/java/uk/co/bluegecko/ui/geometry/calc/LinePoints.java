package uk.co.bluegecko.ui.geometry.calc;

import java.awt.geom.Point2D;
import java.util.List;

public class LinePoints extends GeometryCalculator {

	public static void main(String[] args) {
		// Define the start and end points
		Point2D start = new Point2D.Double(0, 0);
		Point2D end = new Point2D.Double(10, 10);

		// Calculate points along the line
		List<Point2D> points = calculatePointsOnLine(start, end, 100);

		// Print points
		for (Point2D point : points) {
			System.out.println(point);
		}
	}

}