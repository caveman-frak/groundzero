package uk.co.bluegecko.marine.model.compass;

import static systems.uom.ucum.UCUM.DEGREE;

import com.uber.h3core.util.LatLng;
import java.awt.geom.Point2D;
import java.awt.geom.Point2D.Double;
import tech.units.indriya.quantity.Quantities;

public record Coordinate(Latitude latitude, Longitude longitude) {

	public Coordinate(Point2D point) {
		this(new Latitude(Quantities.getQuantity(point.getY(), DEGREE)),
				new Longitude(Quantities.getQuantity(point.getX(), DEGREE)));
	}

	public Coordinate(LatLng latLng) {
		this(new Latitude(Quantities.getQuantity(latLng.lat, DEGREE)),
				new Longitude(Quantities.getQuantity(latLng.lng, DEGREE)));
	}

	public Point2D toPoint() {
		return new Double(latitude.to(DEGREE).getValue().doubleValue(), longitude.to(DEGREE).getValue().doubleValue());
	}

	public LatLng toLatLng() {
		return new LatLng(latitude.to(DEGREE).getValue().doubleValue(), longitude.to(DEGREE).getValue().doubleValue());
	}

}