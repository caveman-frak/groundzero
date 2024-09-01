package uk.co.bluegecko.common.clock;

import java.time.Clock;
import java.time.Duration;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.util.concurrent.atomic.AtomicReference;

public class SteppingClock extends Clock {

	private final AtomicReference<Instant> instant;
	private final ZoneId zone;

	private SteppingClock(Instant instant, ZoneId zone) {
		this.instant = new AtomicReference<>(instant);
		this.zone = zone;
	}

	public Instant tick(Duration tick) {
		return instant.getAndSet(instant().plus(tick));
	}

	public Instant tick() {
		return tick(Duration.ofSeconds(1));
	}

	@Override
	public ZoneId getZone() {
		return zone;
	}

	@Override
	public Clock withZone(ZoneId zone) {
		if (zone.equals(this.zone)) {  // intentional NPE
			return this;
		}
		return new SteppingClock(instant(), zone);
	}

	@Override
	public long millis() {
		return instant().toEpochMilli();
	}

	@Override
	public Instant instant() {
		return instant.get();
	}

	@Override
	public boolean equals(Object obj) {
		return obj instanceof SteppingClock other
				&& instant().equals(other.instant())
				&& zone.equals(other.zone);
	}

	@Override
	public int hashCode() {
		return instant.hashCode() ^ zone.hashCode();
	}

	@Override
	public String toString() {
		return "SteppingClock[" + instant() + "," + zone + "]";
	}

	public static SteppingClock stepping(Instant instant, ZoneId zone) {
		return new SteppingClock(instant, zone);
	}

	public static SteppingClock stepping(LocalDate date, LocalTime time, ZoneOffset zone) {
		return new SteppingClock(LocalDateTime.of(date,time).toInstant(zone), zone);
	}

	public static SteppingClock stepping(LocalDate date, LocalTime time) {
		return stepping(date, time, ZoneOffset.UTC);
	}

	public static SteppingClock stepping(Clock clock) {
		return new SteppingClock(clock.instant(), clock.getZone());
	}

}