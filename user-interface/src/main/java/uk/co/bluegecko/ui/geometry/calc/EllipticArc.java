package uk.co.bluegecko.ui.geometry.calc;

import java.awt.geom.Point2D;
import java.util.List;

public class EllipticArc extends GeometryCalculator {

	public static void main(String[] args) {
		// Define ellipse parameters
		double centreX = 0; // Center x
		double centreY = 0; // Center y
		double radiusX = 5; // Semi-major axis
		double radiusY = 3; // Semi-minor axis
		double startAngleRad = 0; // Start angle in radians
		double angleExtentRad = Math.PI / 2; // Angle extent in radians (90 degrees)

		// Calculate points along the elliptic arc
		List<Point2D> points = calculatePointsOnEllipticArc(centreX, centreY, radiusX, radiusY, startAngleRad,
				angleExtentRad, 100);

		// Print points
		for (Point2D point : points) {
			System.out.println(point);
		}
	}

}