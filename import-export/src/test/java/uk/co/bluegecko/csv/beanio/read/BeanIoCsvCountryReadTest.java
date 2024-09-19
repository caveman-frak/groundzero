package uk.co.bluegecko.csv.beanio.read;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.fail;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Consumer;
import org.beanio.BeanReader;
import org.beanio.BeanReaderErrorHandler;
import org.beanio.BeanReaderException;
import org.beanio.InvalidRecordException;
import org.beanio.StreamFactory;
import org.beanio.builder.CsvParserBuilder;
import org.beanio.builder.FieldBuilder;
import org.beanio.builder.RecordBuilder;
import org.beanio.builder.StreamBuilder;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import uk.co.bluegecko.common.model.country.Country;
import uk.co.bluegecko.common.model.country.CountryData;
import uk.co.bluegecko.common.model.country.CountryReadOnly;
import uk.co.bluegecko.common.model.country.CountryRecord;
import uk.co.bluegecko.common.model.country.CountryValue;
import uk.co.bluegecko.csv.beanio.AbstractBeanIoCountryTest;
import uk.co.bluegecko.csv.beanio.handler.ListOfIntTypeHandler;
import uk.co.bluegecko.csv.beanio.handler.SetOfStringTypeHandler;
import uk.co.bluegecko.csv.beanio.model.CountryAnnotated;
import uk.co.bluegecko.csv.data.fixture.CountriesRaw;

public class BeanIoCsvCountryReadTest extends AbstractBeanIoCountryTest {

	@Test
	void toCountryRawToMap() throws IOException {
		StreamFactory factory = StreamFactory.newInstance();

		assertThat(readRawFromCsv(factory, HashMap.class))
				.hasSize(250)
				.contains(new HashMap<>(Map.of(
						"capital", "Andorra la Vella", "code", "AD", "continent", "Europe",
						"currencies", "EUR", "id", "1", "languages", "ca",
						"name", "Andorra", "nativeName", "Andorra", "phones", "376")));
	}

	@Test
	void toCountryRawToList() throws IOException {
		StreamFactory factory = StreamFactory.newInstance();

		assertThat(readRawFromCsv(factory, ArrayList.class))
				.contains(new ArrayList<>(List.of(
						"1", "AD", "Andorra", "Andorra", "376", "Europe", "Andorra la Vella", "EUR", "ca")));
	}

	@Test
	void toDataWithMapping() {
		StreamFactory factory = StreamFactory.newInstance();
		// load the mapping file
		factory.loadResource(MAPPING_FILE);

		assertThat(readCountriesFromCsv(factory, FILENAME))
				.hasSize(250)
				.containsAll(CountriesRaw.from(CountryData.to()));
	}

	@Test
	void toDataWithBuilder() {
		StreamFactory factory = StreamFactory.newInstance();
		// create a stream builder to define the record and fields
		factory.define(streamBuilder(new RecordBuilder(RECORD_NAME).type(CountryData.class),
				FIELDS, (f, _) -> f));

		assertThat(readCountriesFromCsv(factory, FILENAME))
				.hasSize(250)
				.containsAll(CountriesRaw.from(CountryData.to()));
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
				.containsAll(CountriesRaw.from(CountryReadOnly.to()));
	}

	/**
	 * For Java {@link Record} we need to specify the field setter as #N (1 based) to represent the constructor
	 * parameter index and explicitly define the getter (as it does not correspond to the bean specification.
	 */
	@Test
	void toRecordWithBuilder() {
		StreamFactory factory = StreamFactory.newInstance();
		factory.define(streamBuilder(new RecordBuilder(RECORD_NAME).type(CountryRecord.class),
				FIELDS, (f, e) -> f.setter("#" + e.getKey()).getter(e.getValue())));

		assertThat(readCountriesFromCsv(factory, FILENAME))
				.hasSize(250)
				.containsAll(CountriesRaw.from(CountryRecord.to()));
	}

	/**
	 * Not currently aware of a way to get the bean created using a {@link lombok.Builder}.
	 */
	@Test
	@Disabled("No current support for creating bean with lombok builder / factory method")
	void toValueWithBuilder() {
		StreamFactory factory = StreamFactory.newInstance();
		factory.define(streamBuilder(new RecordBuilder(RECORD_NAME).type(CountryAnnotated.class),
				FIELDS, (f, e) -> f.setter("#" + e.getValue())));

		assertThat(readCountriesFromCsv(factory, FILENAME))
				.hasSize(250)
				.containsAll(CountriesRaw.from(CountryValue.to()));
	}

