package uk.co.bluegecko.data.fixture;

import static org.assertj.core.api.Assertions.assertThat;
import static uk.co.bluegecko.data.fixture.CountriesRaw.toSet;

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
	void countCountries() {
		assertThat(CountriesRaw.countries()).hasDimensions(250, 8);
	}


}