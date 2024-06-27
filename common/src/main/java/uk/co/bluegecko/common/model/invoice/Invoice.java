package uk.co.bluegecko.common.model.invoice;

import java.time.Clock;
import java.time.LocalDate;
import java.util.List;
import javax.money.CurrencyUnit;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.Singular;
import lombok.experimental.FieldDefaults;
import org.springframework.lang.Nullable;

@Builder
@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Invoice {

	Integer number;
	LocalDate date;

	Address customer;
	@Nullable
	Address delivery;

	@Singular
	List<Line> lines;

	CurrencyUnit currency;
	Total total;

	public static class InvoiceBuilder {

		public InvoiceBuilder date(Clock clock) {
			this.date = LocalDate.now(clock);
			return this;
		}

	}

}