package uk.co.bluegecko.marine.model;

import com.uber.h3core.util.LatLng;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import java.awt.geom.Point2D;
import java.time.Instant;
import java.util.UUID;
import lombok.Builder;
import lombok.Value;
import lombok.With;
import lombok.extern.jackson.Jacksonized;
import org.hibernate.validator.constraints.Range;

@Value
@Builder(toBuilder = true)
@With
@Jacksonized
public class Trace {

	@NotNull
	UUID vesselId;
	@PastOrPresent
	Instant timestamp;
	@Range(min = -90, max = +90)
	double latitude;
	@Range(min = -180, max = +180)
	double longitude;
	@Range(max = 360)
	Double bearing;
	@Min(0)
	Double speed;
	Double rateOfTurn;

	public Point2D position() {
		return new Point2D.Double(longitude, latitude);
	}

	public LatLng latLng() {
		return new LatLng(longitude, latitude);
	}

}