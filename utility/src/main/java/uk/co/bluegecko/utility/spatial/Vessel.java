package uk.co.bluegecko.utility.spatial;

import java.time.Duration;
import lombok.Builder;
import lombok.Value;

@Builder
@Value
public class Vessel<T> {

	T position;
	double speed;
	double bearing;
	double rateOfTurn;

	public double distance(Duration duration) {
		return speed * duration.toMinutes() / 60;
	}
	
}