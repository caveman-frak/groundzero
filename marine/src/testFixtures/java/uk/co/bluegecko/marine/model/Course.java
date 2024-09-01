package uk.co.bluegecko.marine.model;

import java.util.function.UnaryOperator;

public interface Course {

	UnaryOperator<Trace> next();

}