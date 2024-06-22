package uk.co.bluegecko.beanio;

import static org.springframework.util.StringUtils.hasText;

import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;
import org.beanio.types.TypeHandler;

public class ToSetTypeHandler implements TypeHandler {

	@Override
	public Object parse(String text) {
		return hasText(text) ? Arrays.stream(text.split(",")).collect(Collectors.toSet()) : Set.of();
	}

	@Override
	@SuppressWarnings("unchecked")
	public String format(Object value) {
		return String.join(",", ((Iterable<String>) value));
	}

	@Override
	public Class<?> getType() {
		return Set.class;
	}

}