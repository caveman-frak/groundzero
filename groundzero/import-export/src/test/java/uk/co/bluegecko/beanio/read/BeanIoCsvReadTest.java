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
import org.junit.jupiter.api.Test;
import uk.co.bluegecko.beanio.ToSetTypeHandler;
import uk.co.bluegecko.data.model.CountryData;

public class BeanIoCsvReadTest {

	@Test
	void toDataWithMapping() {
		StreamFactory factory = StreamFactory.newInstance();

		factory.loadResource("mapping/country.xml");
		List<CountryData> countries = new ArrayList<>();
		try (InputStream in = getClass().getClassLoader().getResourceAsStream("countries.csv")) {
			assertThat(in).isNotNull();
			try (BeanReader reader = factory.createReader("countries", new InputStreamReader(in))) {
				reader.skip(1);
				CountryData country;
				while ((country = (CountryData) reader.read()) != null) {
					countries.add(country);
				}
			}
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
		assertThat(countries).hasSize(250).containsAll(CountryData.countries());
	}

	@Test
	void toDataWithBuilder() {
		StreamFactory factory = StreamFactory.newInstance();
		TypeHandler handler = new ToSetTypeHandler();

		factory.define(new StreamBuilder("countries").format("csv").addRecord(
				new RecordBuilder("country").type(CountryData.class)
						.addField(new FieldBuilder("code"))
						.addField(new FieldBuilder("name"))
						.addField(new FieldBuilder("nativeName"))
						.addField(new FieldBuilder("phone"))
						.addField(new FieldBuilder("continent"))
						.addField(new FieldBuilder("capital"))
						.addField(new FieldBuilder("currencies").typeHandler(handler))
						.addField(new FieldBuilder("languages").typeHandler(handler))
		));

		List<CountryData> countries = new ArrayList<>();
		try (InputStream in = getClass().getClassLoader().getResourceAsStream("countries.csv")) {
			assertThat(in).isNotNull();
			try (BeanReader reader = factory.createReader("countries", new InputStreamReader(in))) {
				reader.skip(1);
				CountryData country;
				while ((country = (CountryData) reader.read()) != null) {
					countries.add(country);
				}
			}
		} catch (IOException e) {
			throw new

					RuntimeException(e);
		}

		assertThat(countries).

				hasSize(250).

				containsAll(CountryData.countries());
	}

}