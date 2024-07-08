package uk.co.bluegecko.utility.geo;

public interface Angle {

	double decimal();

	default double radians() {
		return radians(decimal());
	}

	static double radians(double degrees) {
		return Math.toRadians(degrees);
	}

	static double degrees(double radians) {
		return Math.toDegrees(radians);
	}

}