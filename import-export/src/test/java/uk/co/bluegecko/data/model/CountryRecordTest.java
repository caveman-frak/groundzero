package uk.co.bluegecko.data.model;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import java.util.Set;
import org.junit.jupiter.api.Test;

class CountryRecordTest {

	@Test
	void countries() {
		assertThat(CountryRecord.countries()).hasSize(250).contains(
				new CountryRecord("HT", "Haiti", "Haïti", List.of(509), "North America",
						"Port-au-Prince", Set.of("HTG", "USD"), Set.of("fr", "ht")));
	}

}