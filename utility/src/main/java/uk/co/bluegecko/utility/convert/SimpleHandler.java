package uk.co.bluegecko.utility.convert;

import java.util.Collection;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.springframework.core.convert.ConversionService;
import org.springframework.core.convert.TypeDescriptor;

@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class SimpleHandler<T> implements Handler<T> {

	ConversionService conversionService;
	TypeDescriptor typeDescriptor;

	public SimpleHandler(ConversionService conversionService, TypeDescriptor typeDescriptor) {
		this.conversionService = conversionService;
		this.typeDescriptor = typeDescriptor;
	}

	public SimpleHandler(ConversionService conversionService, Class<T> clazz) {
		this.conversionService = conversionService;
		this.typeDescriptor = TypeDescriptor.valueOf(clazz);
	}

	public <C extends Collection<E>, E> SimpleHandler(ConversionService conversionService,
			Class<C> classCollection, Class<E> classElement) {
		this.conversionService = conversionService;
		this.typeDescriptor = TypeDescriptor.collection(classCollection, TypeDescriptor.valueOf(classElement));
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