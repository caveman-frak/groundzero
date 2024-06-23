package uk.co.bluegecko.data.model;

import java.util.List;
import java.util.Set;

public interface Country {

	String code();

	String name();

	String nativeName();

	List<Integer> phones();

	String continent();

	String capital();

	Set<String> currencies();

	Set<String> languages();

	interface Bean extends Country {

		String getCode();

		String getName();

		String getNativeName();

		List<Integer> getPhones();

		String getContinent();

		String getCapital();

		Set<String> getCurrencies();

		Set<String> getLanguages();

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