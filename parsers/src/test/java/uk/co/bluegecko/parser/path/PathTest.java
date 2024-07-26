package uk.co.bluegecko.parser.path;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

import org.antlr.v4.runtime.InputMismatchException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

@SpringJUnitConfig
public class PathTest extends AbstractPathTest {

	@Test
	void invalidParser() {
		walkPathWith("FOO BAR");

		verify(errorListener).syntaxError(any(), any(), eq(1), eq(7), any(), any(InputMismatchException.class));
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
		PathParser parser = walkPathWith(content);

		System.out.println(parser);

		verify(errorListener, never()).syntaxError(any(), any(), anyInt(), anyInt(), any(), any());
	}

	@Test
	void parserLine() {
		walkPathWith("L 10,10");

		verify(errorListener, never()).syntaxError(any(), any(), anyInt(), anyInt(), any(), any());
	}

	@Test
	void parserHorizontal() {
		walkPathWith("H10");

		verify(errorListener, never()).syntaxError(any(), any(), anyInt(), anyInt(), any(), any());
	}

	@Test
	void parserVertical() {
		walkPathWith("V 10");

		verify(errorListener, never()).syntaxError(any(), any(), anyInt(), anyInt(), any(), any());
	}

	@Test
	void parserCubic() {
		walkPathWith("C 10,10 20,10 20,20");

		verify(errorListener, never()).syntaxError(any(), any(), anyInt(), anyInt(), any(), any());
	}

	@Test
	void parserQuadratic() {
		walkPathWith("Q10,10 15,15");

		verify(errorListener, never()).syntaxError(any(), any(), anyInt(), anyInt(), any(), any());
	}

	@Test
	void parserArc() {
		walkPathWith("A 10,10 20 1 0 40,30");

		verify(errorListener, never()).syntaxError(any(), any(), anyInt(), anyInt(), any(), any());
	}

	@Test
	void parseClose() {
		walkPathWith("Z");

		verify(errorListener, never()).syntaxError(any(), any(), anyInt(), anyInt(), any(), any());
	}

}