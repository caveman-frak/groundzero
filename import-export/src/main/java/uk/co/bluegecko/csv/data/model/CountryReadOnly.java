package uk.co.bluegecko.csv.data.model;

import static uk.co.bluegecko.csv.data.fixture.CountriesRaw.integer;
import static uk.co.bluegecko.csv.data.fixture.CountriesRaw.toList;
import static uk.co.bluegecko.csv.data.fixture.CountriesRaw.toSet;

import java.util.List;
import java.util.Set;
import java.util.function.Function;
import lombok.Singular;
import lombok.Value;
import uk.co.bluegecko.csv.data.fixture.CountriesRaw;

@Value
public class CountryReadOnly implements Country.Bean {

	int id;
	String code;
	String name;
	String nativeName;
	@Singular
	List<Integer> phones;
	String continent;
	String capital;
	@Singular
	Set<String> currencies;
	@Singular
	Set<String> languages;

	public static List<CountryReadOnly> countries() {
		return CountriesRaw.to(converter());
	}

	public static CountryReadOnly countries(int index) {
		return CountriesRaw.to(converter(), index);
	}

	public static Function<String[], CountryReadOnly> converter() {
		return s -> new CountryReadOnly(integer(s[0]), s[1], s[2], s[3], toList(s[4]), s[5], s[6],
				toSet(s[7]), toSet(s[8]));
	}

}