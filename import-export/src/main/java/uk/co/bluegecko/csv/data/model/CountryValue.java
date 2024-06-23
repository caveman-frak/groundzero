package uk.co.bluegecko.csv.data.model;

import static uk.co.bluegecko.csv.data.fixture.CountriesRaw.integer;
import static uk.co.bluegecko.csv.data.fixture.CountriesRaw.toList;
import static uk.co.bluegecko.csv.data.fixture.CountriesRaw.toSet;

import java.util.List;
import java.util.Set;
import java.util.function.Function;
import lombok.Builder;
import lombok.Singular;
import lombok.Value;
import uk.co.bluegecko.csv.data.fixture.CountriesRaw;

@Value
@Builder
public class CountryValue implements Country.Bean {

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

	public static List<CountryValue> countries() {
		return CountriesRaw.to(converter());
	}

	public static Function<String[], CountryValue> converter() {
		return s -> new CountryValue(integer(s[0]), s[1], s[2], s[3], toList(s[4]), s[5], s[6],
				toSet(s[7]), toSet(s[8]));
	}

}