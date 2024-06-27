package uk.co.bluegecko.common.model.country;

import static uk.co.bluegecko.common.model.country.Country.integer;
import static uk.co.bluegecko.common.model.country.Country.toList;
import static uk.co.bluegecko.common.model.country.Country.toSet;

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

@Data
@Builder
@FieldNameConstants(asEnum = true)
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CountryData implements Country.Bean {

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

	public static CountryData from(String[] args) {
		return to().apply(args);
	}

	public static Function<String[], CountryData> to() {
		return s -> new CountryData(integer(s[0]), s[1], s[2], s[3], toList(s[4]), s[5], s[6],
				toSet(s[7]), toSet(s[8]));
	}

}