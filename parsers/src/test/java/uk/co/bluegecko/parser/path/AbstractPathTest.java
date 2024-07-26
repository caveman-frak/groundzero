package uk.co.bluegecko.parser.path;

import org.antlr.v4.runtime.ANTLRErrorListener;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTreeWalker;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Configuration;

public abstract class AbstractPathTest implements PathPrinter {

	@MockBean
	ANTLRErrorListener errorListener;

	protected PathParser walkPathWith(PathListener listener, String content) {
		PathParser parser = new PathParser(
				new CommonTokenStream(
						new PathLexer(CharStreams.fromString(content))));
		parser.addErrorListener(errorListener);
		new ParseTreeWalker().walk(
				listener, parser.path());
		return parser;
	}

	protected String walkPathWith(PathVisitor<String> visitor, String content) {
		PathParser parser = new PathParser(
				new CommonTokenStream(
						new PathLexer(CharStreams.fromString(content))));
		parser.addErrorListener(errorListener);
		return visitor.visit(parser.path());
	}

	@Override
	public String prefix() {
		return getClass().getSimpleName() + ": ";
	}

	@Configuration
	static class Config {

	}
	
}