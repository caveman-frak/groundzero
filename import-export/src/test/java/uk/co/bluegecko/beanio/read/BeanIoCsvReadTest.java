package uk.co.bluegecko.beanio.read;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import org.beanio.BeanReader;
import org.beanio.StreamFactory;
import org.beanio.builder.FieldBuilder;
import org.beanio.builder.RecordBuilder;
import org.beanio.builder.StreamBuilder;
import org.beanio.types.TypeHandler;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import uk.co.bluegecko.beanio.ToSetTypeHandler;
import uk.co.bluegecko.data.model.Country;
import uk.co.bluegecko.data.model.CountryData;
import uk.co.bluegecko.data.model.CountryRecord;
import uk.co.bluegecko.data.model.CountryValue;

public class BeanIoCsvReadTest extends AbstractReadTest {

	private static final String STREAM_NAME = "countries";
	private static final String RECORD_NAME = "country";

	@Test
	void toDataWithMapping() throws IOException {
		StreamFactory factory = StreamFactory.newInstance();
		factory.loadResource("mapping/country.xml");

		assertThat(readCountriesFromCsv(factory, FILENAME))
				.hasSize(250)
				.containsAll(CountryData.countries());
	}

	@Test
	void toDataWithBuilder() throws IOException {
		StreamFactory factory = StreamFactory.newInstance();
		factory.define(streamBuilder(new RecordBuilder(RECORD_NAME).type(CountryData.class)));

		assertThat(readCountriesFromCsv(factory, FILENAME))
				.hasSize(250)
				.containsAll(CountryData.countries());
	}

	@Test
	@Disabled("No current support for creating records")
	void toRecordWithBuilder() throws IOException {
		StreamFactory factory = StreamFactory.newInstance();
		factory.define(streamBuilder(new RecordBuilder(RECORD_NAME).type(CountryRecord.class)));

		assertThat(readCountriesFromCsv(factory, FILENAME))
				.hasSize(250)
				.containsAll(CountryRecord.countries());
	}

	@Test
	@Disabled("No current support for creating immutable records")
	void toValueWithBuilder() throws IOException {
		StreamFactory factory = StreamFactory.newInstance();
		factory.define(streamBuilder(new RecordBuilder(RECORD_NAME).type(CountryValue.class)));

		assertThat(readCountriesFromCsv(factory, FILENAME))
				.hasSize(250)
				.containsAll(CountryValue.countries());
	}

	private StreamBuilder streamBuilder(RecordBuilder recordBuilder) {
		TypeHandler handler = new ToSetTypeHandler();
		return new StreamBuilder(STREAM_NAME).format("csv").addRecord(
				recordBuilder
						.addField(new FieldBuilder("code"))
						.addField(new FieldBuilder("name"))
						.addField(new FieldBuilder("nativeName"))
						.addField(new FieldBuilder("phone"))
						.addField(new FieldBuilder("continent"))
						.addField(new FieldBuilder("capital"))
						.addField(new FieldBuilder("currencies").typeHandler(handler))
						.addField(new FieldBuilder("languages").typeHandler(handler))
		);
	}

	private List<Country> readCountriesFromCsv(StreamFactory factory, String filename) throws IOException {
		List<Country> countries = new ArrayList<>();
		try (InputStream in = getClass().getClassLoader().getResourceAsStream(filename)) {
			assertThat(in).isNotNull();
			try (BeanReader reader = factory.createReader(STREAM_NAME, new InputStreamReader(in))) {
				reader.skip(HEADER);

				Country country;
				while ((country = (Country) reader.read()) != null) {
					countries.add(country);
				}
			}
		}
		return countries;
	}

}