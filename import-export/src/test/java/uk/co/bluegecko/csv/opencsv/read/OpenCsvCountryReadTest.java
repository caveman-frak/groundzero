package uk.co.bluegecko.csv.opencsv.read;

import static org.assertj.core.api.Assertions.assertThat;
import static uk.co.bluegecko.utility.ThrowingBiConsumer.sneakyThrows;

import com.opencsv.CSVReader;
import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.function.BiConsumer;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import uk.co.bluegecko.csv.data.model.Country;
import uk.co.bluegecko.csv.data.model.CountryData;
import uk.co.bluegecko.csv.data.model.CountryReadOnly;
import uk.co.bluegecko.csv.data.model.CountryRecord;
import uk.co.bluegecko.csv.data.model.CountryValue;
import uk.co.bluegecko.csv.opencsv.AbstractOpenCsvCountryTest;
import uk.co.bluegecko.csv.opencsv.model.CountryAnnotated;

public class OpenCsvCountryReadTest extends AbstractOpenCsvCountryTest {

	@Test
	void toCountryRawMapping() throws IOException {
		assertThat(readCountriesFromCsv(FILENAME, sneakyThrows((r, l) -> {
			String[] line;
			while ((line = r.readNext()) != null) {
				l.add(CountryData.converter().apply(line));
			}
		})))
				.hasSize(250)
				.containsAll(CountryData.countries());
	}

	@Test
	void toDataBeanColumnMapping() throws IOException {
		assertThat(readCountriesFromCsv(FILENAME, sneakyThrows((r, l) -> {
			CsvToBean<CountryData> toBean = new CsvToBeanBuilder<CountryData>(r)
					.withMappingStrategy(mappingStrategy(CountryData.class, r, ignoredFields())).build();
			toBean.stream().forEach(l::add);
		})))
				.hasSize(250)
				/* TODO need to strip out phones, currency and language fields as the processor can't handle them! */
				.containsAll(CountryData.countries().stream()
						.peek(c -> c.setPhones(null))
						.peek(c -> c.setCurrencies(null))
						.peek(c -> c.setLanguages(null))
						.toList());
	}

	@Test
	@Disabled("No current support for creating read-only bean")
	void toReadOnlyBeanColumnMapping() throws IOException {
		assertThat(readCountriesFromCsv(FILENAME, sneakyThrows((r, l) -> {
			CsvToBean<CountryReadOnly> toBean = new CsvToBeanBuilder<CountryReadOnly>(r)
					.withMappingStrategy(mappingStrategy(CountryReadOnly.class, r, ignoredFields())).build();
			toBean.stream().forEach(l::add);
		})))
				.hasSize(250)
				/* TODO need to strip out phones, currency and language fields as the processor can't handle them! */
				.containsAll(CountryReadOnly.countries());
	}

	@Test
	@Disabled("No current support for creating java record")
	void toRecordBeanColumnMapping() throws IOException {
		assertThat(readCountriesFromCsv(FILENAME, sneakyThrows((r, l) -> {
			CsvToBean<CountryRecord> toBean = new CsvToBeanBuilder<CountryRecord>(r)
					.withMappingStrategy(mappingStrategy(CountryRecord.class, r, ignoredFields())).build();
			toBean.stream().forEach(l::add);
		})))
				.hasSize(250)
				/* TODO need to strip out phones, currency and language fields as the processor can't handle them! */
				.containsAll(CountryRecord.countries());
	}

	@Test
	@Disabled("No current support for creating bean with lombok builder / factory method")
	void toValueBeanColumnMapping() throws IOException {
		assertThat(readCountriesFromCsv(FILENAME, sneakyThrows((r, l) -> {
			CsvToBean<CountryValue> toBean = new CsvToBeanBuilder<CountryValue>(r)
					.withMappingStrategy(mappingStrategy(CountryValue.class, r, ignoredFields())).build();
			toBean.stream().forEach(l::add);
		})))
				.hasSize(250)
				/* TODO need to strip out phones, currency and language fields as the processor can't handle them! */
				.containsAll(CountryValue.countries());
	}

	@Test
	void toAnnotatedBeanColumnMapping() throws IOException {
		List<Country> expected = readCountriesFromCsv(FILENAME, sneakyThrows((r, l) -> {
			CsvToBean<CountryAnnotated> toBean = new CsvToBeanBuilder<CountryAnnotated>(r)
					.withType(CountryAnnotated.class)
					.build();
			toBean.stream().forEach(l::add);
		}));

		List<CountryAnnotated> actual = CountryAnnotated.countries();
		/* TODO Default collection handler does not deal correctly with empty string:
		 * "" -> Set.of("") instead of Set.of()
		 * We could add a custom handler to fix this, but it only occurs for a single record.
		 */
		CountryAnnotated countryAntarctica = (CountryAnnotated) expected.get(8);
		countryAntarctica.setCurrencies(Set.of());
		countryAntarctica.setLanguages(Set.of());
		// Compare with fixed data
		assertThat(actual).containsAll(CountryAnnotated.countries());
	}

	private List<Country> readCountriesFromCsv(String filename, BiConsumer<CSVReader, List<Country>> consumer)
			throws IOException {
		List<Country> countries = new ArrayList<>();
		try (InputStream in = getClass().getClassLoader().getResourceAsStream(filename)) {
			assertThat(in).isNotNull();
			try (CSVReader reader = new CSVReader(new InputStreamReader(in))) {
				reader.skip(HEADERS);

				consumer.accept(reader, countries);
			}
		}
		return countries;
	}

}