package uk.co.bluegecko.marine.model.position;

import java.util.function.UnaryOperator;

public interface Course {

	UnaryOperator<Trace> next();

}