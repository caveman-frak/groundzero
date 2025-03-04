package uk.co.bluegecko.marine.model.position;

import static systems.uom.ucum.UCUM.DEGREE;
import static systems.uom.ucum.UCUM.MINUTE;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import java.awt.geom.Point2D;
import java.time.Instant;
import java.util.UUID;
import javax.measure.Quantity;
import javax.measure.Unit;
import javax.measure.quantity.Speed;
import lombok.Builder;
import lombok.Builder.Default;
import lombok.Value;
import lombok.With;
import lombok.extern.jackson.Jacksonized;
import org.apache.commons.lang3.builder.CompareToBuilder;
import si.uom.quantity.AngularSpeed;
import tech.units.indriya.unit.ProductUnit;
import uk.co.bluegecko.marine.model.compass.Bearing;
import uk.co.bluegecko.marine.model.compass.Coordinate;

@Value
@Builder(toBuilder = true)
@With
@Jacksonized
public class Trace implements Comparable<Trace> {

	public static final Unit<AngularSpeed> DEGREE_PER_MINUTE = new ProductUnit<>(DEGREE.divide(MINUTE));

	@NotNull
	UUID vesselId;
	@NotNull
	@PastOrPresent
	Instant timestamp;
	@NotNull
	@Default
	Coordinate coordinate = new Coordinate(0, 0);
	Bearing bearing;
	Quantity<Speed> speed;
	Quantity<AngularSpeed> rateOfTurn;

	public double latitude() {
		return coordinate.latitude().getValue().doubleValue();
	}

	public double longitude() {
		return coordinate.longitude().getValue().doubleValue();
	}

	public Point2D position() {
		return coordinate.toPoint();
	}

	@Override
	public int compareTo(Trace o) {
		return new CompareToBuilder()
				.append(timestamp, o.timestamp)
				.append(vesselId, o.vesselId)
				.build();
	}

	public static class TraceBuilder {

		public TraceBuilder coordinates(double latitude, double longitude) {
			return coordinate(new Coordinate(latitude, longitude));
		}

	}

}