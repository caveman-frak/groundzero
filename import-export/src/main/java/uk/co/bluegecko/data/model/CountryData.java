package uk.co.bluegecko.data.model;

import static uk.co.bluegecko.data.fixture.CountriesRaw.toSet;

import java.util.List;
import java.util.Set;
import java.util.function.Function;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Singular;
import lombok.experimental.FieldDefaults;
import lombok.experimental.FieldNameConstants;
import uk.co.bluegecko.data.fixture.CountriesRaw;

@Data
@Builder
@FieldNameConstants(asEnum = true)
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CountryData implements Country.Bean {

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

	public static List<CountryData> countries() {
		return CountriesRaw.to(converter());
	}

	public static Function<String[], CountryData> converter() {
		return s -> new CountryData(s[0], s[1], s[2], s[3], s[4], s[5], toSet(s[6]), toSet(s[7]));
	}

}