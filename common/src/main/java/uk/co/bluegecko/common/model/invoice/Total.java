package uk.co.bluegecko.common.model.invoice;

import java.math.BigDecimal;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.FieldDefaults;
import org.javamoney.moneta.Money;

@Builder
@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Total {

	private final Invoice parent;

	BigDecimal taxRate;

	public Money total() {
		return parent.getLines().stream().map(Line::total).reduce(Money.zero(parent.getCurrency()), Money::add);
	}

	public Money tax() {
		return total().multiply(taxRate);
	}

	public Money gross() {
		return total().multiply(taxRate.add(BigDecimal.ONE));
	}

}