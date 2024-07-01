package uk.co.bluegecko.common.model.invoice;

import java.math.BigDecimal;
import java.util.Map;
import java.util.UUID;
import lombok.Builder;
import lombok.Singular;

@Builder
public record Item(

		UUID identifier,
		String shortName,
		String description,
		@Singular
		Map<String, String> customisations,
		BigDecimal price) {

}