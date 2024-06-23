package uk.co.bluegecko.beanio.read;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;
import java.util.SortedMap;
import java.util.function.BiFunction;
import org.beanio.BeanReader;
import org.beanio.StreamFactory;
import org.beanio.builder.FieldBuilder;
import org.beanio.builder.RecordBuilder;
import org.beanio.builder.StreamBuilder;
import org.beanio.types.TypeHandler;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import uk.co.bluegecko.beanio.ListOfIntTypeHandler;
import uk.co.bluegecko.beanio.SetOfStringTypeHandler;
import uk.co.bluegecko.data.model.Country;
import uk.co.bluegecko.data.model.CountryData;
import uk.co.bluegecko.data.model.CountryReadOnly;
import uk.co.bluegecko.data.model.CountryRecord;
import uk.co.bluegecko.data.model.CountryValue;

public class BeanIoCsvReadTest extends AbstractReadTest {

	private static final String MAPPING_FILE = "mapping/country.xml";
	private static final String STREAM_NAME = "countries";
	private static final String RECORD_NAME = "country";

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

	private StreamBuilder streamBuilder(RecordBuilder recordBuilder, SortedMap<Integer, String> fields,
			BiFunction<FieldBuilder, Entry<Integer, String>, FieldBuilder> fieldBuilder) {
		addFields(recordBuilder, fields, fieldBuilder);
		return new StreamBuilder(STREAM_NAME).format("csv").addRecord(recordBuilder);
	}

	private void addFields(RecordBuilder recordBuilder, SortedMap<Integer, String> fields,
			BiFunction<FieldBuilder, Entry<Integer, String>, FieldBuilder> fieldBuilder) {
		TypeHandler setHandler = new SetOfStringTypeHandler();
		TypeHandler listHandler = new ListOfIntTypeHandler();
		fields.entrySet().forEach(e -> {
			FieldBuilder builder = new FieldBuilder(e.getValue());
			if (SET_FIELDS.contains(e.getValue())) {
				builder.typeHandler(setHandler);
			} else if (LIST_FIELDS.contains(e.getValue())) {
				builder.typeHandler(listHandler);
			}

			recordBuilder.addField(fieldBuilder.apply(builder, e));
		});
	}

	private List<Country> readCountriesFromCsv(StreamFactory factory, String filename) {
		List<Country> countries = new ArrayList<>();
		try (InputStream in = getClass().getClassLoader().getResourceAsStream(filename)) {
			assertThat(in).isNotNull();
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