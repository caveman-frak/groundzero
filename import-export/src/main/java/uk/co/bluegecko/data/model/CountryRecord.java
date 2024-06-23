package uk.co.bluegecko.data.model;

import static uk.co.bluegecko.data.fixture.CountriesRaw.toList;
import static uk.co.bluegecko.data.fixture.CountriesRaw.toSet;

import java.util.List;
import java.util.Set;
import java.util.function.Function;
import uk.co.bluegecko.data.fixture.CountriesRaw;

public record CountryRecord(
		String code,
		String name,
		String nativeName,
		List<Integer> phones,
		String continent,
		String capital,
		Set<String> currencies,
		Set<String> languages) implements Country {

	public static List<CountryRecord> countries() {
		return CountriesRaw.to(converter());
	}

	public static Function<String[], CountryRecord> converter() {
		return s -> new CountryRecord(s[0], s[1], s[2], toList(s[3]), s[4], s[5], toSet(s[6]), toSet(s[7]));
	}

}