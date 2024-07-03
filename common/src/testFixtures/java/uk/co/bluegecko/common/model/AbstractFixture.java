package uk.co.bluegecko.common.model;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import net.datafaker.Faker;
import net.datafaker.service.FakeValuesService;
import net.datafaker.service.FakerContext;
import net.datafaker.service.RandomService;

public abstract class AbstractFixture {

	private static final FakeValuesService FAKE_VALUES_SERVICE = new FakeValuesService();
	private static final RandomService RANDOM_SERVICE = new RandomService();
	private static final Map<Locale, Faker> FAKERS_BY_LOCALE = new HashMap<>();

	public static Faker faker(Locale locale) {
		return FAKERS_BY_LOCALE.computeIfAbsent(locale,
				l -> new Faker(FAKE_VALUES_SERVICE, new FakerContext(l, RANDOM_SERVICE)));
	}

	public static Faker faker() {
		return faker(Locale.UK);
	}

}