package uk.co.bluegecko.csv.opencsv.write;

import static org.assertj.core.api.Assertions.assertThat;

import com.opencsv.CSVWriterBuilder;
import com.opencsv.ICSVWriter;
import com.opencsv.bean.HeaderColumnNameMappingStrategy;
import com.opencsv.bean.HeaderColumnNameMappingStrategyBuilder;
import com.opencsv.bean.MappingStrategy;
import com.opencsv.bean.StatefulBeanToCsv;
import com.opencsv.bean.StatefulBeanToCsvBuilder;
import java.io.CharArrayWriter;
import java.io.IOException;
import java.io.Writer;
import org.apache.commons.collections4.multimap.ArrayListValuedHashMap;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import uk.co.bluegecko.common.model.country.CountryData;
import uk.co.bluegecko.common.model.country.CountryReadOnly;
import uk.co.bluegecko.common.model.country.CountryRecord;
import uk.co.bluegecko.common.model.country.CountryValue;
import uk.co.bluegecko.csv.data.fixture.CountriesRaw;
import uk.co.bluegecko.csv.opencsv.AbstractOpenCsvCountryTest;
import uk.co.bluegecko.csv.opencsv.model.CountryAnnotated;

public class OpenCsvCountryWriteTest extends AbstractOpenCsvCountryTest {

	@Test
	void fromCountryRawMapping() throws IOException {
		CharArrayWriter writer = new CharArrayWriter();

		try (ICSVWriter csvWriter = new CSVWriterBuilder(writer).build()) {
			csvWriter.writeNext(CountriesRaw.countries()[0], false);
		}
		assertThat(writer.toString()).contains("1,AD,Andorra,Andorra,376,Europe,Andorra la Vella,EUR,ca\n");
	}

	@Test
	void fromCountryRawMappingQuoted() throws IOException {
		Writer writer = new CharArrayWriter();

		try (ICSVWriter csvWriter = new CSVWriterBuilder(writer).build()) {
			csvWriter.writeNext(CountriesRaw.countries()[0], true);
		}
		assertThat(writer.toString()).contains("""
				"1","AD","Andorra","Andorra","376","Europe","Andorra la Vella","EUR","ca"
				""");
	}

	@Test
	void fromDataDefaultBeanMapping() throws Exception {
		try (CharArrayWriter writer = new CharArrayWriter()) {

			StatefulBeanToCsv<CountryData> beanToCsv = new StatefulBeanToCsvBuilder<CountryData>(writer).build();
			beanToCsv.write(CountriesRaw.from(CountryData.to(), 0));

			assertThat(writer.toString()).contains("""
					"CAPITAL","CODE","CONTINENT","CURRENCIES","ID","LANGUAGES","NAME","NATIVENAME","PHONES"
					"Andorra la Vella","AD","Europe","EUR","1","ca","Andorra","Andorra","376"
					""");
		}
	}

	@Test
	void fromDataBeanHeaderMappingAbbreviated() throws Exception {
		try (CharArrayWriter writer = new CharArrayWriter()) {

			MappingStrategy<CountryData> strategy = new HeaderColumnNameMappingStrategyBuilder<CountryData>().build();
			strategy.setType(CountryData.class);
			strategy.ignoreFields(ignoredFields());
			StatefulBeanToCsv<CountryData> beanToCsv = new StatefulBeanToCsvBuilder<CountryData>(writer)
					.withMappingStrategy(strategy).build();
			beanToCsv.write(CountriesRaw.from(CountryData.to(), 0));

			assertThat(writer.toString()).contains("""
					"CAPITAL","CODE","CONTINENT","ID","NAME","NATIVENAME"
					"Andorra la Vella","AD","Europe","1","Andorra","Andorra"
					""");
		}
	}

	@Test
	void fromDataBeanHeaderMappingReordered() throws Exception {
		try (CharArrayWriter writer = new CharArrayWriter()) {

			HeaderColumnNameMappingStrategy<CountryData> strategy = new HeaderColumnNameMappingStrategyBuilder<CountryData>().build();
			strategy.setType(CountryData.class);
			strategy.setColumnOrderOnWrite(columnOrderComparator(FIELDS));
			StatefulBeanToCsv<CountryData> beanToCsv = new StatefulBeanToCsvBuilder<CountryData>(writer)
					.withMappingStrategy(strategy).build();
			beanToCsv.write(CountriesRaw.from(CountryData.to(), 0));

			assertThat(writer.toString()).contains("""
					"ID","CODE","NAME","NATIVENAME","PHONES","CONTINENT","CAPITAL","CURRENCIES","LANGUAGES"
					"1","AD","Andorra","Andorra","376","Europe","Andorra la Vella","EUR","ca"
					""");
		}
	}

