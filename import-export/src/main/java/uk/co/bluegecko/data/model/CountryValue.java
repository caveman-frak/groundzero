package uk.co.bluegecko.data.model;

import static uk.co.bluegecko.data.fixture.CountriesRaw.toSet;

import java.util.List;
import java.util.Set;
import java.util.function.Function;
import lombok.Builder;
import lombok.Singular;
import lombok.Value;
import lombok.experimental.FieldNameConstants;
import uk.co.bluegecko.data.fixture.CountriesRaw;

@Value
@Builder
@FieldNameConstants(asEnum = true)
public class CountryValue implements Country.Bean {

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

	public static List<CountryValue> countries() {
		return CountriesRaw.to(converter());
	}

	public static Function<String[], CountryValue> converter() {
		return s -> new CountryValue(s[0], s[1], s[2], s[3], s[4], s[5], toSet(s[6]), toSet(s[7]));
	}

}