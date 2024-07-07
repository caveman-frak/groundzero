package uk.co.bluegecko.utility.geo.ex;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

@Getter
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public enum Direction {

	OVERFLOW("overflow"), UNDERFLOW("underflow");

	String text;

}