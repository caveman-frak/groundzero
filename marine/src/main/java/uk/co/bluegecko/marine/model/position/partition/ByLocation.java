package uk.co.bluegecko.marine.model.position.partition;

import com.uber.h3core.H3Core;
import java.util.concurrent.atomic.AtomicReference;
import org.locationtech.spatial4j.shape.Point;
import org.locationtech.spatial4j.shape.Shape;
import org.locationtech.spatial4j.shape.ShapeFactory;
import org.locationtech.spatial4j.shape.ShapeFactory.PolygonBuilder;
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
				.collect(() -> new ClosingBuilder(factory),
						ClosingBuilder::point,
						(_, _) -> {
						}).build();
	}

	record ClosingBuilder(PolygonBuilder builder, AtomicReference<Point> start) {

		ClosingBuilder(ShapeFactory factory) {
			this(factory.polygon(), new AtomicReference<>());
		}

		void point(Point point) {
			builder.pointLatLon(point.getLat(), point.getLon());
			start.compareAndSet(null, point);
		}

		Shape build() {
			Point point = start.get();
			builder.pointLatLon(point.getLat(), point.getLon());
			return builder.build();
		}

	}

}