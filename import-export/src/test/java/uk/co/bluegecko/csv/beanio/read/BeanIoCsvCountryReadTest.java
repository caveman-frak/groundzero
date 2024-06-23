package uk.co.bluegecko.csv.beanio.read;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import org.beanio.BeanReader;
import org.beanio.StreamFactory;
import org.beanio.builder.CsvParserBuilder;
import org.beanio.builder.RecordBuilder;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import uk.co.bluegecko.csv.beanio.AbstractBeanIoCountryTest;
import uk.co.bluegecko.csv.data.model.Country;
import uk.co.bluegecko.csv.data.model.CountryData;
import uk.co.bluegecko.csv.data.model.CountryReadOnly;
import uk.co.bluegecko.csv.data.model.CountryRecord;
import uk.co.bluegecko.csv.data.model.CountryValue;

public class BeanIoCsvCountryReadTest extends AbstractBeanIoCountryTest {

	@Test
	void toDataWithMapping() {
		StreamFactory factory = StreamFactory.newInstance();
		// load the mapping file
		factory.loadResource(MAPPING_FILE);

		assertThat(readCountriesFromCsv(factory, FILENAME))
				.hasSize(250)
				.containsAll(CountryData.countries());
	}

	@Test
	void toDataWithBuilder() {
		StreamFactory factory = StreamFactory.newInstance();
		// create a stream builder to define the record and fields
		factory.define(streamBuilder(new RecordBuilder(RECORD_NAME).type(CountryData.class),
				FIELDS, (f, e) -> f));

		assertThat(readCountriesFromCsv(factory, FILENAME))
				.hasSize(250)
				.containsAll(CountryData.countries());
	}

	/**
	 * For immutable/read-only beans we need to specify the field setter as #N (1 based) to represent the constructor
	 * parameter index.
	 */
	@Test
	void toReadOnlyWithBuilder() {
		StreamFactory factory = StreamFactory.newInstance();
		factory.define(streamBuilder(new RecordBuilder(RECORD_NAME).type(CountryReadOnly.class),
				FIELDS, (f, e) -> f.setter("#" + e.getKey())));

		assertThat(readCountriesFromCsv(factory, FILENAME))
				.hasSize(250)
				.containsAll(CountryReadOnly.countries());
	}

	/**
	 * For Java Record we need to specify the field setter as #N (1 based) to represent the constructor parameter index
	 * and explicitly define the getter (as it does not correspond to the bean specification.
	 */
	@Test
	void toRecordWithBuilder() {
		StreamFactory factory = StreamFactory.newInstance();
		factory.define(streamBuilder(new RecordBuilder(RECORD_NAME).type(CountryRecord.class),
				FIELDS, (f, e) -> f.setter("#" + e.getKey()).getter(e.getValue())));

		assertThat(readCountriesFromCsv(factory, FILENAME))
				.hasSize(250)
				.containsAll(CountryRecord.countries());
	}

	/**
	 * Not currently aware of a way to get the bean created using the lombok {@link lombok.Builder}.
	 */
	@Test
	@Disabled("No current support for creating bean with lombok builder / factory method")
	void toValueWithBuilder() {
		StreamFactory factory = StreamFactory.newInstance();
		factory.define(streamBuilder(new RecordBuilder(RECORD_NAME).type(CountryValue.class),
				FIELDS, (f, e) -> f.setter("#" + e.getValue())));

		assertThat(readCountriesFromCsv(factory, FILENAME))
				.hasSize(250)
				.containsAll(CountryValue.countries());
	}

	/**
	 * Loading non-standard CSV file, with different quote and delimiter, and supporting multi-line. Additional options
	 * specified using the {@link org.beanio.builder.ParserBuilder}.
	 */
	@Test
	void toDataFromNonStandard() {
		StreamFactory factory = StreamFactory.newInstance();
		// create a stream builder to define the record and fields
		factory.define(streamBuilder(new RecordBuilder(RECORD_NAME).type(CountryData.class),
				FIELDS, (f, e) -> f).parser(
				new CsvParserBuilder().delimiter(';').quote('\'').allowUnquotedWhitespace().enableMultiline()));

		assertThat(readCountriesFromCsv(factory, "countries-nonstandard.csv"))
				.hasSize(3)
				.contains(CountryData.builder().id(1).code("AD").name("Andorra").nativeName("Andorra").phone(375)
						.phone(376).continent("Europe").capital("Andorra la Vella").currency("EUR").language("ca")
						.build())
				.contains(CountryData.builder().id(2).code("AE").name("""
								United Arab
								Emirates""")
						.nativeName("دولة الإمارات العربية المتحدة").phone(971).continent("Asia")
						.capital("Abu Dhabi").currency("AED").currency("USD").language("ar").build())
				.contains(CountryData.builder().id(3).code("AF").name("Afghanistan").nativeName("افغانستان").phone(93)
						.continent("Asia").capital("Kabul").currency("AFN").languages(Set.of("ps", "uz", "tk"))
						.build());
	}

	private List<Country> readCountriesFromCsv(StreamFactory factory, String filename) {
		List<Country> countries = new ArrayList<>();
		try (InputStream in = getClass().getClassLoader().getResourceAsStream(filename)) {
			assertThat(in).describedAs("Missing input stream for '%s'", filename).isNotNull();
			try (BeanReader reader = factory.createReader(STREAM_NAME, new InputStreamReader(in))) {
				reader.skip(HEADERS);

				Country country;
				while ((country = (Country) reader.read()) != null) {
					countries.add(country);
				}
			}
		} catch (IOException e) {
			System.err.printf("ERROR: Unable to load %s due to %s\n", filename, e.getMessage());
			return List.of();
		}
		return countries;
	}

}