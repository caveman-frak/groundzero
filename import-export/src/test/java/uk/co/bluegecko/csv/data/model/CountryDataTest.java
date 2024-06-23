package uk.co.bluegecko.csv.data.model;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

class CountryDataTest {

	@Test
	void countries() {
		assertThat(CountryData.countries()).hasSize(250).contains(
				CountryData.builder()
						.id(99).code("HT").name("Haiti").nativeName("Haïti").phone(509)
						.continent("North America").capital("Port-au-Prince").currency("HTG").currency("USD")
						.language("fr").language("ht").build());
	}

}