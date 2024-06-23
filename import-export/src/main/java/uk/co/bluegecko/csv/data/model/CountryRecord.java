package uk.co.bluegecko.csv.data.model;

import static uk.co.bluegecko.csv.data.fixture.CountriesRaw.integer;
import static uk.co.bluegecko.csv.data.fixture.CountriesRaw.toList;
import static uk.co.bluegecko.csv.data.fixture.CountriesRaw.toSet;

import java.util.List;
import java.util.Set;
import java.util.function.Function;
import uk.co.bluegecko.csv.data.fixture.CountriesRaw;

public record CountryRecord(
		int id,
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
		return s -> new CountryRecord(integer(s[0]), s[1], s[2], s[3], toList(s[4]), s[5], s[6],
				toSet(s[7]), toSet(s[8]));
	}

}