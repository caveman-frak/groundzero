package uk.co.bluegecko.utility.geo;

import java.util.EnumSet;
import java.util.Set;
import javax.measure.quantity.Angle;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import tech.units.indriya.ComparableQuantity;

@RequiredArgsConstructor
@Getter
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public enum Compass {

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
	String name;

	public ComparableQuantity<Angle> decimal() {
		return bearing.decimal();
	}

	public static Set<Compass> cardinal() {
		return EnumSet.of(N, E, S, W);
	}

	public static Set<Compass> eightWinds() {
		return EnumSet.of(N, NE, E, SE, S, SW, W, NW);
	}

	public static Set<Compass> sixteenWinds() {
		return EnumSet.allOf(Compass.class);
	}

	public static Set<Compass> latitude() {
		return EnumSet.of(N, S);
	}

	public static Set<Compass> longitude() {
		return EnumSet.of(E, W);
	}


	public static Compass nearestPoint(ComparableQuantity<Angle> decimal, Set<Compass> points) {
		Compass before = null;
		Compass after = null;
		for (Compass compass : points) {
			if (compass.decimal().isLessThanOrEqualTo(decimal)) {
				before = compass;
			}
			if (compass.decimal().isGreaterThan(decimal)) {
				after = compass;
				break;
			}
		}
		if (before == null || after == null) {
			return null;
		} else if (decimal.subtract(before.decimal()).isLessThanOrEqualTo(after.decimal().subtract(decimal))) {
			return before;
		} else {
			return after;
		}
	}

	public static Compass nearestPoint(ComparableQuantity<Angle> degrees) {
		return nearestPoint(degrees, sixteenWinds());
	}

}