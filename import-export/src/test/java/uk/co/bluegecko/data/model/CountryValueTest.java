package uk.co.bluegecko.data.model;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import java.util.Set;
import org.junit.jupiter.api.Test;

class CountryValueTest {

	@Test
	void builder() {
		CountryValue built = CountryValue.builder()
				.code("HT").name("Haiti").nativeName("Haïti").phone(509)
				.continent("North America").capital("Port-au-Prince").currency("HTG").currency("USD")
				.language("fr").language("ht").build();
		assertThat(built).isEqualTo(
				new CountryValue("HT", "Haiti", "Haïti", List.of(509), "North America",
						"Port-au-Prince", Set.of("HTG", "USD"), Set.of("fr", "ht")));
	}

	@Test
	void convertor() {
		assertThat(CountryValue.converter().apply(new String[]{"HT", "Haiti", "Haïti", "509", "North America",
				"Port-au-Prince", "HTG,USD", "fr,ht"}))
				.isEqualTo(
						new CountryValue("HT", "Haiti", "Haïti", List.of(509),
								"North America", "Port-au-Prince", Set.of("HTG", "USD"), Set.of("fr", "ht")));
	}

	@Test
	void countries() {
		assertThat(CountryValue.countries()).hasSize(250).contains(
				CountryValue.builder()
						.code("HT").name("Haiti").nativeName("Haïti").phone(509)
						.continent("North America").capital("Port-au-Prince").currency("HTG").currency("USD")
						.language("fr").language("ht").build());
	}

}