package uk.co.bluegecko.common.model.invoice;

import java.math.BigDecimal;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Builder.Default;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Builder
@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Line {

	int quantity;
	Item item;
	@Default
	BigDecimal discount = BigDecimal.ZERO;

	public BigDecimal price() {
		return item.price().multiply(BigDecimal.ONE.subtract(discount));
	}

	public BigDecimal total() {
		return price().multiply(BigDecimal.valueOf(quantity));
	}

}