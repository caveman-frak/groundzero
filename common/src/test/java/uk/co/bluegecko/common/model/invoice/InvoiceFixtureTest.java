package uk.co.bluegecko.common.model.invoice;

import static org.assertj.core.api.Assertions.assertThat;
import static uk.co.bluegecko.common.model.invoice.InvoiceFixture.address;
import static uk.co.bluegecko.common.model.invoice.InvoiceFixture.invoice;
import static uk.co.bluegecko.common.model.invoice.InvoiceFixture.item;
import static uk.co.bluegecko.common.model.invoice.InvoiceFixture.items;
import static uk.co.bluegecko.common.model.invoice.InvoiceFixture.line;
import static uk.co.bluegecko.common.model.invoice.InvoiceFixture.lines;
import static uk.co.bluegecko.common.model.invoice.InvoiceFixture.withCompany;
import static uk.co.bluegecko.common.model.invoice.InvoiceFixture.withCountry;
import static uk.co.bluegecko.common.model.invoice.InvoiceFixture.withDelivery;
import static uk.co.bluegecko.common.model.invoice.InvoiceFixture.withLocality;
import static uk.co.bluegecko.common.model.invoice.InvoiceFixture.withState;
import static uk.co.bluegecko.common.model.invoice.InvoiceFixture.withUSD;
import static uk.co.bluegecko.common.model.invoice.InvoiceFixture.withUSState;

import java.math.BigDecimal;
import java.time.Clock;
import java.time.Instant;
import java.time.Month;
import java.time.ZoneOffset;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;
import javax.money.Monetary;
import org.javamoney.moneta.Money;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import uk.co.bluegecko.common.model.invoice.Item.ItemBuilder;

@SpringJUnitConfig
class InvoiceFixtureTest {

	private static final int REPEATS = 10;
	private static final int MAX_LINES = 10;
	private static final String GBP = "GBP";
	private static final String USD = "USD";

	@Autowired
	private Clock clock;

	@RepeatedTest(REPEATS)
	void fakedAddress() {
		Address address = address().build();

		assertThat(address.getName()).isNotBlank();
		assertThat(address.getCompany()).isNull();
		assertThat(address.getBuilding()).isNotBlank();
		assertThat(address.getStreet()).isNotBlank();
		assertThat(address.getLocality()).isNull();
		assertThat(address.getTown()).isNotBlank();
		assertThat(address.getPostcode()).isNotBlank();
		assertThat(address.getCountry()).isNull();
	}

	@RepeatedTest(REPEATS)
	void fakedAddressComplete() {
		Address address = address(withCompany(), withLocality(), withState(), withCountry()).build();

		assertThat(address.getName()).isNotBlank();
		assertThat(address.getCompany()).isNotBlank();
		assertThat(address.getBuilding()).isNotBlank();
		assertThat(address.getStreet()).isNotBlank();
		assertThat(address.getLocality()).isNotBlank();
		assertThat(address.getTown()).isNotBlank();
		assertThat(address.getState()).isNotBlank();
		assertThat(address.getPostcode()).isNotBlank();
		assertThat(address.getCountry()).isNotBlank();
	}

	@RepeatedTest(REPEATS)
	void fakedAddressUSA() {
		Address address = address(withUSState(), a -> a.country("USA")).build();

		System.out.println(address);
		assertThat(address.getName()).isNotBlank();
		assertThat(address.getBuilding()).isNotBlank();
		assertThat(address.getStreet()).isNotBlank();
		assertThat(address.getTown()).isNotBlank();
//		assertThat(address.getLocality()).isNotBlank().endsWith(" County");
		assertThat(address.getState()).isNotBlank().hasSize(2);
		assertThat(address.getPostcode()).isNotBlank().hasSize(5);
		assertThat(address.getCountry()).isEqualTo("USA");
	}

	@RepeatedTest(REPEATS)
	void fakeItem() {
		Item item = item().build();

		assertThat(item.identifier()).isNotNull().extracting(UUID::getMostSignificantBits).isEqualTo(0L);
		assertThat(item.shortName()).isNotBlank();
		assertThat(item.description()).isNotBlank().contains(item.shortName());
		assertThat(item.price()).isNotNull()
				.isGreaterThan(BigDecimal.ZERO)
				.isLessThan(BigDecimal.valueOf(100));
	}

	@RepeatedTest(REPEATS)
	void fakeItemsCheckIdentifier() {
		List<Item> items = items(0, 10).stream().map(ItemBuilder::build).toList();

		AtomicLong counter = new AtomicLong();
		items.forEach(i -> assertThat(i.identifier()).isNotNull()
				.extracting(UUID::getMostSignificantBits).isEqualTo(counter.getAndIncrement()));
	}

