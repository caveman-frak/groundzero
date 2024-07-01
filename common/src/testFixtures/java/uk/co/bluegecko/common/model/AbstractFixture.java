package uk.co.bluegecko.common.model;

import java.util.Locale;
import net.datafaker.Faker;
import net.datafaker.service.FakeValuesService;
import net.datafaker.service.FakerContext;
import net.datafaker.service.RandomService;

public abstract class AbstractFixture {

	private static final Faker faker = new Faker(new FakeValuesService(),
			new FakerContext(Locale.UK, new RandomService()));

	public static Faker faker() {
		return faker;
	}

}