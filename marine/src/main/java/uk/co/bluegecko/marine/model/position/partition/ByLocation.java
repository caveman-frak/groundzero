package uk.co.bluegecko.marine.model.position.partition;

import com.uber.h3core.H3Core;
import org.locationtech.spatial4j.shape.Shape;
import org.locationtech.spatial4j.shape.ShapeFactory;
import org.locationtech.spatial4j.shape.SpatialRelation;
import uk.co.bluegecko.marine.model.compass.Coordinate;

public interface ByLocation extends ByResolution {

	long cell();

	default boolean contains(H3Core core, ShapeFactory factory, Coordinate coordinate) {
		return polygon(core, factory).relate(coordinate.toPoint(factory)) == SpatialRelation.CONTAINS;
	}

	default Shape polygon(H3Core core, ShapeFactory factory) {
		return core.cellToBoundary(cell()).stream()
				.map(p -> factory.pointLatLon(p.lat, p.lng))
				.collect(factory::polygon,
						(s, p) -> s.pointLatLon(p.getLat(), p.getLon()),
						(_, _) -> {
						}).build();
	}

}