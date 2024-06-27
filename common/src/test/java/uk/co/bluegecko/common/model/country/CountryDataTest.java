package uk.co.bluegecko.common.model.country;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

class CountryDataTest {

	@Test
	void toData() {
		assertThat(CountryData.to().apply(
				new String[]{"99", "HT", "Haiti", "Haïti", "509", "North America", "Port-au-Prince", "HTG,USD",
						"fr,ht"}))
				.isEqualTo(
						CountryData.builder()
								.id(99).code("HT").name("Haiti").nativeName("Haïti").phone(509)
								.continent("North America").capital("Port-au-Prince").currency("HTG").currency("USD")
								.language("fr").language("ht").build());
	}

}