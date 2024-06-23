package uk.co.bluegecko.csv.opencvs.read;

import static org.assertj.core.api.Assertions.assertThat;
import static uk.co.bluegecko.utility.ThrowingBiConsumer.sneakyThrows;

import com.opencsv.CSVReader;
import com.opencsv.bean.ColumnPositionMappingStrategy;
import com.opencsv.bean.ColumnPositionMappingStrategyBuilder;
import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.BiConsumer;
import org.apache.commons.collections4.multimap.ArrayListValuedHashMap;
import org.junit.jupiter.api.Test;
import uk.co.bluegecko.AbstractCountryTest;
import uk.co.bluegecko.csv.data.model.Country;
import uk.co.bluegecko.csv.data.model.CountryData;
import uk.co.bluegecko.csv.data.model.CountryData.Fields;

public class OpenCsvCountryReadTest extends AbstractCountryTest {

	@Test
	void toCountryManualMapping() throws IOException {
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
	void toDataBeanMapping() throws IOException {
		assertThat(readCountriesFromCsv(FILENAME, sneakyThrows((r, l) -> {
			CsvToBean<CountryData> builder = new CsvToBeanBuilder<CountryData>(r)
					.withType(CountryData.class).withMappingStrategy(mappingStrategy(r))
					.build();
			builder.stream().forEach(l::add);
		})))
				.hasSize(250)
				/* TODO need to strip out phones, currency and language fields as the processor can't handle them! */
				.containsAll(CountryData.countries().stream()
						.peek(c -> c.setPhones(null))
						.peek(c -> c.setCurrencies(null))
						.peek(c -> c.setLanguages(null))
						.toList());
	}

	private ColumnPositionMappingStrategy<CountryData> mappingStrategy(CSVReader r)
			throws NoSuchFieldException, IOException {
		ColumnPositionMappingStrategy<CountryData> mappingStrategy = new ColumnPositionMappingStrategyBuilder<CountryData>()
				.build();
		mappingStrategy.setType(CountryData.class);
		mappingStrategy.setColumnMapping(Arrays.stream(Fields.values()).map(Enum::name).toArray(String[]::new));
		mappingStrategy.ignoreFields(ignoredFields());
		mappingStrategy.captureHeader(r);
		return mappingStrategy;
	}

	private ArrayListValuedHashMap<Class<?>, Field> ignoredFields()
			throws NoSuchFieldException {
		ArrayListValuedHashMap<Class<?>, Field> fields = new ArrayListValuedHashMap<>();
		fields.put(CountryData.class, CountryData.class.getDeclaredField("phones"));
		fields.put(CountryData.class, CountryData.class.getDeclaredField("currencies"));
		fields.put(CountryData.class, CountryData.class.getDeclaredField("languages"));
		return fields;
	}

	private List<Country> readCountriesFromCsv(String filename,
			BiConsumer<CSVReader, List<Country>> consumer)
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