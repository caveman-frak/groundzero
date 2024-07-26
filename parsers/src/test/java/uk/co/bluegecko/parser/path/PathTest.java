package uk.co.bluegecko.parser.path;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.startsWith;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

import org.antlr.v4.runtime.InputMismatchException;
import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.tree.TerminalNode;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import uk.co.bluegecko.parser.path.PathParser.ArcContext;
import uk.co.bluegecko.parser.path.PathParser.CloseContext;
import uk.co.bluegecko.parser.path.PathParser.CubicContext;
import uk.co.bluegecko.parser.path.PathParser.HorizontalContext;
import uk.co.bluegecko.parser.path.PathParser.LineContext;
import uk.co.bluegecko.parser.path.PathParser.MoveContext;
import uk.co.bluegecko.parser.path.PathParser.PathContext;
import uk.co.bluegecko.parser.path.PathParser.QuadraticContext;
import uk.co.bluegecko.parser.path.PathParser.VerticalContext;

@SpringJUnitConfig
public class PathTest extends AbstractPathTest {

	@Test
	void invalidParser() {
		PathContext ctx = parsePath("FOO BAR");

		assertThat(ctx.children).hasSize(7)
				.extracting(c -> ((TerminalNode) c).getSymbol().getType())
				.contains(PathLexer.ANY, PathLexer.ANY, PathLexer.ANY, PathLexer.ANY, PathLexer.ANY, PathLexer.ANY,
						PathLexer.ANY);

		verify(errorListener)
				.syntaxError(any(), any(), eq(1), eq(0), startsWith("mismatched input 'F' expecting "),
						any(InputMismatchException.class));
	}

	@ParameterizedTest
	@ValueSource(strings = {
			// no initial space
			"M10,15", "M10, 15", "M10,\t15",
			// initial space or tab
			"M 10,15", "M  10, 15", "M\t10,\t15",
			// various line endings
			"M10,15\n", "M10,15\r", "M10,15\r\n", "M10,15  \r", "M10,15\r\r"})
	void parserMove(String content) {
		PathContext ctx = parsePath(content);

		verify(errorListener, never()).syntaxError(any(), any(), anyInt(), anyInt(), any(), any());

		assertThat(rule(ctx, MoveContext.class)).isNotNull()
				.extracting(h -> h.destination.getText()).asString().startsWith("10").endsWith("15");
	}

	@Test
	void parserLine() {
		PathContext ctx = parsePath("L 10,15");

		verify(errorListener, never()).syntaxError(any(), any(), anyInt(), anyInt(), any(), any());

		assertThat(rule(ctx, LineContext.class)).isNotNull()
				.extracting(h -> h.destination.getText()).asString().startsWith("10").endsWith("15");
	}

	@Test
	void parserHorizontal() {
		PathContext ctx = parsePath("H10");

		verify(errorListener, never()).syntaxError(any(), any(), anyInt(), anyInt(), any(), any());

		assertThat(rule(ctx, HorizontalContext.class)).isNotNull()
				.extracting(h -> h.distance.getText()).isEqualTo("10");
	}

	@Test
	void parserVertical() {
		PathContext ctx = parsePath("V 10");

		verify(errorListener, never()).syntaxError(any(), any(), anyInt(), anyInt(), any(), any());

		assertThat(rule(ctx, VerticalContext.class)).isNotNull()
				.extracting(h -> h.distance.getText()).isEqualTo("10");
	}

	@Test
	void parserCubic() {
		PathContext ctx = parsePath("C 10,10 20,10 20,20");

		verify(errorListener, never()).syntaxError(any(), any(), anyInt(), anyInt(), any(), any());

		assertThat(rule(ctx, CubicContext.class)).isNotNull()
				.extracting(h -> h.control1.getText()).isEqualTo("10,10");
		assertThat(rule(ctx, CubicContext.class).control2.getText()).isEqualTo("20,10");
		assertThat(rule(ctx, CubicContext.class).destination.getText()).isEqualTo("20,20");
	}

	@Test
	void parserQuadratic() {
		PathContext ctx = parsePath("Q10,10 15,15");

		verify(errorListener, never()).syntaxError(any(), any(), anyInt(), anyInt(), any(), any());

		assertThat(rule(ctx, QuadraticContext.class)).isNotNull()
				.extracting(h -> h.control.getText()).isEqualTo("10,10");
		assertThat(rule(ctx, QuadraticContext.class).destination.getText()).isEqualTo("15,15");
	}

	@Test
	void parserArc() {
		PathContext ctx = parsePath("A 10,10 20 40,30 1 0");

		verify(errorListener, never()).syntaxError(any(), any(), anyInt(), anyInt(), any(), any());

		assertThat(rule(ctx, ArcContext.class)).isNotNull()
				.extracting(h -> h.radius.getText()).isEqualTo("10,10");
		assertThat(rule(ctx, ArcContext.class).rotation.getText()).isEqualTo("20");
		assertThat(rule(ctx, ArcContext.class).destination.getText()).isEqualTo("40,30");
		assertThat(rule(ctx, ArcContext.class).largeArc.getText()).isEqualTo("1");
		assertThat(rule(ctx, ArcContext.class).sweep.getText()).isEqualTo("0");
	}

	@Test
	void parseClose() {
		PathContext ctx = parsePath("Z");

		verify(errorListener, never()).syntaxError(any(), any(), anyInt(), anyInt(), any(), any());

		assertThat(rule(ctx, CloseContext.class)).isNotNull()
				.extracting(ParserRuleContext::getChildCount).isEqualTo(1);
		assertThat(rule(ctx, CloseContext.class).getText()).isEqualTo("Z");
	}

	@Configuration
	public static class Config {

	}

}