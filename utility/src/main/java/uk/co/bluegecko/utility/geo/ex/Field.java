package uk.co.bluegecko.utility.geo.ex;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

@Getter
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public enum Field {

	DECIMAL("Decimal"), DEGREES("Degrees"), MINUTES("Minutes"), SECONDS("Seconds");

	String text;

}