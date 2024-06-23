package uk.co.bluegecko.data.model;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Set;
import org.junit.jupiter.api.Test;

class CountryReadOnlyTest {

	@Test
	void convertor() {
		assertThat(CountryReadOnly.converter().apply(new String[]{"HT", "Haiti", "Haïti", "509", "North America",
				"Port-au-Prince", "HTG,USD", "fr,ht"}))
				.isEqualTo(
						new CountryReadOnly("HT", "Haiti", "Haïti", "509",
								"North America", "Port-au-Prince", Set.of("HTG", "USD"), Set.of("fr", "ht")));
	}

	@Test
	void countries() {
		assertThat(CountryReadOnly.countries()).hasSize(250).contains(
				new CountryReadOnly("HT", "Haiti", "Haïti", "509",
						"North America", "Port-au-Prince", Set.of("HTG", "USD"), Set.of("fr", "ht")));
	}

}