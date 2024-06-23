package uk.co.bluegecko.data.model;

import static uk.co.bluegecko.data.fixture.CountriesRaw.toSet;

import java.util.List;
import java.util.Set;
import java.util.function.Function;
import lombok.Singular;
import lombok.Value;
import uk.co.bluegecko.data.fixture.CountriesRaw;

@Value
public class CountryReadOnly implements Country.Bean {

	String code;
	String name;
	String nativeName;
	String phone;
	String continent;
	String capital;
	@Singular
	Set<String> currencies;
	@Singular
	Set<String> languages;

	public static List<CountryReadOnly> countries() {
		return CountriesRaw.to(converter());
	}

	public static Function<String[], CountryReadOnly> converter() {
		return s -> new CountryReadOnly(s[0], s[1], s[2], s[3], s[4], s[5], toSet(s[6]), toSet(s[7]));
	}

}