	@RepeatedTest(REPEATS)
	void fakeLine() {
		Line line = line(item()).build();

		assertThat(line.getItem()).isNotNull();
		assertThat(line.getDiscount()).isNotNull().isNotNegative().isBetween(BigDecimal.ZERO, BigDecimal.valueOf(0.50));
		assertThat(line.getQuantity()).isPositive().isBetween(1, 10);
		assertThat(line.price()).describedAs("price").isEqualTo(
				line.getItem().price()
						.multiply(BigDecimal.ONE.subtract(line.getDiscount())));
		assertThat(line.total()).describedAs("total").isEqualTo(
				line.price().multiply(BigDecimal.valueOf(line.getQuantity())));
	}

	@RepeatedTest(REPEATS)
	void fakeInvoice() {
		Invoice invoice = invoice(clock, address(),
				lines(items(0))).build();

		assertThat(invoice.getCurrency()).isEqualTo(Monetary.getCurrency(GBP));
		assertThat(invoice.getDate()).hasDayOfMonth(15).hasMonth(Month.JUNE).hasYear(2000);
		assertThat(invoice.getCustomer()).isNotNull();
		assertThat(invoice.getDelivery()).isNull();
		assertThat(invoice.getNumber()).isPositive();
		assertThat(invoice.getLines()).isNotNull().isEmpty();
		assertThat(invoice.getTotal()).isNotNull().extracting(Total::getParent).isEqualTo(invoice);
	}

	@RepeatedTest(REPEATS)
	void fakeInvoiceWithDelivery() {
		Invoice invoice = invoice(clock, address(), lines(items(0)), withDelivery()).build();

		assertThat(invoice.getCurrency()).isEqualTo(Monetary.getCurrency(GBP));
		assertThat(invoice.getDate()).hasDayOfMonth(15).hasMonth(Month.JUNE).hasYear(2000);
		assertThat(invoice.getCustomer()).isNotNull();
		assertThat(invoice.getDelivery()).isNotNull();
		assertThat(invoice.getNumber()).isPositive();
		assertThat(invoice.getLines()).isNotNull().isEmpty();
		assertThat(invoice.getTotal()).isNotNull().extracting(Total::getParent).isEqualTo(invoice);
	}

	@Test
	void fakeTotal() {
		Invoice invoice = invoice(clock, address(), List.of()).build();
		Total total = invoice.getTotal();

		assertThat(total).isNotNull().extracting(Total::getParent).isEqualTo(invoice);
		assertThat(total).extracting(Total::getTaxRate).isEqualTo(TaxRate.STANDARD);
		assertThat(total).extracting(Total::total).isEqualTo(Money.of(0, GBP));
		assertThat(total).extracting(Total::tax).isEqualTo(Money.of(0, GBP));
		assertThat(total).extracting(Total::gross).isEqualTo(Money.of(0, GBP));
	}

	@RepeatedTest(REPEATS)
	void fakeInvoiceWithLines() {
		Invoice invoice = invoice(clock, address(),
				lines(items(1, MAX_LINES))).build();
		Total total = invoice.getTotal();

		assertThat(invoice.getLines()).isNotNull().isNotEmpty()
				.hasSizeBetween(1, MAX_LINES);
		BigDecimal calculated = BigDecimal.ZERO;
		for (Line line : invoice.getLines()) {
			calculated = calculated.add(line.total());
		}
		assertThat(total.total()).describedAs("total")
				.isGreaterThan(Money.of(0, GBP))
				.isEqualTo(Money.of(calculated, invoice.getCurrency()));
		assertThat(total.tax()).describedAs("tax")
				.isGreaterThan(Money.of(0, GBP))
				.isEqualTo(Money.of(calculated.multiply(new BigDecimal("0.2")), invoice.getCurrency()));
		assertThat(total.gross()).describedAs("gross")
				.isGreaterThan(Money.of(0, GBP))
				.isEqualTo(total.total().add(total.tax()));
	}

	@RepeatedTest(REPEATS)
	void fakeInvoiceWithUsd() {
		Invoice invoice = invoice(clock, address(), lines(items(1, MAX_LINES)), withUSD()).build();
		Total total = invoice.getTotal();

		BigDecimal calculated = invoice.getLines().stream().map(Line::total)
				.reduce(BigDecimal.ZERO, BigDecimal::add);
		assertThat(invoice.getCurrency()).isEqualTo(Monetary.getCurrency(USD));
		assertThat(total.total()).describedAs("total")
				.isGreaterThan(Money.of(0, USD))
				.isEqualTo(Money.of(calculated, invoice.getCurrency()));
		assertThat(total.tax()).describedAs("tax")
				.isGreaterThan(Money.of(0, USD))
				.isEqualTo(Money.of(calculated.multiply(new BigDecimal("0.2")), invoice.getCurrency()));
		assertThat(total.gross()).describedAs("gross")
				.isGreaterThan(Money.of(0, USD))
				.isEqualTo(total.total().add(total.tax()));
	}

	@TestConfiguration
	static class Configuration {

		@Bean
		public Clock clock() {
			return Clock.fixed(Instant.ofEpochSecond(961072210), ZoneOffset.UTC);
		}

	}

}