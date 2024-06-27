package uk.co.bluegecko.common.model.country;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import java.util.Set;
import org.junit.jupiter.api.Test;

class CountryRecordTest {

	@Test
	void toRecord() {
		assertThat(CountryRecord.to().apply(
				new String[]{"99", "HT", "Haiti", "Haïti", "509", "North America", "Port-au-Prince", "HTG,USD",
						"fr,ht"}))
				.isEqualTo(
						new CountryRecord(99, "HT", "Haiti", "Haïti", List.of(509),
								"North America", "Port-au-Prince", Set.of("HTG", "USD"), Set.of("fr", "ht")));
	}


}