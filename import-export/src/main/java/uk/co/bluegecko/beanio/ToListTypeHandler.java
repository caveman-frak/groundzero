package uk.co.bluegecko.beanio;

import static org.springframework.util.StringUtils.hasText;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import org.beanio.types.TypeHandler;

public class ToListTypeHandler implements TypeHandler {

	@Override
	public Object parse(String text) {
		return hasText(text) ? Arrays.stream(text.split(",")).map(Integer::valueOf).toList() : List.of();
	}

	@Override
	@SuppressWarnings("unchecked")
	public String format(Object value) {
		return value == null ? null : ((List<Integer>) value).stream().map(Object::toString)
				.collect(Collectors.joining(","));
	}

	@Override
	public Class<?> getType() {
		return List.class;
	}

}