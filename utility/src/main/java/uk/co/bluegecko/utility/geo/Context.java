package uk.co.bluegecko.utility.geo;


import static tech.units.indriya.unit.Units.METRE;

import java.math.BigDecimal;
import java.math.MathContext;
import javax.measure.Quantity;
import javax.measure.quantity.Length;
import tech.units.indriya.quantity.Quantities;

public class Context {

	public Quantity<Length> equatorialRadius() {
		return Quantities.getQuantity(new BigDecimal("6378137.000000"), METRE);
	}

	public Quantity<Length> polarRadius() {
		return Quantities.getQuantity(new BigDecimal("6356752.314140"), METRE);
	}

	private MathContext mc() {
		return MathContext.DECIMAL64;
	}

	public Quantity<Length> getRadius() {
		return equatorialRadius();
	}

	public Quantity<Length> arithmeticMeanRadius() {
		return equatorialRadius().multiply(2).add(polarRadius()).divide(3);
	}

	public Quantity<Length> geocentricRadius(Latitude latitude) {
		BigDecimal cosA = BigDecimal.valueOf(Math.cos(latitude.radians().getValue().doubleValue()));
		BigDecimal sinB = BigDecimal.valueOf(Math.sin(latitude.radians().getValue().doubleValue()));

		BigDecimal equatorial = (BigDecimal) equatorialRadius().getValue();
		BigDecimal polar = (BigDecimal) polarRadius().getValue();

		return Quantities.getQuantity(
				equatorial.pow(2).multiply(cosA).pow(2).add(polar.pow(2).multiply(sinB).pow(2))
						.divide(equatorial.multiply(cosA).pow(2).add(polar.multiply(sinB).pow(2)),
								mc()).sqrt(mc()), METRE);
	}

}