package uk.co.bluegecko.common.model.invoice;

import static org.assertj.core.api.Assertions.assertThat;
import static uk.co.bluegecko.common.model.invoice.InvoiceFixture.address;
import static uk.co.bluegecko.common.model.invoice.InvoiceFixture.currencyUSD;
import static uk.co.bluegecko.common.model.invoice.InvoiceFixture.invoice;
import static uk.co.bluegecko.common.model.invoice.InvoiceFixture.item;
import static uk.co.bluegecko.common.model.invoice.InvoiceFixture.items;
import static uk.co.bluegecko.common.model.invoice.InvoiceFixture.line;
import static uk.co.bluegecko.common.model.invoice.InvoiceFixture.lines;

import java.math.BigDecimal;
import java.time.Clock;
import java.time.Instant;
import java.time.Month;
import java.time.ZoneOffset;
import java.util.List;
import java.util.UUID;
import javax.money.Monetary;
import org.javamoney.moneta.Money;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

@SpringJUnitConfig
class InvoiceFixtureTest {

	public static final int REPEATS = 10;

	@Autowired
	private Clock clock;

	@RepeatedTest(REPEATS)
	void fakedAddress() {
		Address address = address().build();

		assertThat(address.getName()).isNotBlank();
		assertThat(address.getBuilding()).isNotBlank();
		assertThat(address.getStreet()).isNotBlank();
		assertThat(address.getTown()).isNotBlank();
		assertThat(address.getPostcode()).isNotBlank();
	}

	@RepeatedTest(REPEATS)
	void fakeItem() {
		Item item = item(1).build();

		assertThat(item.identifier()).isNotNull().extracting(UUID::getMostSignificantBits).isEqualTo(1L);
		assertThat(item.shortName()).isNotBlank();
		assertThat(item.description()).isNotBlank().contains(item.shortName());
		assertThat(item.price()).isNotNull()
				.isGreaterThan(BigDecimal.ZERO)
				.isLessThan(BigDecimal.valueOf(100));
	}

	@RepeatedTest(REPEATS)
	void fakeLine() {
		Line line = line(0, item(0)).build();

		assertThat(line.getItem()).isNotNull();
		assertThat(line.getDiscount()).isNotNull().isNotNegative().isBetween(BigDecimal.ZERO, BigDecimal.valueOf(0.50));
		assertThat(line.getQuantity()).isPositive().isBetween(1, REPEATS);
		assertThat(line.price()).describedAs("price").isEqualTo(
				line.getItem().price()
						.multiply(BigDecimal.ONE
								.subtract(line.getDiscount())));
		assertThat(line.total()).describedAs("total").isEqualTo(
				line.price().multiply(BigDecimal.valueOf(line.getQuantity())));
	}

	@RepeatedTest(REPEATS)
	void fakeInvoice() {
		Invoice invoice = invoice(clock, address(),
				lines(items(0))).build();

		assertThat(invoice.getCurrency()).isEqualTo(Monetary.getCurrency("GBP"));
		assertThat(invoice.getDate()).hasDayOfMonth(15).hasMonth(Month.JUNE).hasYear(2000);
		assertThat(invoice.getCustomer()).isNotNull();
		assertThat(invoice.getDelivery()).isNull();
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
		assertThat(total).extracting(Total::total).isEqualTo(Money.of(0, "GBP"));
		assertThat(total).extracting(Total::tax).isEqualTo(Money.of(0, "GBP"));
	}

	@RepeatedTest(REPEATS)
	void fakeInvoiceWithLines() {
		Invoice invoice = invoice(clock, address(),
				lines(items(1, REPEATS))).build();
		Total total = invoice.getTotal();

		assertThat(invoice.getLines()).isNotNull().isNotEmpty()
				.hasSizeBetween(1, REPEATS);
		BigDecimal calculated = BigDecimal.ZERO;
		for (Line line : invoice.getLines()) {
			calculated = calculated.add(line.total());
		}
		assertThat(total.total()).isGreaterThan(Money.of(0, "GBP"))
				.isEqualTo(Money.of(calculated, invoice.getCurrency()));
		assertThat(total.tax()).isGreaterThan(Money.of(0, "GBP"))
				.isEqualTo(Money.of(calculated.multiply(new BigDecimal("0.2")), invoice.getCurrency()));
		assertThat(total.gross()).isGreaterThan(Money.of(0, "GBP"))
				.isEqualTo(total.total().add(total.tax()));
	}

	@RepeatedTest(REPEATS)
	void fakeInvoiceInUsdWithLines() {
		Invoice invoice = invoice(clock, address(), lines(items(1, REPEATS)), currencyUSD()).build();
		Total total = invoice.getTotal();

		assertThat(invoice.getCurrency()).isEqualTo(Monetary.getCurrency("USD"));
		assertThat(total.total().getCurrency()).isEqualTo(Monetary.getCurrency("USD"));
		assertThat(total.tax().getCurrency()).isEqualTo(Monetary.getCurrency("USD"));
		assertThat(total.gross().getCurrency()).isEqualTo(Monetary.getCurrency("USD"));
	}

	@TestConfiguration
	static class Configuration {

		@Bean
		public Clock clock() {
			return Clock.fixed(Instant.ofEpochSecond(961072210), ZoneOffset.UTC);
		}

	}

}