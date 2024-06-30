package uk.co.bluegecko.common.model.invoice;

import java.math.BigDecimal;
import lombok.Getter;

@Getter
public enum TaxRate {

	ZERO(0.00),
	REDUCED(0.05),
	STANDARD(0.20);

	private final BigDecimal rate;

	TaxRate(double rate) {
		this.rate = BigDecimal.valueOf(rate);
	}

}