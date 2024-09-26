package uk.co.bluegecko.common.utility;

import java.util.Optional;

public interface Mergeable<T> {

	Optional<T> merge(T other);

}