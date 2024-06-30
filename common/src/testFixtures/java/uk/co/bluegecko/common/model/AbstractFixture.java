package uk.co.bluegecko.common.model;

import java.util.Locale;
import net.datafaker.Faker;
import net.datafaker.service.FakeValuesService;
import net.datafaker.service.FakerContext;
import net.datafaker.service.RandomService;

public class AbstractFixture {

	protected static final FakeValuesService valuesService = new FakeValuesService();
	protected static final RandomService randomService = new RandomService();
	protected static final FakerContext context = new FakerContext(Locale.UK, randomService);
	protected static final Faker faker = new Faker(valuesService, context);

}