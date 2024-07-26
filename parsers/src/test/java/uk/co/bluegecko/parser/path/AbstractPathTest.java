package uk.co.bluegecko.parser.path;

import org.antlr.v4.runtime.ANTLRErrorListener;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTreeWalker;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Configuration;

public class AbstractPathTest {

	@MockBean
	ANTLRErrorListener errorListener;

	protected PathParser walkPathWith(String content) {
		PathParser parser = new PathParser(
				new CommonTokenStream(
						new PathLexer(CharStreams.fromString(content))));
		parser.addErrorListener(errorListener);
		new ParseTreeWalker().walk(
				new PathBaseListener(), parser.path());
		return parser;
	}

	@Configuration
	static class Config {

	}
}