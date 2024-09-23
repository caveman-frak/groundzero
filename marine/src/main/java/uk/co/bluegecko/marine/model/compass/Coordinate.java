package uk.co.bluegecko.marine.model.compass;

import static systems.uom.ucum.UCUM.DEGREE;
import static systems.uom.ucum.UCUM.RADIAN;

import com.uber.h3core.util.LatLng;
import java.awt.geom.Point2D;
import java.awt.geom.Point2D.Double;
import org.locationtech.spatial4j.context.SpatialContext;
import org.locationtech.spatial4j.shape.Point;
import tech.units.indriya.quantity.Quantities;

public record Coordinate(Latitude latitude, Longitude longitude) {

	public Coordinate(double latitude, double longitude) {
		this(new Latitude(Quantities.getQuantity(latitude, DEGREE)),
				new Longitude(Quantities.getQuantity(longitude, DEGREE)));
	}

	public static Coordinate radians(double latitude, double longitude) {
		return new Coordinate(new Latitude(Quantities.getQuantity(latitude, RADIAN)),
				new Longitude(Quantities.getQuantity(longitude, RADIAN)));
	}

	public Coordinate(Point2D point) {
		this(point.getY(), point.getX());
	}

	public Coordinate(LatLng latLng) {
		this(latLng.lat, latLng.lng);
	}

	public Coordinate(Point point) {
		this(point.getLat(), point.getLon());
	}

	public Coordinate with(Latitude latitude) {
		return new Coordinate(latitude, longitude);
	}

	public Coordinate with(Longitude longitude) {
		return new Coordinate(latitude, longitude);
	}

	public Point2D toPoint() {
		return new Double(latitude.degrees(), longitude.degrees());
	}

	public LatLng toLatLng() {
		return new LatLng(latitude.degrees(), longitude.degrees());
	}

	public Point toPoint(SpatialContext ctx) {
		return ctx.getShapeFactory().pointLatLon(latitude.degrees(), longitude.degrees());
	}

}