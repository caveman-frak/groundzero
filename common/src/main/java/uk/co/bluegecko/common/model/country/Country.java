package uk.co.bluegecko.common.model.country;

import static org.springframework.util.ObjectUtils.isEmpty;
import static org.springframework.util.StringUtils.hasText;
import static uk.co.bluegecko.common.model.country.Country.fromList;
import static uk.co.bluegecko.common.model.country.Country.fromSet;

import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public interface Country {

	int id();

	String code();

	String name();

	String nativeName();

	List<Integer> phones();

	String continent();

	String capital();

	Set<String> currencies();

	Set<String> languages();

	default Object[] toRaw() {
		return new Object[]{id(), code(), name(), nativeName(), fromList(phones()), continent(),
				capital(), fromSet(currencies()), fromSet(languages())};
	}

	static int integer(String value) {
		return hasText(value) ? Integer.parseInt(value) : 0;
	}

	static Set<String> toSet(String value) {
		return hasText(value) ? Arrays.stream(value.split(",")).collect(Collectors.toSet()) : Set.of();
	}

	static String fromSet(Set<String> values) {
		return isEmpty(values) ? "" : String.join(",", values);
	}

	static List<Integer> toList(String value) {
		return hasText(value) ? Arrays.stream(value.split(",")).map(Integer::valueOf).toList() : List.of();
	}

	static String fromList(List<Integer> values) {
		return isEmpty(values) ? "" : values.stream().map(String::valueOf).collect(Collectors.joining(","));
	}

	interface Bean extends Country {

		int getId();

		String getCode();

		String getName();

		String getNativeName();

		List<Integer> getPhones();

		String getContinent();

		String getCapital();

		Set<String> getCurrencies();

		Set<String> getLanguages();

		@Override
		default int id() {
			return getId();
		}

		@Override
		default String code() {
			return getCode();
		}

		@Override
		default String name() {
			return getName();
		}

		@Override
		default String nativeName() {
			return getNativeName();
		}

		@Override
		default List<Integer> phones() {
			return getPhones();
		}

		@Override
		default String continent() {
			return getContinent();
		}

		@Override
		default String capital() {
			return getCapital();
		}

		@Override
		default Set<String> currencies() {
			return getCurrencies();
		}

		@Override
		default Set<String> languages() {
			return getLanguages();
		}

	}
}