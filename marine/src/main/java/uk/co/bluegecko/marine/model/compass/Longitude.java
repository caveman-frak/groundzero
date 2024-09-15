package uk.co.bluegecko.marine.model.compass;

import static systems.uom.ucum.UCUM.DEGREE;
import static uk.co.bluegecko.marine.model.compass.Limit.LONGITUDE;

import java.util.Set;
import javax.measure.quantity.Angle;
import tech.units.indriya.ComparableQuantity;
import tech.units.indriya.quantity.Quantities;
import uk.co.bluegecko.marine.model.compass.Hemisphere.Spheriod;

public class Longitude extends Compass implements Spheriod {

	public Longitude(ComparableQuantity<Angle> angle) {
		super(angle, LONGITUDE);
	}

	public Longitude(Number degrees) {
		this(Quantities.getQuantity(degrees, DEGREE));
	}

	@Override
	protected ComparableQuantity<Angle> wrap(ComparableQuantity<Angle> angle) {
		return new Longitude(angle);
	}

	@Override
	public Set<Hemisphere> hemispheres() {
		return getLimit().hemispheres();
	}

}