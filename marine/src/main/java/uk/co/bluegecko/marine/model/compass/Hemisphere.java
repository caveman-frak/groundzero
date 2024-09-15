package uk.co.bluegecko.marine.model.compass;

import java.util.Arrays;
import java.util.Optional;
import java.util.Set;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.Accessors;
import lombok.experimental.FieldDefaults;

@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
@Accessors(fluent = true)
@Getter
public enum Hemisphere {
	NORTH("N", true),
	SOUTH("S", false),
	EAST("E", true),
	WEST("W", false);

	String abbreviation;
	boolean positive;

	public static Optional<Hemisphere> fromAbbreviation(String abbreviation) {
		return Arrays.stream(values()).filter(h -> h.abbreviation().equals(abbreviation)).findFirst();
	}

	public interface Spheriod {

		Set<Hemisphere> hemispheres();

		default Optional<Hemisphere> hemisphere(boolean positive) {
			return hemispheres().stream().filter(h -> h.positive() == positive).findFirst();
		}

	}

}