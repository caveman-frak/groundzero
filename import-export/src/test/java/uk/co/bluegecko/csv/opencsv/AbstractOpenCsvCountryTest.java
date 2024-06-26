package uk.co.bluegecko.csv.opencsv;

import com.opencsv.CSVReader;
import com.opencsv.bean.ColumnPositionMappingStrategy;
import com.opencsv.bean.ColumnPositionMappingStrategyBuilder;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Map;
import org.apache.commons.collections4.MultiValuedMap;
import org.apache.commons.collections4.multimap.ArrayListValuedHashMap;
import uk.co.bluegecko.AbstractCsvCountryTest;
import uk.co.bluegecko.csv.data.model.CountryData;
import uk.co.bluegecko.csv.data.model.CountryData.Fields;

public abstract class AbstractOpenCsvCountryTest extends AbstractCsvCountryTest {

	protected <T> ColumnPositionMappingStrategy<T> mappingStrategy(Class<T> clazz, CSVReader csvReader,
			MultiValuedMap<Class<?>, Field> ignoredFields) throws IOException {
		ColumnPositionMappingStrategy<T> mappingStrategy = mappingStrategy(clazz, ignoredFields);
		mappingStrategy.captureHeader(csvReader);
		return mappingStrategy;
	}

	protected <T> ColumnPositionMappingStrategy<T> mappingStrategy(Class<T> clazz,
			MultiValuedMap<Class<?>, Field> ignoredFields) {
		ColumnPositionMappingStrategy<T> mappingStrategy = new ColumnPositionMappingStrategyBuilder<T>().build();
		mappingStrategy.setType(clazz);
		mappingStrategy.setColumnMapping(Arrays.stream(Fields.values()).map(Enum::name).toArray(String[]::new));
		mappingStrategy.ignoreFields(ignoredFields);
		return mappingStrategy;
	}

	protected MultiValuedMap<Class<?>, Field> ignoredFields()
			throws NoSuchFieldException {
		ArrayListValuedHashMap<Class<?>, Field> fields = new ArrayListValuedHashMap<>();
		fields.put(CountryData.class, CountryData.class.getDeclaredField("phones"));
		fields.put(CountryData.class, CountryData.class.getDeclaredField("currencies"));
		fields.put(CountryData.class, CountryData.class.getDeclaredField("languages"));
		return fields;
	}

	protected Comparator<String> columnOrderComparator(Map<Integer, String> fields) {
		return (a, b) -> fieldIndex(fields, a).compareTo(fieldIndex(fields, b));
	}

	protected Integer fieldIndex(Map<Integer, String> fields, String field) {
		return fields.entrySet()
				.stream()
				.filter(entry -> field.equalsIgnoreCase(entry.getValue()))
				.map(Map.Entry::getKey).findFirst().orElse(0);
	}

}