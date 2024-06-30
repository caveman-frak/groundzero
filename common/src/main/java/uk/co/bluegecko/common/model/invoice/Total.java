package uk.co.bluegecko.common.model.invoice;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.NonNull;
import lombok.ToString;
import lombok.experimental.FieldDefaults;
import org.javamoney.moneta.Money;

@Builder
@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
@ToString(exclude = "parent")
public class Total {

	private final Invoice parent;

	@NonNull
	TaxRate taxRate;

	public Money total() {
		return parent.getLines().stream().map(Line::total).reduce(Money.zero(parent.getCurrency()), Money::add);
	}

	public Money tax() {
		return total().multiply(taxRate.getRate());
	}

	public Money gross() {
		return total().add(tax());
	}

}