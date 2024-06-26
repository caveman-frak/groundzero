package uk.co.bluegecko.csv.beanio;

import java.util.Map.Entry;
import java.util.SortedMap;
import java.util.function.BiFunction;
import org.beanio.builder.FieldBuilder;
import org.beanio.builder.RecordBuilder;
import org.beanio.builder.StreamBuilder;
import org.beanio.types.TypeHandler;
import uk.co.bluegecko.AbstractCsvCountryTest;
import uk.co.bluegecko.csv.beanio.handler.ListOfIntTypeHandler;
import uk.co.bluegecko.csv.beanio.handler.SetOfStringTypeHandler;

public abstract class AbstractBeanIoCountryTest extends AbstractCsvCountryTest {

	protected static final String FORMAT_CSV = "csv";
	protected static final String MAPPING_FILE = "mapping/country.xml";
	protected static final String STREAM_NAME = "countries";
	protected static final String RECORD_NAME = "country";
	protected static final String RECORD_HEADER = "header";

	protected StreamBuilder streamBuilder(RecordBuilder recordBuilder, SortedMap<Integer, String> fields,
			BiFunction<FieldBuilder, Entry<Integer, String>, FieldBuilder> fieldBuilder) {
		addFields(recordBuilder, fields, fieldBuilder);
		return new StreamBuilder(STREAM_NAME).format(FORMAT_CSV)
				.addRecord(recordBuilder)
				.addRecord(headerBuilder(fields));
	}

	private RecordBuilder headerBuilder(SortedMap<Integer, String> fields) {
		RecordBuilder recordBuilder = new RecordBuilder(RECORD_HEADER);
		fields.forEach((key, value) -> recordBuilder.addField(new FieldBuilder(value).defaultValue(value)));
		return recordBuilder;
	}

	protected void addFields(RecordBuilder recordBuilder, SortedMap<Integer, String> fields,
			BiFunction<FieldBuilder, Entry<Integer, String>, FieldBuilder> fieldCustomiser) {
		TypeHandler listHandler = new ListOfIntTypeHandler();
		TypeHandler setHandler = new SetOfStringTypeHandler();
		fields.entrySet().forEach(e -> {
			FieldBuilder builder = new FieldBuilder(e.getValue());
			if (LIST_FIELDS.contains(e.getValue())) {
				builder.typeHandler(listHandler);
			} else if (SET_FIELDS.contains(e.getValue())) {
				builder.typeHandler(setHandler);
			}
			recordBuilder.addField(fieldCustomiser.apply(builder, e));
		});
	}
}