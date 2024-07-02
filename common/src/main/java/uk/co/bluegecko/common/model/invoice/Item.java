package uk.co.bluegecko.common.model.invoice;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import java.math.BigDecimal;
import java.util.Map;
import java.util.UUID;
import lombok.Builder;
import lombok.Singular;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.Range;

@Builder
public record Item(

		@NotNull
		UUID identifier,
		@NotBlank
		@Length(min = 3, max = 30)
		String shortName,
		@NotBlank
		@Length(min = 3, max = 100)
		String description,
		@Singular
		@NotNull
		Map<String, String> customisations,
		@NotNull
		@Positive
		@Range(min = 0, max = 999_999)
		BigDecimal price) {

}