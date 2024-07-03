package uk.co.bluegecko.common.model.invoice;

import java.math.BigDecimal;
import java.time.Clock;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Locale;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.stream.IntStream;
import javax.money.Monetary;
import lombok.NonNull;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import net.datafaker.Faker;
import uk.co.bluegecko.common.model.AbstractFixture;
import uk.co.bluegecko.common.model.invoice.Address.AddressBuilder;
import uk.co.bluegecko.common.model.invoice.Invoice.InvoiceBuilder;
import uk.co.bluegecko.common.model.invoice.Item.ItemBuilder;
import uk.co.bluegecko.common.model.invoice.Line.LineBuilder;

@UtilityClass
@Slf4j
public class InvoiceFixture extends AbstractFixture {

	AtomicLong invoiceId = new AtomicLong(1);
	AtomicInteger itemId = new AtomicInteger(1);

	@SafeVarargs
	public InvoiceBuilder invoice(Clock clock, AddressBuilder customer, AddressBuilder delivery,
			Collection<LineBuilder> lines, Consumer<InvoiceBuilder>... adjusters) {
		InvoiceBuilder builder = Invoice.builder().number(invoiceId.getAndIncrement())
				.clock(clock)
				.customer(customer.build())
				.delivery(delivery == null ? null : delivery.build());

		lines.forEach(l -> builder.line(l.build()));
		adjust(adjusters, builder);

		return builder;
	}

	@SafeVarargs
	public InvoiceBuilder invoice(Clock clock, AddressBuilder customer, Collection<LineBuilder> lines,
			Consumer<InvoiceBuilder>... adjusters) {
		return invoice(clock, customer, null, lines, adjusters);
	}

	@SafeVarargs
	public AddressBuilder address(Consumer<AddressBuilder>... adjusters) {
		AddressBuilder builder = Address.builder()
				.name(faker().name().name())
				.building(faker().address().buildingNumber())
				.street(faker().address().streetName())
				.town(faker().address().cityName())
				.postcode(faker().address().postcode());

		adjust(adjusters, builder);

		return builder;
	}

	@SafeVarargs
	public ItemBuilder item(int count, BiConsumer<Integer, ItemBuilder>... adjusters) {
		String equipment = faker().appliance().equipment();
		ItemBuilder builder = Item.builder().identifier(new UUID(count, itemId.getAndIncrement()))
				.shortName(equipment)
				.description(faker().appliance().brand() + " " + equipment)
				.price(new BigDecimal(faker().commerce().price()));

		switch (faker().random().nextInt(10)) {
			case 0, 1 -> builder.customisation("Size", faker().size().adjective());
			case 2 -> builder.customisation("Material", faker().commerce().material());
			case 3 -> builder.customisation("Measure", faker().food().measurement());
			case 4, 5 -> builder.customisation("Size", faker().garmentSize().size());
			case 6 -> builder.customisation("Colour", faker().color().name());
		}

		adjust(count, adjusters, builder);

		return builder;
	}

	@SafeVarargs
	public ItemBuilder item(BiConsumer<Integer, ItemBuilder>... adjusters) {
		return item(0, adjusters);
	}

	@SafeVarargs
	public List<ItemBuilder> items(int times, BiConsumer<Integer, ItemBuilder>... adjusters) {
		return IntStream.range(0, times).boxed().map(i -> item(i, adjusters)).toList();
	}

	@SafeVarargs
	public List<ItemBuilder> items(int min, int max, BiConsumer<Integer, ItemBuilder>... adjusters) {
		return items(faker().number().numberBetween(min, max), adjusters);
	}

	@SafeVarargs
	public Line.LineBuilder line(int count, ItemBuilder item, BiConsumer<Integer, LineBuilder>... adjusters) {
		LineBuilder builder = Line.builder()
				.item(item.build())
				.quantity(faker().number().numberBetween(1, 10))
				.discount(BigDecimal.valueOf(faker().number().numberBetween(0, 50) / 100.0));

		adjust(count, adjusters, builder);

		return builder;
	}

	@SafeVarargs
	public Line.LineBuilder line(ItemBuilder item, BiConsumer<Integer, LineBuilder>... adjusters) {
		return line(0, item, adjusters);
	}

	@SafeVarargs
	public List<LineBuilder> lines(List<ItemBuilder> items, BiConsumer<Integer, LineBuilder>... adjusters) {
		return IntStream.range(0, items.size()).boxed().map(i -> line(i, items.get(i), adjusters)).toList();
	}

	@NonNull
	@SafeVarargs
	public static Consumer<InvoiceBuilder> withDelivery(Consumer<AddressBuilder>... adjusters) {
		return i -> i.delivery(address(adjusters).build());
	}

	@NonNull
	public static Consumer<InvoiceBuilder> withUSD() {
		return i -> i.currency(Monetary.getCurrency("USD"));
	}

	@NonNull
	public static Consumer<AddressBuilder> withCompany() {
		return a -> a.company(faker().company().name());
	}

	@NonNull
	public static Consumer<AddressBuilder> withLocality() {
		return a -> a.locality(faker().address().secondaryAddress());
	}

	@NonNull
	public static Consumer<AddressBuilder> withUSState() {
		return a -> {
			Faker faker = faker(Locale.US);
			String state = faker.address().stateAbbr();
			String zipcode = faker.address().zipCodeByState(state).trim();
			try {
				a.locality(faker.address().countyByZipCode(zipcode) + " County");
			} catch (Exception e) {
				a.locality(null);
			}
			a.state(state);
			a.postcode(zipcode);
			a.country("USA");
		};
	}

	@NonNull
	public static Consumer<AddressBuilder> withState() {
		return a -> a.state(faker().address().state());
	}

	@NonNull
	public static Consumer<AddressBuilder> withCountry() {
		return a -> a.country(faker().address().country());
	}

	private <T> void adjust(Consumer<T>[] adjusters, T builder) {
		Arrays.stream(adjusters).forEach(a -> a.accept(builder));
	}

	private <T> void adjust(int count, BiConsumer<Integer, T>[] adjusters, T builder) {
		Arrays.stream(adjusters).forEach(a -> a.accept(count, builder));
	}
}