package uk.co.bluegecko.beanio.read;

import java.util.Map;
import java.util.Set;
import java.util.SortedMap;
import java.util.TreeMap;

public abstract class AbstractReadTest {

	static final String FILENAME = "countries.csv";
	static final int HEADERS = 1;
	static final SortedMap<Integer, String> FIELDS = new TreeMap<>(Map.of(
			1, "code",
			2, "name",
			3, "nativeName",
			4, "phones",
			5, "continent",
			6, "capital",
			7, "currencies",
			8, "languages"));
	static final Set<String> SET_FIELDS = Set.of("currencies", "languages");
	static final Set<String> LIST_FIELDS = Set.of("phones");

}