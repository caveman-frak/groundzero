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
	void fromDataWithMapperWithBuilder() {
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
	void fromDataWithMapperWithBuilderWithHeader() {
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
	void fromDataWithMapperWithBuilderQuoted() {
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
	void fromDataWithMapperWithBuilderAbbreviated() {
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
	void fromDataWithMapperWithBuilderAllRecordsWithHeader() {
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

}