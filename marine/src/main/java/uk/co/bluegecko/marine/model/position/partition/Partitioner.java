package uk.co.bluegecko.marine.model.position.partition;

import java.util.function.BiFunction;
import uk.co.bluegecko.marine.model.position.Trace;

public interface Partitioner extends BiFunction<Resolution, Trace, Partition<?>> {

}