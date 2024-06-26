package uk.co.bluegecko.csv.apache.write;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.CharArrayWriter;
import java.io.IOException;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.junit.jupiter.api.Test;
import uk.co.bluegecko.csv.apache.AbstractApacheCountryTest;
import uk.co.bluegecko.csv.data.model.CountryData;

public class ApacheCsvCountryWriteTest extends AbstractApacheCountryTest {

	@Test
	void fromDataRaw() throws IOException {
		CharArrayWriter writer = new CharArrayWriter();

		CSVPrinter printer = CSVFormat.DEFAULT.builder().build().print(writer);
		printer.printRecord(CountryData.countries(0).toRaw());

		assertThat(writer.toString()).isEqualTo("1,AD,Andorra,Andorra,376,Europe,Andorra la Vella,EUR,ca\r\n");
	}

	@Test
	void fromDataRawWithHeader() throws IOException {
		CharArrayWriter writer = new CharArrayWriter();

		CSVPrinter printer = CSVFormat.DEFAULT.builder()
				.setHeader("Id", "Code", "Name", "Native Name", "Phones", "Continent", "Capital", "Currencies",
						"Languages")
				.build().print(writer);
		printer.printRecord(CountryData.countries(0).toRaw());

		assertThat(writer.toString()).isEqualTo("""
				Id,Code,Name,Native Name,Phones,Continent,Capital,Currencies,Languages\r
				1,AD,Andorra,Andorra,376,Europe,Andorra la Vella,EUR,ca\r
				""");
	}

	@Test
	void fromDataRawWithEnumHeader() throws IOException {
		CharArrayWriter writer = new CharArrayWriter();

		CSVPrinter printer = CSVFormat.DEFAULT.builder()
				.setHeader(CountryData.Fields.class)
				.build().print(writer);
		printer.printRecord(CountryData.countries(0).toRaw());

		assertThat(writer.toString()).isEqualTo("""
				id,code,name,nativeName,phones,continent,capital,currencies,languages\r
				1,AD,Andorra,Andorra,376,Europe,Andorra la Vella,EUR,ca\r
				""");
	}

}