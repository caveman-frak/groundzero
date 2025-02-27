package uk.co.bluegecko.csv.beanio.write;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.CharArrayWriter;
import java.util.Map;
import java.util.TreeMap;
import org.beanio.BeanWriter;
import org.beanio.StreamFactory;
import org.beanio.builder.CsvParserBuilder;
import org.beanio.builder.RecordBuilder;
import org.beanio.builder.StreamBuilder;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import uk.co.bluegecko.common.model.country.CountryData;
import uk.co.bluegecko.common.model.country.CountryReadOnly;
import uk.co.bluegecko.common.model.country.CountryRecord;
import uk.co.bluegecko.common.model.country.CountryValue;
import uk.co.bluegecko.csv.beanio.AbstractBeanIoCountryTest;
import uk.co.bluegecko.csv.beanio.handler.ListOfIntTypeHandler;
import uk.co.bluegecko.csv.beanio.handler.SetOfStringTypeHandler;
import uk.co.bluegecko.csv.beanio.model.CountryAnnotated;
import uk.co.bluegecko.csv.data.fixture.CountriesRaw;

public class BeanIOCountryWriteTest extends AbstractBeanIoCountryTest {


	@Test
	void fromDataWithMapper() {
		StreamFactory factory = StreamFactory.newInstance();
		factory.loadResource(MAPPING_FILE);

		CharArrayWriter writer = new CharArrayWriter();
		try (BeanWriter out = factory.createWriter(STREAM_NAME, writer)) {
			out.write(CountriesRaw.from(CountryData.to(), 0));
			out.flush();
		}
		assertThat(writer.toString()).contains("1,AD,Andorra,Andorra,376,Europe,Andorra la Vella,EUR,ca");
	}

	@Test
	void fromDataWithBuilder() {
		StreamFactory factory = StreamFactory.newInstance();
		factory.define(streamBuilder(new RecordBuilder(RECORD_NAME).type(CountryData.class), FIELDS, (f, e) -> f));

		CharArrayWriter writer = new CharArrayWriter();
		try (BeanWriter out = factory.createWriter(STREAM_NAME, writer)) {
			out.write(CountriesRaw.from(CountryData.to(), 0));
			out.flush();
		}
		assertThat(writer.toString()).contains("1,AD,Andorra,Andorra,376,Europe,Andorra la Vella,EUR,ca\n");
	}

	@Test
	void fromDataWithBuilderWithHeader() {
		StreamFactory factory = StreamFactory.newInstance();
		factory.define(streamBuilder(new RecordBuilder(RECORD_NAME).type(CountryData.class), FIELDS, (f, e) -> f));

		CharArrayWriter writer = new CharArrayWriter();
		try (BeanWriter out = factory.createWriter(STREAM_NAME, writer)) {
			out.write(RECORD_HEADER, null);
			out.write(CountriesRaw.from(CountryData.to(), 0));
			out.flush();
		}
		assertThat(writer.toString()).contains("""
				id,code,name,nativeName,phones,continent,capital,currencies,languages
				1,AD,Andorra,Andorra,376,Europe,Andorra la Vella,EUR,ca
				""");
	}

	@Test
	void fromDataWithBuilderQuoted() {
		StreamFactory factory = StreamFactory.newInstance();
		factory.define(streamBuilder(new RecordBuilder(RECORD_NAME).type(CountryData.class), FIELDS, (f, e) -> f)
				.parser(new CsvParserBuilder().alwaysQuote()));

		CharArrayWriter writer = new CharArrayWriter();
		try (BeanWriter out = factory.createWriter(STREAM_NAME, writer)) {
			out.write(CountriesRaw.from(CountryData.to(), 0));
			out.flush();
		}
		assertThat(writer.toString()).contains(
				"\"1\",\"AD\",\"Andorra\",\"Andorra\",\"376\",\"Europe\",\"Andorra la Vella\",\"EUR\",\"ca\"\n");
	}

	@Test
	void fromDataWithBuilderAbbreviated() {
		StreamFactory factory = StreamFactory.newInstance();
		factory.define(streamBuilder(new RecordBuilder(RECORD_NAME).type(CountryData.class),
				new TreeMap<>(Map.of(
						2, "code",
						3, "name",
						4, "nativeName",
						6, "continent",
						9, "languages")), (f, e) -> f));

		CharArrayWriter writer = new CharArrayWriter();
		try (BeanWriter out = factory.createWriter(STREAM_NAME, writer)) {
			out.write(CountriesRaw.from(CountryData.to(), 0));
			out.flush();
		}
		assertThat(writer.toString()).contains("AD,Andorra,Andorra,Europe,ca\n");
	}

