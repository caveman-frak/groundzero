package uk.co.bluegecko.common.model.invoice;

import java.math.BigDecimal;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Builder.Default;
import lombok.Data;
import lombok.experimental.FieldDefaults;
import org.javamoney.moneta.Money;

@Builder
@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Line {

	int quantity;
	Item item;
	@Default
	BigDecimal discount = BigDecimal.ZERO;

	public Money price() {
		return item.price().multiply(BigDecimal.ONE.subtract(discount));
	}

	public Money total() {
		return price().multiply(quantity);
	}

}