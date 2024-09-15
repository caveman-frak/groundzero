package uk.co.bluegecko.marine.model.travel;

import java.util.function.UnaryOperator;

public interface Course {

	UnaryOperator<Trace> next();

}