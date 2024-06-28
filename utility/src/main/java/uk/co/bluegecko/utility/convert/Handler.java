package uk.co.bluegecko.utility.convert;

public interface Handler<T> {

	T parse(String value);

	String format(T value);

	boolean supports(Class<?> clazz);

}