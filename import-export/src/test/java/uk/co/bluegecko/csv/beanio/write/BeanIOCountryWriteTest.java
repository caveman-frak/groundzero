package uk.co.bluegecko.csv.beanio.write;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.CharArrayWriter;
import java.util.Map;
import java.util.TreeMap;
import org.beanio.BeanWriter;
import org.beanio.StreamFactory;
import org.beanio.builder.CsvParserBuilder;
import org.beanio.builder.RecordBuilder;
import org.junit.jupiter.api.Test;
import uk.co.bluegecko.csv.beanio.AbstractBeanIoCountryTest;
import uk.co.bluegecko.csv.data.model.CountryData;
import uk.co.bluegecko.csv.data.model.CountryReadOnly;
import uk.co.bluegecko.csv.data.model.CountryRecord;
import uk.co.bluegecko.csv.data.model.CountryValue;

public class BeanIOCountryWriteTest extends AbstractBeanIoCountryTest {


	@Test
	void fromDataWithMapper() {
		StreamFactory factory = StreamFactory.newInstance();
		factory.loadResource(MAPPING_FILE);

		CharArrayWriter writer = new CharArrayWriter();
		try (BeanWriter out = factory.createWriter(STREAM_NAME, writer)) {
			out.write(CountryData.countries(0));
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
			out.write(CountryData.countries(0));
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
			out.write(CountryData.countries(0));
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
			out.write(CountryData.countries(0));
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
			out.write(CountryData.countries(0));
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
			CountryData.countries().forEach(out::write);
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

	@Test
	void fromReadOnlyWithBuilder() {
		StreamFactory factory = StreamFactory.newInstance();
		factory.define(streamBuilder(new RecordBuilder(RECORD_NAME).type(CountryReadOnly.class), FIELDS,
				(f, e) -> f).writeOnly());

		CharArrayWriter writer = new CharArrayWriter();
		try (BeanWriter out = factory.createWriter(STREAM_NAME, writer)) {
			out.write(CountryReadOnly.countries(0));
			out.flush();
		}
		assertThat(writer.toString()).contains("1,AD,Andorra,Andorra,376,Europe,Andorra la Vella,EUR,ca\n");
	}


	@Test
	void fromRecordWithBuilder() {
		StreamFactory factory = StreamFactory.newInstance();
		factory.define(streamBuilder(new RecordBuilder(RECORD_NAME).type(CountryRecord.class), FIELDS,
				(f, e) -> f.getter(e.getValue())).writeOnly());

		CharArrayWriter writer = new CharArrayWriter();
		try (BeanWriter out = factory.createWriter(STREAM_NAME, writer)) {
			out.write(CountryRecord.countries(0));
			out.flush();
		}
		assertThat(writer.toString()).contains("1,AD,Andorra,Andorra,376,Europe,Andorra la Vella,EUR,ca\n");
	}

	@Test
	void fromValueWithBuilder() {
		StreamFactory factory = StreamFactory.newInstance();
		factory.define(streamBuilder(new RecordBuilder(RECORD_NAME).type(CountryValue.class), FIELDS,
				(f, e) -> f).writeOnly());

		CharArrayWriter writer = new CharArrayWriter();
		try (BeanWriter out = factory.createWriter(STREAM_NAME, writer)) {
			out.write(CountryValue.countries(0));
			out.flush();
		}
		assertThat(writer.toString()).contains("1,AD,Andorra,Andorra,376,Europe,Andorra la Vella,EUR,ca\n");
	}

}