	@Test
	void fromDataWithBuilderAllRecordsWithHeader() {
		StreamFactory factory = StreamFactory.newInstance();
		factory.define(streamBuilder(new RecordBuilder(RECORD_NAME).type(CountryData.class), FIELDS, (f, e) -> f));

		CharArrayWriter writer = new CharArrayWriter();
		try (BeanWriter out = factory.createWriter(STREAM_NAME, writer)) {
			out.write(RECORD_HEADER, null);
			CountriesRaw.from(CountryData.to()).forEach(out::write);
			out.flush();
		}
		assertThat(writer.toString())
				.hasSize(14809)
				.startsWith("""
						id,code,name,nativeName,phones,continent,capital,currencies,languages
						1,AD,Andorra,Andorra,376,Europe,Andorra la Vella,EUR,ca
						""")
				.endsWith("""
						250,ZW,Zimbabwe,Zimbabwe,263,Africa,Harare,"AUD,JPY,GBP,USD,ZAR,BWP,INR,CNY","nd,en,sn"
						""");
	}

	/**
	 * Setting {@link StreamBuilder#writeOnly()} allows support for read-only bean.
	 */
	@Test
	void fromReadOnlyWithBuilder() {
		StreamFactory factory = StreamFactory.newInstance();
		factory.define(streamBuilder(new RecordBuilder(RECORD_NAME).type(CountryReadOnly.class), FIELDS,
				(f, e) -> f).writeOnly());

		CharArrayWriter writer = new CharArrayWriter();
		try (BeanWriter out = factory.createWriter(STREAM_NAME, writer)) {
			out.write(CountriesRaw.from(CountryReadOnly.to(), 0));
			out.flush();
		}
		assertThat(writer.toString()).contains("1,AD,Andorra,Andorra,376,Europe,Andorra la Vella,EUR,ca\n");
	}


	/**
	 * Setting {@link StreamBuilder#writeOnly()} allows support for java {@link Record}.
	 */
	@Test
	void fromRecordWithBuilder() {
		StreamFactory factory = StreamFactory.newInstance();
		factory.define(streamBuilder(new RecordBuilder(RECORD_NAME).type(CountryRecord.class), FIELDS,
				(f, e) -> f.getter(e.getValue())).writeOnly());

		CharArrayWriter writer = new CharArrayWriter();
		try (BeanWriter out = factory.createWriter(STREAM_NAME, writer)) {
			out.write(CountriesRaw.from(CountryRecord.to(), 0));
			out.flush();
		}
		assertThat(writer.toString()).contains("1,AD,Andorra,Andorra,376,Europe,Andorra la Vella,EUR,ca\n");
	}

	/**
	 * Setting {@link StreamBuilder#writeOnly()} allows support for {@link lombok.Value}.
	 */
	@Test
	void fromValueWithBuilder() {
		StreamFactory factory = StreamFactory.newInstance();
		factory.define(streamBuilder(new RecordBuilder(RECORD_NAME).type(CountryValue.class), FIELDS,
				(f, e) -> f).writeOnly());

		CharArrayWriter writer = new CharArrayWriter();
		try (BeanWriter out = factory.createWriter(STREAM_NAME, writer)) {
			out.write(CountriesRaw.from(CountryValue.to(), 0));
			out.flush();
		}
		assertThat(writer.toString()).contains("1,AD,Andorra,Andorra,376,Europe,Andorra la Vella,EUR,ca\n");
	}

	@Test
	@Disabled("Annotation parser incorrectly types collection fields")
	void fromAnnotatedWithBuilder() {
		StreamFactory factory = StreamFactory.newInstance();
		factory.define(new StreamBuilder(STREAM_NAME).format(FORMAT_CSV)
				.addRecord(CountryAnnotated.class)
				.addTypeHandler("phoneHandler", new ListOfIntTypeHandler())
				.addTypeHandler("currencyHandler", new SetOfStringTypeHandler())
				.addTypeHandler("languageHandler", new SetOfStringTypeHandler()));

		CharArrayWriter writer = new CharArrayWriter();
		try (BeanWriter out = factory.createWriter(STREAM_NAME, writer)) {
			out.write(CountriesRaw.from(CountryAnnotated.to(), 0));
			out.flush();
		}
		assertThat(writer.toString()).contains("1,AD,Andorra,Andorra,376,Europe,Andorra la Vella,EUR,ca\n");
	}

}