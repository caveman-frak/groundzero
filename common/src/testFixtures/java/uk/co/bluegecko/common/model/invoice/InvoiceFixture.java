package uk.co.bluegecko.common.model.invoice;

import java.math.BigDecimal;
import java.time.Clock;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.stream.IntStream;
import javax.money.Monetary;
import lombok.experimental.UtilityClass;
import org.javamoney.moneta.Money;
import uk.co.bluegecko.common.model.AbstractFixture;
import uk.co.bluegecko.common.model.invoice.Address.AddressBuilder;
import uk.co.bluegecko.common.model.invoice.Invoice.InvoiceBuilder;
import uk.co.bluegecko.common.model.invoice.Item.ItemBuilder;
import uk.co.bluegecko.common.model.invoice.Line.LineBuilder;

@UtilityClass
public class InvoiceFixture extends AbstractFixture {

	AtomicLong invoiceId = new AtomicLong(1);
	AtomicInteger itemId = new AtomicInteger(1);

	@SafeVarargs
	public InvoiceBuilder invoice(Clock clock, AddressBuilder customer, AddressBuilder delivery, String currency,
			Collection<LineBuilder> lines, Consumer<InvoiceBuilder>... adjusters) {
		InvoiceBuilder builder = Invoice.builder().number(invoiceId.getAndIncrement())
				.date(clock)
				.customer(customer.build())
				.delivery(delivery == null ? null : delivery.build())
				.currency(Monetary.getCurrency(currency));

		lines.forEach(l -> builder.line(l.build()));
		Arrays.stream(adjusters).forEach(a -> a.accept(builder));

		return builder;
	}

	@SafeVarargs
	public InvoiceBuilder invoice(Clock clock, AddressBuilder customer, String currency, Collection<LineBuilder> lines,
			Consumer<InvoiceBuilder>... adjusters) {
		return invoice(clock, customer, null, currency, lines, adjusters);
	}

	@SafeVarargs
	public AddressBuilder address(Consumer<AddressBuilder>... adjusters) {
		AddressBuilder builder = Address.builder()
				.name(faker.name().name())
				.building(faker.address().buildingNumber())
				.street(faker.address().streetAddress())
				.town(faker.address().cityName())
				.postcode(faker.address().postcode());

		Arrays.stream(adjusters).forEach(a -> a.accept(builder));

		return builder;
	}

	@SafeVarargs
	public ItemBuilder item(int i, String currency, BiConsumer<Integer, ItemBuilder>... adjusters) {
		String equipment = faker.appliance().equipment();
		ItemBuilder builder = Item.builder().identifier(new UUID(i, itemId.getAndIncrement()))
				.shortName(equipment)
				.description(faker.appliance().brand() + " " + equipment)
				.price(Money.of(new BigDecimal(faker.commerce().price()), currency));

		switch (randomService.nextInt(10)) {
			case 0, 1 -> builder.customisation("Size", faker.size().adjective());
			case 2 -> builder.customisation("Material", faker.commerce().material());
			case 3 -> builder.customisation("Measure", faker.food().measurement());
			case 4, 5 -> builder.customisation("Size", faker.garmentSize().size());
			case 6 -> builder.customisation("Colour", faker.color().name());
		}

		Arrays.stream(adjusters).forEach(a -> a.accept(i, builder));

		return builder;
	}

	@SafeVarargs
	public List<ItemBuilder> items(int times, String currency, BiConsumer<Integer, ItemBuilder>... adjusters) {
		return IntStream.range(0, times).boxed().map(i -> item(i, currency, adjusters)).toList();
	}

	@SafeVarargs
	public List<ItemBuilder> items(int min, int max, String currency, BiConsumer<Integer, ItemBuilder>... adjusters) {
		return items(randomService.nextInt(min, max), currency, adjusters);
	}

	@SafeVarargs
	public Line.LineBuilder line(ItemBuilder item, int i, BiConsumer<Integer, LineBuilder>... adjusters) {
		LineBuilder builder = Line.builder()
				.item(item.build())
				.quantity(faker.number().numberBetween(1, 10))
				.discount(BigDecimal.valueOf(faker.number().numberBetween(0, 50) / 100.0));

		Arrays.stream(adjusters).forEach(a -> a.accept(i, builder));

		return builder;
	}

	@SafeVarargs
	public List<LineBuilder> lines(List<ItemBuilder> items, BiConsumer<Integer, LineBuilder>... adjusters) {
		return IntStream.range(0, items.size()).boxed().map(i -> line(items.get(i), i, adjusters)).toList();
	}

}