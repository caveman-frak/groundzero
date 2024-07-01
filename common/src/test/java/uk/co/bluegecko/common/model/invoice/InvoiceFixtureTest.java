package uk.co.bluegecko.common.model.invoice;

import static org.assertj.core.api.Assertions.assertThat;

import java.math.BigDecimal;
import java.time.Clock;
import java.time.Instant;
import java.time.Month;
import java.time.ZoneOffset;
import java.util.List;
import java.util.UUID;
import java.util.function.Consumer;
import javax.money.Monetary;
import org.javamoney.moneta.Money;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import uk.co.bluegecko.common.model.invoice.Invoice.InvoiceBuilder;

@SpringJUnitConfig
class InvoiceFixtureTest {

	@Autowired
	private Clock clock;

	@RepeatedTest(10)
	void fakedAddress() {
		Address address = InvoiceFixture.address().build();

		assertThat(address.getName()).isNotBlank();
		assertThat(address.getBuilding()).isNotBlank();
		assertThat(address.getStreet()).isNotBlank();
		assertThat(address.getTown()).isNotBlank();
		assertThat(address.getPostcode()).isNotBlank();
	}

	@RepeatedTest(10)
	void fakeItem() {
		Item item = InvoiceFixture.item(1).build();

		System.out.println(item);
		assertThat(item.identifier()).isNotNull().extracting(UUID::getMostSignificantBits).isEqualTo(1L);
		assertThat(item.shortName()).isNotBlank();
		assertThat(item.description()).isNotBlank().contains(item.shortName());
		assertThat(item.price()).isNotNull()
				.isGreaterThan(BigDecimal.ZERO)
				.isLessThan(BigDecimal.valueOf(100));
	}

	@RepeatedTest(10)
	void fakeLine() {
		Line line = InvoiceFixture.line(InvoiceFixture.item(0), 0).build();

		System.out.println(line);
		assertThat(line.getItem()).isNotNull();
		assertThat(line.getDiscount()).isNotNull().isNotNegative().isBetween(BigDecimal.ZERO, BigDecimal.valueOf(0.50));
		assertThat(line.getQuantity()).isPositive().isBetween(1, 10);
		assertThat(line.price()).describedAs("price").isEqualTo(
				line.getItem().price()
						.multiply(BigDecimal.ONE
								.subtract(line.getDiscount())));
		assertThat(line.total()).describedAs("total").isEqualTo(
				line.price().multiply(BigDecimal.valueOf(line.getQuantity())));
	}

	@RepeatedTest(10)
	void fakeInvoice() {
		Invoice invoice = InvoiceFixture.invoice(clock, InvoiceFixture.address(),
				InvoiceFixture.lines(InvoiceFixture.items(0))).build();

		System.out.println(invoice);
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
		Invoice invoice = InvoiceFixture.invoice(clock, InvoiceFixture.address(), List.of()).build();
		Total total = invoice.getTotal();

		System.out.println(total);
		assertThat(total).isNotNull().extracting(Total::getParent).isEqualTo(invoice);
		assertThat(total).extracting(Total::getTaxRate).isEqualTo(TaxRate.STANDARD);
		assertThat(total).extracting(Total::total).isEqualTo(Money.of(0, "GBP"));
		assertThat(total).extracting(Total::tax).isEqualTo(Money.of(0, "GBP"));
	}

	@RepeatedTest(10)
	void fakeInvoiceWithLines() {
		Invoice invoice = InvoiceFixture.invoice(clock, InvoiceFixture.address(),
				InvoiceFixture.lines(InvoiceFixture.items(1, 10))).build();
		Total total = invoice.getTotal();

		System.out.println(invoice);
		assertThat(invoice.getLines()).isNotNull().isNotEmpty()
				.hasSizeBetween(1, 10);
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

	@RepeatedTest(10)
	void fakeInvoiceInUsdWithLines() {
		Invoice invoice = InvoiceFixture.invoice(clock, InvoiceFixture.address(),
				InvoiceFixture.lines(InvoiceFixture.items(1, 10)),
				asUSD()).build();
		Total total = invoice.getTotal();

		assertThat(invoice.getCurrency()).isEqualTo(Monetary.getCurrency("USD"));
		assertThat(total.total().getCurrency()).isEqualTo(Monetary.getCurrency("USD"));
	}

	@NotNull
	private Consumer<InvoiceBuilder> asUSD() {
		return i -> i.currency(Monetary.getCurrency("USD"));
	}

	@TestConfiguration
	static class Configuration {

		@Bean
		public Clock clock() {
			return Clock.fixed(Instant.ofEpochSecond(961072210), ZoneOffset.UTC);
		}
	}

}