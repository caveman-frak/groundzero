package uk.co.bluegecko.marine.model.compass;

import java.util.EnumSet;
import java.util.Set;
import javax.measure.quantity.Angle;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.Accessors;
import lombok.experimental.FieldDefaults;
import tech.units.indriya.ComparableQuantity;

@RequiredArgsConstructor
@Getter
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
@Accessors(fluent = true)
public enum Point {

	N(new Bearing(0), "North"),
	NNE(new Bearing(22, 30), "North North East"),
	NE(new Bearing(45), "North East"),
	ENE(new Bearing(67, 30), "East North East"),
	E(new Bearing(90), "East"),
	ESE(new Bearing(112, 30), "East South East"),
	SE(new Bearing(135), "South East"),
	SSE(new Bearing(157, 30), "South South East"),
	S(new Bearing(180), "South"),
	SSW(new Bearing(202, 30), "South South West"),
	SW(new Bearing(225), "South West"),
	WSW(new Bearing(247, 30), "West South West"),
	W(new Bearing(270), "West"),
	WNW(new Bearing(292, 30), "West North West"),
	NW(new Bearing(315), "North West"),
	NNW(new Bearing(337, 30), "North North West");

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

	public static Set<Point> latitude() {
		return EnumSet.of(N, S);
	}

	public static Set<Point> longitude() {
		return EnumSet.of(E, W);
	}

	public static Point nearest(ComparableQuantity<Angle> bearing, Set<Point> points) {
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

	public static Point nearest(ComparableQuantity<Angle> degrees) {
		return nearest(degrees, sixteenWinds());
	}

}