	@Test
	@Disabled("Incorrect parsing of collection field input, only accepts first value")
	void fromDataBeanHeaderMappingReorderedAll() throws Exception {
		try (CharArrayWriter writer = new CharArrayWriter()) {

			HeaderColumnNameMappingStrategy<CountryData> strategy = new HeaderColumnNameMappingStrategyBuilder<CountryData>().build();
			strategy.setType(CountryData.class);
			strategy.setColumnOrderOnWrite(columnOrderComparator(FIELDS));
			StatefulBeanToCsv<CountryData> beanToCsv = new StatefulBeanToCsvBuilder<CountryData>(writer)
					.withMappingStrategy(strategy).build();
			beanToCsv.write(CountriesRaw.from(CountryData.to()));

			assertThat(writer.toString()).startsWith("""
							"ID","CODE","NAME","NATIVENAME","PHONES","CONTINENT","CAPITAL","CURRENCIES","LANGUAGES"
							"1","AD","Andorra","Andorra","376","Europe","Andorra la Vella","EUR","ca"
							""")
					.endsWith("""
							250,ZW,Zimbabwe,Zimbabwe,263,Africa,Harare,"AUD,JPY,GBP,USD,ZAR,BWP,INR,CNY","nd,en,sn"
							""");
		}
	}

	@Test
	void fromDataBeanColumnMapping() throws Exception {
		try (CharArrayWriter writer = new CharArrayWriter()) {

			StatefulBeanToCsv<CountryData> beanToCsv = new StatefulBeanToCsvBuilder<CountryData>(writer)
					.withMappingStrategy(mappingStrategy(CountryData.class, new ArrayListValuedHashMap<>()))
					.build();
			beanToCsv.write(CountriesRaw.from(CountryData.to(), 0));

			/* TODO Incorrect parsing of collection field input, only accepts first value */
			assertThat(writer.toString()).contains("""
					"1","AD","Andorra","Andorra","376","Europe","Andorra la Vella","EUR","ca"
					""");
		}
	}

	@Test
	void fromReadOnlyBeanColumnMapping() throws Exception {
		try (CharArrayWriter writer = new CharArrayWriter()) {

			StatefulBeanToCsv<CountryReadOnly> beanToCsv = new StatefulBeanToCsvBuilder<CountryReadOnly>(writer)
					.withMappingStrategy(mappingStrategy(CountryReadOnly.class, new ArrayListValuedHashMap<>()))
					.build();
			beanToCsv.write(CountriesRaw.from(CountryReadOnly.to(), 0));

			/* TODO Incorrect parsing of collection field input, only accepts first value */
			assertThat(writer.toString()).contains("""
					"1","AD","Andorra","Andorra","376","Europe","Andorra la Vella","EUR","ca"
					""");
		}
	}

	@Test
	void fromRecordBeanColumnMapping() throws Exception {
		try (CharArrayWriter writer = new CharArrayWriter()) {

			StatefulBeanToCsv<CountryRecord> beanToCsv = new StatefulBeanToCsvBuilder<CountryRecord>(writer)
					.withMappingStrategy(mappingStrategy(CountryRecord.class, new ArrayListValuedHashMap<>()))
					.build();
			beanToCsv.write(CountriesRaw.from(CountryRecord.to(), 0));

			/* TODO Incorrect parsing of collection field input, only accepts first value */
			assertThat(writer.toString()).contains("""
					"1","AD","Andorra","Andorra","376","Europe","Andorra la Vella","EUR","ca"
					""");
		}
	}

	@Test
	void fromValueBeanColumnMapping() throws Exception {
		try (CharArrayWriter writer = new CharArrayWriter()) {

			StatefulBeanToCsv<CountryValue> beanToCsv = new StatefulBeanToCsvBuilder<CountryValue>(writer)
					.withMappingStrategy(mappingStrategy(CountryValue.class, new ArrayListValuedHashMap<>()))
					.build();
			beanToCsv.write(CountriesRaw.from(CountryValue.to(), 0));

			/* TODO Incorrect parsing of collection field input, only accepts first value */
			assertThat(writer.toString()).contains("""
					"1","AD","Andorra","Andorra","376","Europe","Andorra la Vella","EUR","ca"
					""");
		}
	}

	@Test
	void fromAnnotatedBean() throws Exception {
		try (CharArrayWriter writer = new CharArrayWriter()) {

			StatefulBeanToCsv<CountryAnnotated> beanToCsv = new StatefulBeanToCsvBuilder<CountryAnnotated>(writer)
					.build();
			beanToCsv.write(CountryAnnotated.countries(0));

			assertThat(writer.toString()).contains("""
					"1","AD","Andorra","Andorra","376","Europe","Andorra la Vella","EUR","ca"
					""");
		}
	}

	@Test
	void fromAnnotatedBeanAll() throws Exception {
		try (CharArrayWriter writer = new CharArrayWriter()) {

			StatefulBeanToCsv<CountryAnnotated> beanToCsv = new StatefulBeanToCsvBuilder<CountryAnnotated>(writer)
					.build();
			beanToCsv.write(CountryAnnotated.countries());

			assertThat(writer.toString()).startsWith("""
							"1","AD","Andorra","Andorra","376","Europe","Andorra la Vella","EUR","ca"
							""")
					.endsWith("""
							"250","ZW","Zimbabwe","Zimbabwe","263","Africa","Harare","AUD,JPY,GBP,USD,ZAR,BWP,INR,CNY","nd,en,sn"
							""");
		}
	}

}