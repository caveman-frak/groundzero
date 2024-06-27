package uk.co.bluegecko.csv.data.fixture;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import java.util.Set;
import org.junit.jupiter.api.Test;
import uk.co.bluegecko.common.model.country.CountryData;
import uk.co.bluegecko.common.model.country.CountryReadOnly;
import uk.co.bluegecko.common.model.country.CountryRecord;
import uk.co.bluegecko.common.model.country.CountryValue;

class CountriesRawTest {

	@Test
	void countCountries() {
		assertThat(CountriesRaw.countries()).hasDimensions(250, 9);
	}

	@Test
	void countriesData() {
		assertThat(CountriesRaw.from(CountryData.to())).hasSize(250).contains(
				CountryData.builder()
						.id(99).code("HT").name("Haiti").nativeName("Haïti").phone(509)
						.continent("North America").capital("Port-au-Prince").currency("HTG").currency("USD")
						.language("fr").language("ht").build());
	}

	@Test
	void countriesReadOnly() {
		assertThat(CountriesRaw.from(CountryReadOnly.to())).hasSize(250).contains(
				new CountryReadOnly(99, "HT", "Haiti", "Haïti", List.of(509),
						"North America", "Port-au-Prince", Set.of("HTG", "USD"), Set.of("fr", "ht")));
	}

	@Test
	void countriesRecord() {
		assertThat(CountriesRaw.from(CountryRecord.to())).hasSize(250).contains(
				new CountryRecord(99, "HT", "Haiti", "Haïti", List.of(509), "North America",
						"Port-au-Prince", Set.of("HTG", "USD"), Set.of("fr", "ht")));
	}

	@Test
	void countriesValue() {
		assertThat(CountriesRaw.from(CountryValue.to())).hasSize(250).contains(
				CountryValue.builder()
						.id(99).code("HT").name("Haiti").nativeName("Haïti").phone(509)
						.continent("North America").capital("Port-au-Prince").currency("HTG").currency("USD")
						.language("fr").language("ht").build());
	}

}