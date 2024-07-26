package uk.co.bluegecko.parser.path;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;

import java.awt.Graphics;
import java.awt.Point;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

@SpringJUnitConfig
class PathListenerDrawerTest extends AbstractPathTest {

	@MockBean
	Graphics graphics;

	PathListenerDrawer listener;

	@BeforeEach
	void setUp() {
		listener = new PathListenerDrawer(graphics);
	}

	@Test
	void parseMoveAbsolute() {
		walkPathWith(listener, "M10,10\nM10,10");

		assertThat(listener.getOrigin()).isEqualTo(new Point(10, 10));
		assertThat(listener.getPosition()).isEqualTo(new Point(10, 10));
		verifyNoInteractions(graphics);
	}

	@Test
	void parseMoveRelative() {
		walkPathWith(listener, "M10,10\nm10,10");

		assertThat(listener.getOrigin()).isEqualTo(new Point(20, 20));
		assertThat(listener.getPosition()).isEqualTo(new Point(20, 20));
		verifyNoInteractions(graphics);
	}

	@Test
	void parseCloseNoMove() {
		walkPathWith(listener, "Z");

		assertThat(listener.getOrigin()).isEqualTo(new Point(0, 0));
		assertThat(listener.getPosition()).isEqualTo(new Point(0, 0));
		verifyNoInteractions(graphics);
	}

	@Test
	void parseCloseWithLines() {
		walkPathWith(listener, "h10\nv10\nZ");

		assertThat(listener.getOrigin()).isEqualTo(new Point(0, 0));
		assertThat(listener.getPosition()).isEqualTo(new Point(0, 0));
		verify(graphics).drawLine(0, 0, 10, 0);
		verify(graphics).drawLine(10, 0, 10, 10);
		verify(graphics).drawLine(10, 10, 0, 0);
	}

	@Test
	void parseCloseWithMoveAndLines() {
		walkPathWith(listener, "M10,10\nh10\nv10\nZ");

		assertThat(listener.getOrigin()).isEqualTo(new Point(10, 10));
		assertThat(listener.getPosition()).isEqualTo(new Point(10, 10));
		verify(graphics).drawLine(10, 10, 20, 10);
		verify(graphics).drawLine(20, 10, 20, 20);
		verify(graphics).drawLine(20, 20, 10, 10);
	}

	@Nested
	public class Absolute {

		@Test
		void parseMove() {
			walkPathWith(listener, "M10,10");

			assertThat(listener.getOrigin()).isEqualTo(new Point(10, 10));
			assertThat(listener.getPosition()).isEqualTo(new Point(10, 10));
			verifyNoInteractions(graphics);
		}


		@Test
		void parseHorizontal() {
			walkPathWith(listener, "H10");

			assertThat(listener.getOrigin()).isEqualTo(new Point(0, 0));
			assertThat(listener.getPosition()).isEqualTo(new Point(10, 0));
			verify(graphics).drawLine(0, 0, 10, 0);
		}

		@Test
		void parseVertical() {
			walkPathWith(listener, "V10");

			assertThat(listener.getOrigin()).isEqualTo(new Point(0, 0));
			assertThat(listener.getPosition()).isEqualTo(new Point(0, 10));
			verify(graphics).drawLine(0, 0, 0, 10);
		}

		@Test
		void parseLine() {
			walkPathWith(listener, "L10,10");

			assertThat(listener.getOrigin()).isEqualTo(new Point(0, 0));
			assertThat(listener.getPosition()).isEqualTo(new Point(10, 10));
			verify(graphics).drawLine(0, 0, 10, 10);
		}

		@Test
		void parseCubicCurve() {
			walkPathWith(listener, "C5,0 0,5 10,10");

			assertThat(listener.getOrigin()).isEqualTo(new Point(0, 0));
			assertThat(listener.getPosition()).isEqualTo(new Point(10, 10));
			verifyNoInteractions(graphics);
		}

		@Test
		void parseQuadraticCurve() {
			walkPathWith(listener, "Q5,0 10,10");

			assertThat(listener.getOrigin()).isEqualTo(new Point(0, 0));
			assertThat(listener.getPosition()).isEqualTo(new Point(10, 10));
			verifyNoInteractions(graphics);
		}

		@Test
		void parseArc() {
			walkPathWith(listener, "A5,10 15 10,10 0 1");

			assertThat(listener.getOrigin()).isEqualTo(new Point(0, 0));
			assertThat(listener.getPosition()).isEqualTo(new Point(10, 10));
			verify(graphics).drawArc(0, 0, 5, 10, 10, 10);
		}

	}

	@Nested
	public class Relative {

		@BeforeEach
		void setUpRelative() {
			walkPathWith(listener, "M10,10");
		}

		@Test
		void parseMoveRelative() {
			walkPathWith(listener, "m10,10");

			assertThat(listener.getOrigin()).isEqualTo(new Point(20, 20));
			assertThat(listener.getPosition()).isEqualTo(new Point(20, 20));
			verifyNoInteractions(graphics);
		}

		@Test
		void parseHorizontal() {
			walkPathWith(listener, "h10");

			assertThat(listener.getOrigin()).isEqualTo(new Point(10, 10));
			assertThat(listener.getPosition()).isEqualTo(new Point(20, 10));
			verify(graphics).drawLine(10, 10, 20, 10);
		}

		@Test
		void parseVertical() {
			walkPathWith(listener, "v10");

			assertThat(listener.getOrigin()).isEqualTo(new Point(10, 10));
			assertThat(listener.getPosition()).isEqualTo(new Point(10, 20));
			verify(graphics).drawLine(10, 10, 10, 20);
		}

		@Test
		void parseLine() {
			walkPathWith(listener, "l10,10");

			assertThat(listener.getOrigin()).isEqualTo(new Point(10, 10));
			assertThat(listener.getPosition()).isEqualTo(new Point(20, 20));
			verify(graphics).drawLine(10, 10, 20, 20);
		}

		@Test
		void parseCubicCurve() {
			walkPathWith(listener, "c5,0 0,5 10,10");

			assertThat(listener.getOrigin()).isEqualTo(new Point(10, 10));
			assertThat(listener.getPosition()).isEqualTo(new Point(20, 20));
			verifyNoInteractions(graphics);
		}

		@Test
		void parseQuadraticCurve() {
			walkPathWith(listener, "q5,0 10,10");

			assertThat(listener.getOrigin()).isEqualTo(new Point(10, 10));
			assertThat(listener.getPosition()).isEqualTo(new Point(20, 20));
			verifyNoInteractions(graphics);
		}

		@Test
		void parseArc() {
			walkPathWith(listener, "a5,10 15 10,10 0 1");

			assertThat(listener.getOrigin()).isEqualTo(new Point(10, 10));
			assertThat(listener.getPosition()).isEqualTo(new Point(20, 20));
			verify(graphics).drawArc(10, 10, 5, 10, 20, 20);
		}

	}

	@ParameterizedTest
	@ValueSource(strings = {"10,20", " 10, 20 ", "10,\t20", " 10,\t 20\t"})
	void points(String text) {
		assertThat(listener.point(text)).isEqualTo(new Point(10, 20));
	}

	@Configuration
	public static class Config {

	}

}