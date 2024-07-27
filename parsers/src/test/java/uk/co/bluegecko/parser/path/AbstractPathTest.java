package uk.co.bluegecko.parser.path;

import org.antlr.v4.runtime.ANTLRErrorListener;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.tree.ParseTreeWalker;
import org.springframework.boot.test.mock.mockito.MockBean;
import uk.co.bluegecko.parser.path.PathParser.PathContext;

public abstract class AbstractPathTest implements PathPrinter {

	@MockBean
	ANTLRErrorListener errorListener;

	protected PathContext parsePath(String content) {
		PathParser parser = new PathParser(
				new CommonTokenStream(
						new PathLexer(CharStreams.fromString(content))));
		parser.addErrorListener(errorListener);
		return parser.path();
	}

	protected void walkPathWith(PathListener listener, String content) {
		new ParseTreeWalker().walk(listener, parsePath(content));
	}

	protected <T> T walkPathWith(PathVisitor<T> visitor, String content) {
		return visitor.visit(parsePath(content));
	}

	protected <T extends ParserRuleContext> T rule(PathContext ctx, Class<? extends T> ctxType, int index) {
		return ctx.segment().getFirst().command().getRuleContext(ctxType, index);
	}

	protected <T extends ParserRuleContext> T rule(PathContext ctx, Class<? extends T> ctxType) {
		return rule(ctx, ctxType, 0);
	}

	@Override
	public String prefix() {
		return String.format("%s: ", getClass().getSimpleName());
	}

}