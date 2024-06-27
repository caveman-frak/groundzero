package uk.co.bluegecko.utility.bean.model;

import java.util.Map;
import java.util.UUID;
import lombok.Builder;
import lombok.Singular;
import org.javamoney.moneta.Money;

@Builder
public record Item(

		UUID identifier,
		String shortName,
		String description,
		@Singular
		Map<String, String> customisations,
		Money price) {

}