package uk.co.bluegecko.csv.data.fixture;

import static org.assertj.core.api.Assertions.assertThat;
import static uk.co.bluegecko.csv.data.fixture.CountriesRaw.fromList;
import static uk.co.bluegecko.csv.data.fixture.CountriesRaw.fromSet;
import static uk.co.bluegecko.csv.data.fixture.CountriesRaw.toList;
import static uk.co.bluegecko.csv.data.fixture.CountriesRaw.toSet;

import java.util.List;
import java.util.Set;
import java.util.TreeSet;
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
	void fromSetWithOne() {
		assertThat(fromSet(Set.of("en"))).isEqualTo("en");
	}

	@Test
	void fromSetWithMany() {
		assertThat(fromSet(new TreeSet<>(Set.of("de", "en", "fr", "pt", "sp")))).isEqualTo("de,en,fr,pt,sp");
	}

	@Test
	void fromSetWithEmpty() {
		assertThat(fromSet(Set.of())).isEqualTo("");
	}

	@Test
	void fromSetWithNull() {
		assertThat(fromSet(null)).isEqualTo("");
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
	void fromListWithOne() {
		assertThat(fromList(List.of(1809))).isEqualTo("1809");
	}

	@Test
	void fromListWithMany() {
		assertThat(fromList(List.of(1809, 1829, 1849))).isEqualTo("1809,1829,1849");
	}

	@Test
	void fromListWithEmpty() {
		assertThat(fromList(List.of())).isEqualTo("");
	}

	@Test
	void fromListWithNull() {
		assertThat(fromList(null)).isEqualTo("");
	}

	@Test
	void countCountries() {
		assertThat(CountriesRaw.countries()).hasDimensions(250, 9);
	}

}