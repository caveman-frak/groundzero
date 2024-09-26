package uk.co.bluegecko.marine.model.compass;

import java.util.EnumSet;
import java.util.Set;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.Accessors;
import lombok.experimental.FieldDefaults;

@RequiredArgsConstructor
@Getter
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
@Accessors(fluent = true)
public enum Point {

	N(Bearing.asDegreeMinute(0, 0), "North"),
	NNE(Bearing.asDegreeMinute(22, 30), "North North East"),
	NE(Bearing.asDegreeMinute(45, 0), "North East"),
	ENE(Bearing.asDegreeMinute(67, 30), "East North East"),
	E(Bearing.asDegreeMinute(90, 0), "East"),
	ESE(Bearing.asDegreeMinute(112, 30), "East South East"),
	SE(Bearing.asDegreeMinute(135, 0), "South East"),
	SSE(Bearing.asDegreeMinute(157, 30), "South South East"),
	S(Bearing.asDegreeMinute(180, 0), "South"),
	SSW(Bearing.asDegreeMinute(202, 30), "South South West"),
	SW(Bearing.asDegreeMinute(225, 0), "South West"),
	WSW(Bearing.asDegreeMinute(247, 30), "West South West"),
	W(Bearing.asDegreeMinute(270, 0), "West"),
	WNW(Bearing.asDegreeMinute(292, 30), "West North West"),
	NW(Bearing.asDegreeMinute(315, 0), "North West"),
	NNW(Bearing.asDegreeMinute(337, 30), "North North West");

	Bearing bearing;
	String description;

	public static Set<Point> cardinal() {
		return EnumSet.of(N, E, S, W);
	}

	public static Set<Point> eightWinds() {
		return EnumSet.of(N, NE, E, SE, S, SW, W, NW);
	}

	public static Set<Point> sixteenWinds() {
		return EnumSet.allOf(Point.class);
	}

	public static Point nearest(Bearing bearing, Set<Point> points) {
		Point before = null;
		Point after = null;
		for (Point compass : points) {
			if (compass.bearing.isLessThanOrEqualTo(bearing)) {
				before = compass;
			}
			if (compass.bearing.isGreaterThan(bearing)) {
				after = compass;
				break;
			}
		}
		if (before == null || after == null) {
			return null;
		} else if (bearing.subtract(before.bearing).isLessThanOrEqualTo(after.bearing.subtract(bearing))) {
			return before;
		} else {
			return after;
		}
	}

	public static Point nearest(Bearing degrees) {
		return nearest(degrees, sixteenWinds());
	}

}