package uk.co.bluegecko.parser.path;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.startsWith;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

import org.antlr.v4.runtime.InputMismatchException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

@SpringJUnitConfig
public class PathTest extends AbstractPathTest {

	private PathListener listener;

	@BeforeEach
	void setUp() {
		listener = new PathBaseListener();
	}

	@Test
	void invalidParser() {
		walkPathWith(listener, "FOO BAR");

		verify(errorListener)
				.syntaxError(any(), any(), eq(1), eq(0), startsWith("mismatched input 'F' expecting "),
						any(InputMismatchException.class));
	}

	@ParameterizedTest
	@ValueSource(strings = {
			// no initial space
			"M10,10", "M10, 10", "M10,\t10",
			// initial space or tab
			"M 10,10", "M  10, 10", "M\t10,\t10",
			// various line endings
			"M10,10\n", "M10,10\r", "M10,10\r\n", "M10,10  \r", "M10,10\r\r"})
	void parserMove(String content) {
		walkPathWith(listener, content);

		verify(errorListener, never()).syntaxError(any(), any(), anyInt(), anyInt(), any(), any());
	}

	@Test
	void parserLine() {
		walkPathWith(listener, "L 10,10");

		verify(errorListener, never()).syntaxError(any(), any(), anyInt(), anyInt(), any(), any());
	}

	@Test
	void parserHorizontal() {
		walkPathWith(listener, "H10");

		verify(errorListener, never()).syntaxError(any(), any(), anyInt(), anyInt(), any(), any());
	}

	@Test
	void parserVertical() {
		walkPathWith(listener, "V 10");

		verify(errorListener, never()).syntaxError(any(), any(), anyInt(), anyInt(), any(), any());
	}

	@Test
	void parserCubic() {
		walkPathWith(listener, "C 10,10 20,10 20,20");

		verify(errorListener, never()).syntaxError(any(), any(), anyInt(), anyInt(), any(), any());
	}

	@Test
	void parserQuadratic() {
		walkPathWith(listener, "Q10,10 15,15");

		verify(errorListener, never()).syntaxError(any(), any(), anyInt(), anyInt(), any(), any());
	}

	@Test
	void parserArc() {
		walkPathWith(listener, "A 10,10 20 1 0 40,30");

		verify(errorListener, never()).syntaxError(any(), any(), anyInt(), anyInt(), any(), any());
	}

	@Test
	void parseClose() {
		walkPathWith(listener, "Z");

		verify(errorListener, never()).syntaxError(any(), any(), anyInt(), anyInt(), any(), any());
	}

}