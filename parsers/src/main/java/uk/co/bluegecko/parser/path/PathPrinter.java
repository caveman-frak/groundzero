package uk.co.bluegecko.parser.path;

import java.util.Objects;
import java.util.stream.Collectors;
import org.antlr.v4.runtime.ParserRuleContext;

public interface PathPrinter {

	default String string(ParserRuleContext ctx) {
		return ctx.children.stream().map(Objects::toString).collect(Collectors.joining(", "));
	}

	default void println(String format, Object... args) {
		System.out.println(prefix() + String.format(format, args));
	}

	String prefix();

}