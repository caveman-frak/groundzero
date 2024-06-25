package uk.co.bluegecko.csv.beanio.model;

import static uk.co.bluegecko.csv.data.fixture.CountriesRaw.integer;
import static uk.co.bluegecko.csv.data.fixture.CountriesRaw.toList;
import static uk.co.bluegecko.csv.data.fixture.CountriesRaw.toSet;

import java.util.List;
import java.util.Set;
import java.util.function.Function;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.beanio.annotation.Field;
import org.beanio.annotation.Record;
import uk.co.bluegecko.csv.data.fixture.CountriesRaw;
import uk.co.bluegecko.csv.data.model.Country;

@Data
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@FieldDefaults(level = AccessLevel.PRIVATE)
@Record(name = "country")
public class CountryAnnotated implements Country.Bean {

	@Field(at = 0)
	int id;
	@Field(at = 1)
	String code;
	@Field(at = 2)
	String name;
	@Field(at = 3)
	String nativeName;
	@Field(at = 4, handlerName = "phoneHandler", type = List.class)
	List<Integer> phones;
	@Field(at = 5)
	String continent;
	@Field(at = 6)
	String capital;
	@Field(at = 7, handlerName = "currencyHandler", type = Set.class)
	Set<String> currencies;
	@Field(at = 8, handlerName = "languageHandler", type = Set.class)
	Set<String> languages;

	public static List<CountryAnnotated> countries() {
		return CountriesRaw.to(converter());
	}

	public static CountryAnnotated countries(int index) {
		return CountriesRaw.to(converter(), index);
	}

	public static Function<String[], CountryAnnotated> converter() {
		return s -> new CountryAnnotated(integer(s[0]), s[1], s[2], s[3], toList(s[4]), s[5], s[6],
				toSet(s[7]), toSet(s[8]));
	}

}