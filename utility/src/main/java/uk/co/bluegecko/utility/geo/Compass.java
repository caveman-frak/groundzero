package uk.co.bluegecko.utility.geo;

import java.util.EnumSet;
import java.util.Set;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

@RequiredArgsConstructor
@Getter
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public enum Compass {

	NORTH(new Bearing(0), "North"),
	NNE(new Bearing(22, 30), "North North East"),
	NE(new Bearing(45), "North East"),
	ENE(new Bearing(67, 30), "East North East"),
	EAST(new Bearing(90), "East"),
	ESE(new Bearing(112, 30), "East South East"),
	SE(new Bearing(135), "South East"),
	SSE(new Bearing(157, 30), "South South East"),
	SOUTH(new Bearing(180), "South"),
	SSW(new Bearing(202, 30), "South South West"),
	SW(new Bearing(225), "South West"),
	WSW(new Bearing(247, 30), "West South West"),
	WEST(new Bearing(270), "West"),
	WNW(new Bearing(292, 30), "West North West"),
	NW(new Bearing(315), "North West"),
	NNW(new Bearing(337, 30), "North North West");

	Bearing bearing;
	String name;

	public double decimal() {
		return bearing.toDecimal();
	}

	public static Set<Compass> cardinal() {
		return EnumSet.of(NORTH, EAST, SOUTH, WEST);
	}

	public static Set<Compass> eightWinds() {
		return EnumSet.of(NORTH, NE, EAST, SE, SOUTH, SW, WEST, NW);
	}

	public static Set<Compass> sixteenWinds() {
		return EnumSet.allOf(Compass.class);
	}

	public static Compass nearestPoint(double decimal, Set<Compass> points) {
		Compass before = null;
		Compass after = null;
		for (Compass compass : points) {
			if (compass.decimal() <= decimal) {
				before = compass;
			}
			if (compass.decimal() > decimal) {
				after = compass;
				break;
			}
		}
		if (before == null || after == null) {
			return null;
		} else if (decimal - before.decimal() <= after.decimal() - decimal) {
			return before;
		} else {
			return after;
		}
	}

	public static Compass nearestPoint(double degrees) {
		return nearestPoint(degrees, sixteenWinds());
	}

}