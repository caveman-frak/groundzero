package uk.co.bluegecko.csv.opencsv.model;

import static uk.co.bluegecko.csv.data.fixture.CountriesRaw.integer;
import static uk.co.bluegecko.csv.data.fixture.CountriesRaw.toList;
import static uk.co.bluegecko.csv.data.fixture.CountriesRaw.toSet;

import com.opencsv.bean.CsvBindAndSplitByPosition;
import com.opencsv.bean.CsvBindByPosition;
import java.util.List;
import java.util.Set;
import java.util.function.Function;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.FieldNameConstants;
import uk.co.bluegecko.csv.data.fixture.CountriesRaw;
import uk.co.bluegecko.csv.data.model.Country;

@Data
@Builder
@FieldNameConstants(asEnum = true)
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CountryAnnotated implements Country.Bean {

	@CsvBindByPosition(position = 0)
	int id;
	@CsvBindByPosition(position = 1)
	String code;
	@CsvBindByPosition(position = 2)
	String name;
	@CsvBindByPosition(position = 3)
	String nativeName;
	@CsvBindAndSplitByPosition(position = 4, elementType = Integer.class, splitOn = ",", writeDelimiter = ",")
	List<Integer> phones;
	@CsvBindByPosition(position = 5)
	String continent;
	@CsvBindByPosition(position = 6)
	String capital;
	@CsvBindAndSplitByPosition(position = 7, elementType = String.class, splitOn = ",", writeDelimiter = ",")
	Set<String> currencies;
	@CsvBindAndSplitByPosition(position = 8, elementType = String.class, splitOn = ",", writeDelimiter = ",")
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