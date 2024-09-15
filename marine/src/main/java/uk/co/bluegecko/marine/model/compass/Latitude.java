package uk.co.bluegecko.marine.model.compass;

import static systems.uom.ucum.UCUM.DEGREE;
import static uk.co.bluegecko.marine.model.compass.Limit.LATITUDE;

import java.util.Set;
import javax.measure.quantity.Angle;
import tech.units.indriya.ComparableQuantity;
import tech.units.indriya.quantity.Quantities;
import uk.co.bluegecko.marine.model.compass.Hemisphere.Spheriod;

public class Latitude extends Compass implements Spheriod {

	public Latitude(ComparableQuantity<Angle> angle) {
		super(angle, LATITUDE);
	}

	public Latitude(Number degrees) {
		this(Quantities.getQuantity(degrees, DEGREE));
	}

	@Override
	protected ComparableQuantity<Angle> wrap(ComparableQuantity<Angle> angle) {
		return new Latitude(angle);
	}

	@Override
	public Set<Hemisphere> hemispheres() {
		return getLimit().hemispheres();
	}

}