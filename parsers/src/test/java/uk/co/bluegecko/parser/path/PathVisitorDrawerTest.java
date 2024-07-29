package uk.co.bluegecko.parser.path;

import static java.awt.geom.PathIterator.SEG_CLOSE;
import static java.awt.geom.PathIterator.SEG_LINETO;
import static java.awt.geom.PathIterator.SEG_MOVETO;
import static org.assertj.core.api.Assertions.assertThat;

import java.awt.Point;
import java.awt.Rectangle;
import java.awt.geom.Path2D;
import java.awt.geom.PathIterator;
import java.util.Arrays;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

@SpringJUnitConfig
class PathVisitorDrawerTest extends AbstractPathTest {

	private PathVisitorDrawer visitor;

	@BeforeEach
	void setUp() {
		visitor = new PathVisitorDrawer();
	}

	@Test
	void parseMoveAbsolute() {
		assertThat(walkPathWith(visitor, "M10,10\nM10,10").getCurrentPoint())
				.isEqualTo(new Point(10, 10));
	}

	@Test
	void parseMoveRelative() {
		assertThat(walkPathWith(visitor, "M10,10\nm10,10").getCurrentPoint())
				.isEqualTo(new Point(20, 20));
	}

	@Test
	void parseCloseNoMove() {
		assertThat(walkPathWith(visitor, "Z").getCurrentPoint())
				.isEqualTo(new Point(0, 0));
	}

	@Test
	void parseCloseWithLines() {
		Path2D path = walkPathWith(visitor, "h10\nv10\nZ");
		assertThat(path.getCurrentPoint()).as("point")
				.isEqualTo(new Point(0, 0));
		assertThat(path.getBounds()).as("bounds").isEqualTo(new Rectangle(0, 0, 10, 10));
		assertThat(path.contains(5, 5)).as("contains").isTrue();
	}

	@Test
	void iterateClosedPath() {
		double[] coordinates = new double[]{-1, -1, -1, -1, -1, -1};
		PathIterator iterator = walkPathWith(visitor, "h10\nv10\nZ").getPathIterator(null);

		// initial move
		assertThat(iterator.currentSegment(coordinates)).as("move 0,0").isEqualTo(SEG_MOVETO);
		assertThat(coordinates).as("0").hasSize(6).containsExactly(0, 0, -1, -1, -1, -1);
		iterator.next();
		Arrays.fill(coordinates, -1);
		// horizontal line
		assertThat(iterator.currentSegment(coordinates)).as("horizontal 10").isEqualTo(SEG_LINETO);
		assertThat(coordinates).as("1").hasSize(6).containsExactly(10, 0, -1, -1, -1, -1);
		iterator.next();
		Arrays.fill(coordinates, -1);
		// vertical line
		assertThat(iterator.currentSegment(coordinates)).as("vertical 10").isEqualTo(SEG_LINETO);
		assertThat(coordinates).as("2").hasSize(6).containsExactly(10, 10, -1, -1, -1, -1);
		iterator.next();
		Arrays.fill(coordinates, -1);
		// close
		assertThat(iterator.currentSegment(coordinates)).as("close").isEqualTo(SEG_CLOSE);
		assertThat(coordinates).as("3").hasSize(6).containsExactly(-1, -1, -1, -1, -1, -1);
		iterator.next();
		Arrays.fill(coordinates, -1);
		// close move
		assertThat(iterator.currentSegment(coordinates)).as("return 0, 0").isEqualTo(SEG_MOVETO);
		assertThat(coordinates).as("4").hasSize(6).containsExactly(0, 0, -1, -1, -1, -1);
		assertThat(iterator.isDone()).isTrue();
	}

	@Test
	void parseCloseWithMoveAndLines() {
		assertThat(walkPathWith(visitor, "M10,10\nh10\nv10\nZ").getCurrentPoint())
				.isEqualTo(new Point(10, 10));
	}

	@Nested
	public class Absolute {

		@Test
		void parseMove() {
			assertThat(walkPathWith(visitor, "M10,10").getCurrentPoint())
					.isEqualTo(new Point(10, 10));
		}

		@Test
		void parseHorizontal() {
			assertThat(walkPathWith(visitor, "H10").getCurrentPoint())
					.isEqualTo(new Point(10, 0));
		}

		@Test
		void parseVertical() {
			assertThat(walkPathWith(visitor, "V10").getCurrentPoint())
					.isEqualTo(new Point(0, 10));
		}

		@Test
		void parseLine() {
			assertThat(walkPathWith(visitor, "L10,10").getCurrentPoint())
					.isEqualTo(new Point(10, 10));
		}

		@Test
		void parseCubicCurve() {
			assertThat(walkPathWith(visitor, "C5,0 0,5 10,10").getCurrentPoint())
					.isEqualTo(new Point(10, 10));
		}

		@Test
		void parseQuadraticCurve() {
			assertThat(walkPathWith(visitor, "Q5,0 10,10").getCurrentPoint())
					.isEqualTo(new Point(10, 10));
		}

		@Test
		void parseArc() {
			assertThat(walkPathWith(visitor, "A5,10 15 10,10 0 1").getCurrentPoint())
					.isEqualTo(new Point(0, 0));
		}

	}

	@Nested
	public class Relative {

		@BeforeEach
		void setUpRelative() {
			visitor = new PathVisitorDrawer(10, 10);
		}

		@Test
		void parseMove() {
			assertThat(walkPathWith(visitor, "m10,10").getCurrentPoint())
					.isEqualTo(new Point(20, 20));
		}

		@Test
		void parseHorizontal() {
			assertThat(walkPathWith(visitor, "h10").getCurrentPoint())
					.isEqualTo(new Point(20, 10));
		}

		@Test
		void parseVertical() {
			assertThat(walkPathWith(visitor, "v10").getCurrentPoint())
					.isEqualTo(new Point(10, 20));
		}

		@Test
		void parseLine() {
			assertThat(walkPathWith(visitor, "l10,10").getCurrentPoint())
					.isEqualTo(new Point(20, 20));
		}

		@Test
		void parseCubicCurve() {
			assertThat(walkPathWith(visitor, "c5,0 0,5 10,10").getCurrentPoint())
					.isEqualTo(new Point(20, 20));
		}

		@Test
		void parseQuadraticCurve() {
			assertThat(walkPathWith(visitor, "q5,0 10,10").getCurrentPoint())
					.isEqualTo(new Point(20, 20));
		}

		@Test
		void parseArc() {
			assertThat(walkPathWith(visitor, "a5,10 15 10,10 0 1").getCurrentPoint())
					.isEqualTo(new Point(10, 10));
		}

	}

	@Configuration
	public static class Config {

	}

}