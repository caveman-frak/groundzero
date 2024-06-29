package uk.co.bluegecko.utility.convert;

import java.util.Collection;
import org.springframework.core.convert.ConversionService;
import org.springframework.core.convert.TypeDescriptor;

public class ConversionHandler<T> implements Handler<T> {

	private final ConversionService conversionService;
	private final TypeDescriptor typeDescriptor;

	public ConversionHandler(ConversionService conversionService, TypeDescriptor typeDescriptor) {
		this.conversionService = conversionService;
		this.typeDescriptor = typeDescriptor;
	}

	public ConversionHandler(ConversionService conversionService, Class<T> clazz) {
		this.conversionService = conversionService;
		this.typeDescriptor = TypeDescriptor.valueOf(clazz);
	}

	public <C extends Collection<E>, E> ConversionHandler(ConversionService conversionService,
			Class<C> collectionClass, Class<E> elementClass) {
		this.conversionService = conversionService;
		this.typeDescriptor = TypeDescriptor.collection(collectionClass, TypeDescriptor.valueOf(elementClass));
	}

	@Override
	@SuppressWarnings("unchecked")
	public T parse(String value) {
		return (T) conversionService.convert(value, TypeDescriptor.valueOf(String.class), typeDescriptor);
	}

	@Override
	public String format(T value) {
		return (String) conversionService.convert(value, typeDescriptor, TypeDescriptor.valueOf(String.class));
	}

	@Override
	public boolean supports(Class<?> clazz) {
		TypeDescriptor stringType = TypeDescriptor.valueOf(String.class);
		return conversionService.canConvert(stringType, typeDescriptor) &&
				conversionService.canConvert(typeDescriptor, stringType);
	}

}