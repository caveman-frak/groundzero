package uk.co.bluegecko.csv.data.model;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import java.util.Set;
import org.junit.jupiter.api.Test;

class CountryReadOnlyTest {

	@Test
	void convertor() {
		assertThat(CountryReadOnly.converter().apply(new String[]{"99", "HT", "Haiti", "Haïti", "509", "North America",
				"Port-au-Prince", "HTG,USD", "fr,ht"}))
				.isEqualTo(
						new CountryReadOnly(99, "HT", "Haiti", "Haïti", List.of(509),
								"North America", "Port-au-Prince", Set.of("HTG", "USD"), Set.of("fr", "ht")));
	}

	@Test
	void countries() {
		assertThat(CountryReadOnly.countries()).hasSize(250).contains(
				new CountryReadOnly(99, "HT", "Haiti", "Haïti", List.of(509),
						"North America", "Port-au-Prince", Set.of("HTG", "USD"), Set.of("fr", "ht")));
	}

}