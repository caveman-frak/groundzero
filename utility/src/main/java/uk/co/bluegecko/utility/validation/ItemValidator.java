package uk.co.bluegecko.utility.validation;

import java.util.Set;
import lombok.NonNull;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import uk.co.bluegecko.common.model.invoice.Item;

public class ItemValidator implements Validator {

	private static final Set<String> VALID_CUSTOMISATION = Set.of(
			"Material", "Measure", "Size", "Colour"
	);

	@Override
	public boolean supports(@NonNull Class<?> clazz) {
		return Item.class.equals(clazz);
	}

	@Override
	public void validate(@NonNull Object target, @NonNull Errors errors) {
		if (target instanceof Item item) {
			item.customisations().keySet().stream().filter(k -> !VALID_CUSTOMISATION.contains(k))
					.forEach(k -> errors.rejectValue("customisations", "invalid", new Object[]{k},
							"Invalid customisation type."));
		}
	}

}