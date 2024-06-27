package uk.co.bluegecko.csv.apache.read;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;
import java.util.function.Function;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.junit.jupiter.api.Test;
import uk.co.bluegecko.common.model.country.CountryData;
import uk.co.bluegecko.csv.apache.AbstractApacheCountryTest;
import uk.co.bluegecko.csv.data.fixture.CountriesRaw;

public class ApacheCsvCountryReadTest extends AbstractApacheCountryTest {

	@Test
	void toCountryRaw() {
		List<CSVRecord> actual = readCountriesFromCsv(CSVFormat.DEFAULT, FILENAME, Function.identity());
		assertThat(actual).hasSize(250);
		assertThat(actual.getFirst().values())
				.contains("1", "AD", "Andorra", "Andorra", "376", "Europe", "Andorra la " + "Vella", "EUR", "ca");
	}

	@Test
	void toDataMapped() {
		assertThat(readCountriesFromCsv(CSVFormat.DEFAULT, FILENAME, r -> CountryData.from(r.values())))
				.hasSize(250)
				.containsAll(CountriesRaw.from(CountryData.to()));
	}

	private <T> List<T> readCountriesFromCsv(CSVFormat format, String filename, Function<CSVRecord, T> f) {
		try (InputStream in = getClass().getClassLoader().getResourceAsStream(filename)) {
			assertThat(in).describedAs("Missing resource for '%s'", filename).isNotNull();
			try (CSVParser parser = format.builder()
					.setHeader(CountryData.Fields.class)
					.setSkipHeaderRecord(true)
					.build()
					.parse(new InputStreamReader(in))) {
				return parser.stream().map(f).toList();
			}
		} catch (IOException e) {
			System.err.printf("ERROR: Unable to load '%s' due to %s\n", filename, e.getMessage());
			return List.of();
		}
	}

}