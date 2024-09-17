package uk.co.bluegecko.marine.model.travel;

import com.uber.h3core.util.LatLng;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import java.awt.geom.Point2D;
import java.time.Instant;
import java.util.UUID;
import lombok.Builder;
import lombok.Builder.Default;
import lombok.Value;
import lombok.With;
import lombok.extern.jackson.Jacksonized;
import org.hibernate.validator.constraints.Range;
import uk.co.bluegecko.marine.model.compass.Coordinate;

@Value
@Builder(toBuilder = true)
@With
@Jacksonized
public class Trace {

	@NotNull
	UUID vesselId;
	@PastOrPresent
	Instant timestamp;
	@Default
	Coordinate coordinate = new Coordinate(0, 0);
	@Range(max = 360)
	Double bearing;
	@Min(0)
	Double speed;
	Double rateOfTurn;

	public double latitude() {
		return coordinate.latitude().getValue().doubleValue();
	}

	public double longitude() {
		return coordinate.longitude().getValue().doubleValue();
	}

	public Point2D position() {
		return coordinate.toPoint();
	}

	public LatLng latLng() {
		return coordinate.toLatLng();
	}

}