package uk.co.bluegecko.utility.convert;

import org.springframework.core.convert.converter.Converter;
import org.springframework.core.convert.converter.ConverterRegistry;

public interface BiConverter<S, T> {

	Converter<S, T> from();

	Converter<T, S> to();

	default void register(Class<S> fromClass, Class<T> toClass, ConverterRegistry registry) {
		registry.addConverter(fromClass, toClass, from());
		registry.addConverter(toClass, fromClass, to());
	}

	default T from(S value) {
		return from().convert(value);
	}

	default S to(T value) {
		return to().convert(value);
	}

}