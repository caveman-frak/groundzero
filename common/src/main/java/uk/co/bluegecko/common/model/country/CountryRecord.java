package uk.co.bluegecko.common.model.country;

import static uk.co.bluegecko.common.model.country.Country.integer;
import static uk.co.bluegecko.common.model.country.Country.toList;
import static uk.co.bluegecko.common.model.country.Country.toSet;

import java.util.List;
import java.util.Set;
import java.util.function.Function;

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

	public static CountryRecord from(String[] args) {
		return to().apply(args);
	}

	public static Function<String[], CountryRecord> to() {
		return s -> new CountryRecord(integer(s[0]), s[1], s[2], s[3], toList(s[4]), s[5], s[6],
				toSet(s[7]), toSet(s[8]));
	}

}