package uk.co.bluegecko;

import java.util.Map;
import java.util.Set;
import java.util.SortedMap;
import java.util.TreeMap;

public abstract class AbstractCsvCountryTest {

	protected static final String FILENAME = "countries.csv";
	protected static final String FILENAME_NONSTANDARD = "countries-nonstandard.csv";
	protected static final int HEADERS = 1;
	protected static final SortedMap<Integer, String> FIELDS = new TreeMap<>(Map.of(
			1, "id",
			2, "code",
			3, "name",
			4, "nativeName",
			5, "phones",
			6, "continent",
			7, "capital",
			8, "currencies",
			9, "languages"));
	protected static final Set<String> SET_FIELDS = Set.of("currencies", "languages");
	protected static final Set<String> LIST_FIELDS = Set.of("phones");

}