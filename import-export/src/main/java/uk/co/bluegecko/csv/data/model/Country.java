package uk.co.bluegecko.csv.data.model;

import static uk.co.bluegecko.csv.data.fixture.CountriesRaw.fromList;
import static uk.co.bluegecko.csv.data.fixture.CountriesRaw.fromSet;

import java.util.List;
import java.util.Set;

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