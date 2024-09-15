package uk.co.bluegecko.marine.model;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.Month;
import uk.co.bluegecko.common.clock.SteppingClock;

public class AbstractTest {

	protected SteppingClock clock;

	protected void setUpClock() {
		clock = SteppingClock.stepping(LocalDate.of(2020, Month.JANUARY, 1), LocalTime.of(12, 0));
	}
	
}