package uk.co.bluegecko.common.model.invoice;

import java.time.Clock;
import java.time.LocalDate;
import java.util.List;
import javax.money.CurrencyUnit;
import javax.money.Monetary;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Builder.Default;
import lombok.Data;
import lombok.Singular;
import lombok.experimental.FieldDefaults;
import org.springframework.lang.Nullable;

@Builder
@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Invoice {

	Long number;
	LocalDate date;

	Address customer;
	@Nullable
	Address delivery;

	@Singular
	List<Line> lines;

	@Default
	CurrencyUnit currency = Monetary.getCurrency("GBP");

	final Total total = new Total(this, TaxRate.STANDARD);

	public static class InvoiceBuilder {

		public InvoiceBuilder date(Clock clock) {
			this.date = LocalDate.now(clock);
			return this;
		}

		public InvoiceBuilder ccyCode(String ccyCode) {
			return currency(Monetary.getCurrency(ccyCode));
		}

	}

}