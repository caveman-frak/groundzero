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
				.collect(() -> new FactoryCloser(factory),
						FactoryCloser::point,
						(_, _) -> {
						}).build();
	}

	record FactoryCloser(PolygonBuilder builder, AtomicReference<Point> start) {

		FactoryCloser(ShapeFactory factory) {
			this(factory.polygon(), new AtomicReference<>());
		}

		FactoryCloser point(Point point) {
			builder.pointLatLon(point.getLat(), point.getLon());
			start.compareAndSet(null, point);

			return this;
		}

		Shape build() {
			Point point = start.get();
			builder.pointLatLon(point.getLat(), point.getLon());
			return builder.build();
		}

	}

}