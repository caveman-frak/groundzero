package uk.co.bluegecko.csv.data.fixture;

import static org.assertj.core.api.Assertions.assertThat;
import static uk.co.bluegecko.csv.data.fixture.CountriesRaw.toList;
import static uk.co.bluegecko.csv.data.fixture.CountriesRaw.toSet;

import org.junit.jupiter.api.Test;

class CountriesRawTest {

	@Test
	void toSetWithOne() {
		assertThat(toSet("en")).containsExactly("en");
	}

	@Test
	void toSetWithMany() {
		assertThat(toSet("de,en,fr,pt,sp")).containsExactlyInAnyOrder("de", "en", "fr", "pt", "sp");
	}

	@Test
	void toSetWithEmpty() {
		assertThat(toSet("")).isEmpty();
	}

	@Test
	void toSetWithNull() {
		assertThat(toSet(null)).isEmpty();
	}

	@Test
	void toListWithOne() {
		assertThat(toList("1809")).containsExactly(1809);
	}

	@Test
	void toListWithMany() {
		assertThat(toList("1809,1829,1849")).containsExactlyInAnyOrder(1809, 1829, 1849);
	}

	@Test
	void toListWithEmpty() {
		assertThat(toList("")).isEmpty();
	}

	@Test
	void toListWithNull() {
		assertThat(toList(null)).isEmpty();
	}

	@Test
	void countCountries() {
		assertThat(CountriesRaw.countries()).hasDimensions(250, 9);
	}


}