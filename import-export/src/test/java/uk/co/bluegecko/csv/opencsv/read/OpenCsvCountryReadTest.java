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
import java.util.function.BiConsumer;
import org.junit.jupiter.api.Test;
import uk.co.bluegecko.csv.data.model.Country;
import uk.co.bluegecko.csv.data.model.CountryData;
import uk.co.bluegecko.csv.opencsv.AbstractOpenCsvCountryTest;

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