	/**
	 * Annotated fields incorrectly retrieve type of collections. Fix possible in
	 * {@link org.beanio.internal.config.annotation.AnnotationParser}.
	 * <p>
	 * Update the #updateTypeInfo(TypeInfo, Class, Class) method to control evaluation into parameterized collection
	 * fields.
	 */
	@Test
//	@Disabled("Annotation parser incorrectly types collection fields")
	void toAnnotatedWithBuilder() {
		StreamFactory factory = StreamFactory.newInstance();

		factory.define(new StreamBuilder(STREAM_NAME).format(FORMAT_CSV)
				.addRecord(CountryAnnotated.class)
				.addTypeHandler("phoneHandler", new ListOfIntTypeHandler())
				.addTypeHandler("currencyHandler", new SetOfStringTypeHandler())
				.addTypeHandler("languageHandler", new SetOfStringTypeHandler()));

		assertThat(readCountriesFromCsv(factory, FILENAME))
				.hasSize(250)
				.containsAll(CountriesRaw.from(CountryAnnotated.to()));
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
				FIELDS, (f, _) -> f).parser(
				new CsvParserBuilder().delimiter(';').quote('\'').allowUnquotedWhitespace().enableMultiline()));

		assertThat(readCountriesFromCsv(factory, FILENAME_NONSTANDARD))
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

	/**
	 * Unparseable id field supplied. Examines the information available with the exception.
	 *
	 * @throws Exception from mocked error handler.
	 */
	@Test
	void toDataWithFieldError() throws Exception {
		StreamFactory factory = StreamFactory.newInstance();
		// create a stream builder to define the record and fields
		factory.define(streamBuilder(new RecordBuilder(RECORD_NAME).type(CountryData.class), FIELDS, (f, _) -> f));

		Reader reader = new StringReader("""
				id,code,name,nativeName,phones,continent,capital,currencies,languages
				Foo,"AD","Andorra","Andorra","376","Europe","Andorra la Vella","EUR","ca"
				2,"AE","United Arab Emirates","دولة الإمارات العربية المتحدة","971","Asia","Abu Dhabi","AED","ar"
				""");
		BeanReaderErrorHandler errorHandler = mock(BeanReaderErrorHandler.class);
		doThrow(new RuntimeException()).when(errorHandler).handleError(any(BeanReaderException.class));
		doNothing().when(errorHandler).handleError(any(InvalidRecordException.class));

		assertThat(readCountriesFromCsv(factory, reader, r -> r.setErrorHandler(errorHandler)))
				.hasSize(1);

		ArgumentCaptor<BeanReaderException> captor = ArgumentCaptor.forClass(BeanReaderException.class);
		verify(errorHandler, times(1)).handleError(captor.capture());
		assertThat(captor.getValue())
				.isInstanceOf(InvalidRecordException.class)
				.hasMessage("Invalid 'country' record at line 2")
				.hasNoCause();
		if (captor.getValue() instanceof InvalidRecordException exception) {
			assertThat(exception.getRecordName()).isEqualTo("country");
			assertThat(exception.getRecordContext().getLineNumber()).isEqualTo(2);
			assertThat(exception.getRecordContext().getRecordName()).isEqualTo(RECORD_NAME);
			assertThat(exception.getRecordContext().hasRecordErrors()).isFalse();
			assertThat(exception.getRecordContext().hasFieldErrors()).isTrue();
			assertThat(exception.getRecordContext().getFieldErrors())
					.hasSize(1)
					.containsKey("id")
					.containsValue(List.of("Type conversion error: Invalid Integer value 'Foo'"));
		} else {
			fail("Unexpected exception");
		}
	}

	@SuppressWarnings("unchecked")
	private <T> List<T> readRawFromCsv(StreamFactory factory, Class<? extends T> clazz) throws IOException {
		RecordBuilder recordBuilder = new RecordBuilder(RECORD_NAME).type(clazz);
		FIELDS.forEach((_, v) -> recordBuilder.addField(new FieldBuilder(v)));
		factory.define(new StreamBuilder(STREAM_NAME).format(FORMAT_CSV).addRecord(recordBuilder));

		List<T> countries = new ArrayList<>();
		try (InputStream in = getClass().getClassLoader().getResourceAsStream(FILENAME)) {
			assertThat(in).describedAs("Missing resource for '%s'", FILENAME).isNotNull();
			try (BeanReader beanReader = factory.createReader(STREAM_NAME, new InputStreamReader(in))) {
				beanReader.skip(HEADERS);

				T country;
				while ((country = (T) beanReader.read()) != null) {
					countries.add(country);
				}
			}
		}
		return countries;
	}

	private List<Country> readCountriesFromCsv(StreamFactory factory, String filename) {
		try (InputStream in = getClass().getClassLoader().getResourceAsStream(filename)) {
			assertThat(in).describedAs("Missing resource for '%s'", filename).isNotNull();
			return readCountriesFromCsv(factory, new InputStreamReader(in), _ -> {
			});
		} catch (IOException e) {
			System.err.printf("ERROR: Unable to load '%s' due to %s\n", filename, e.getMessage());
			return List.of();
		}
	}

	private List<Country> readCountriesFromCsv(StreamFactory factory, Reader reader,
			Consumer<BeanReader> augmentReader) {
		List<Country> countries = new ArrayList<>();
		try (BeanReader beanReader = factory.createReader(STREAM_NAME, reader)) {
			augmentReader.accept(beanReader);
			beanReader.skip(HEADERS);

			Country country;
			while ((country = (Country) beanReader.read()) != null) {
				countries.add(country);
			}
		}
		return countries;
	}

}