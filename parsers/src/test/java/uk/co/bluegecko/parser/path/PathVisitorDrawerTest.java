package uk.co.bluegecko.parser.path;

import static java.awt.geom.Arc2D.OPEN;
import static org.assertj.core.api.Assertions.assertThat;

import java.awt.Point;
import java.awt.geom.Arc2D;
import java.awt.geom.CubicCurve2D;
import java.awt.geom.Line2D;
import java.awt.geom.QuadCurve2D;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

@SpringJUnitConfig
class PathVisitorDrawerTest extends AbstractPathTest {

	private PathVisitorDrawer visitor;
	private ShapeComparator shapeComparator;

	@BeforeEach
	void setUp() {
		visitor = new PathVisitorDrawer();
		shapeComparator = new ShapeComparator();
		Assertions.useRepresentation(new ShapeRepresentation());
	}

	@Test
	void parseMoveAbsolute() {
		walkPathWith(visitor, "M10,10\nM10,10");

		assertThat(visitor.getOrigin()).isEqualTo(new Point(10, 10));
		assertThat(visitor.getPosition()).isEqualTo(new Point(10, 10));
		assertThat(visitor.getShapes()).isEmpty();
	}

	@Test
	void parseMoveRelative() {
		walkPathWith(visitor, "M10,10\nm10,10");

		assertThat(visitor.getOrigin()).isEqualTo(new Point(20, 20));
		assertThat(visitor.getPosition()).isEqualTo(new Point(20, 20));
		assertThat(visitor.getShapes()).isEmpty();
	}

	@Test
	void parseCloseNoMove() {
		walkPathWith(visitor, "Z");

		assertThat(visitor.getOrigin()).isEqualTo(new Point(0, 0));
		assertThat(visitor.getPosition()).isEqualTo(new Point(0, 0));
		assertThat(visitor.getShapes()).isEmpty();
	}

	@Test
	void parseCloseWithLines() {
		walkPathWith(visitor, "h10\nv10\nZ");

		assertThat(visitor.getOrigin()).isEqualTo(new Point(0, 0));
		assertThat(visitor.getPosition()).isEqualTo(new Point(0, 0));
		assertThat(visitor.getShapes()).hasSize(3).usingElementComparator(shapeComparator)
				.contains(new Line2D.Double(0, 0, 10, 0),
						new Line2D.Double(10, 0, 10, 10),
						new Line2D.Double(10, 10, 0, 0));
	}

	@Test
	void parseCloseWithMoveAndLines() {
		walkPathWith(visitor, "M10,10\nh10\nv10\nZ");

		assertThat(visitor.getOrigin()).isEqualTo(new Point(10, 10));
		assertThat(visitor.getPosition()).isEqualTo(new Point(10, 10));
		assertThat(visitor.getShapes()).hasSize(3).usingElementComparator(shapeComparator)
				.contains(new Line2D.Double(10, 10, 20, 10),
						new Line2D.Double(20, 10, 20, 20),
						new Line2D.Double(20, 20, 10, 10));
	}

	@Nested
	public class Absolute {

		@Test
		void parseMove() {
			walkPathWith(visitor, "M10,10");

			assertThat(visitor.getOrigin()).isEqualTo(new Point(10, 10));
			assertThat(visitor.getPosition()).isEqualTo(new Point(10, 10));
			assertThat(visitor.getShapes()).isEmpty();
		}

		@Test
		void parseHorizontal() {
			walkPathWith(visitor, "H10");

			assertThat(visitor.getOrigin()).isEqualTo(new Point(0, 0));
			assertThat(visitor.getPosition()).isEqualTo(new Point(10, 0));
			assertThat(visitor.getShapes()).hasSize(1).usingElementComparator(shapeComparator)
					.contains(new Line2D.Double(0, 0, 10, 0));
		}

		@Test
		void parseVertical() {
			walkPathWith(visitor, "V10");

			assertThat(visitor.getOrigin()).isEqualTo(new Point(0, 0));
			assertThat(visitor.getPosition()).isEqualTo(new Point(0, 10));
			assertThat(visitor.getShapes()).hasSize(1).usingElementComparator(shapeComparator)
					.contains(new Line2D.Double(0, 0, 0, 10));
		}

		@Test
		void parseLine() {
			walkPathWith(visitor, "L10,10");

			assertThat(visitor.getOrigin()).isEqualTo(new Point(0, 0));
			assertThat(visitor.getPosition()).isEqualTo(new Point(10, 10));
			assertThat(visitor.getShapes()).hasSize(1).usingElementComparator(shapeComparator)
					.contains(new Line2D.Double(0, 0, 10, 10));
		}

		@Test
		void parseCubicCurve() {
			walkPathWith(visitor, "C5,0 0,5 10,10");

			assertThat(visitor.getOrigin()).isEqualTo(new Point(0, 0));
			assertThat(visitor.getPosition()).isEqualTo(new Point(10, 10));
			assertThat(visitor.getShapes()).hasSize(1).usingElementComparator(shapeComparator)
					.contains(new CubicCurve2D.Double(0, 0, 5, 0, 0, 5, 10, 10));
		}

		@Test
		void parseQuadraticCurve() {
			walkPathWith(visitor, "Q5,0 10,10");

			assertThat(visitor.getOrigin()).isEqualTo(new Point(0, 0));
			assertThat(visitor.getPosition()).isEqualTo(new Point(10, 10));
			assertThat(visitor.getShapes()).hasSize(1).usingElementComparator(shapeComparator)
					.contains(new QuadCurve2D.Double(0, 0, 5, 0, 10, 10));
		}

		@Test
		void parseArc() {
			walkPathWith(visitor, "A5,10 15 10,10 0 1");

			assertThat(visitor.getOrigin()).isEqualTo(new Point(0, 0));
			assertThat(visitor.getPosition()).isEqualTo(new Point(10, 10));
			assertThat(visitor.getShapes()).hasSize(1).usingElementComparator(shapeComparator)
					.contains(new Arc2D.Double(0, 0, 5, 10, 90, 180, OPEN));
		}

	}

	@Nested
	public class Relative {

		@BeforeEach
		void setUpRelative() {
			walkPathWith(visitor, "M10,10");
		}

		@Test
		void parseMoveRelative() {
			walkPathWith(visitor, "m10,10");

			assertThat(visitor.getOrigin()).isEqualTo(new Point(20, 20));
			assertThat(visitor.getPosition()).isEqualTo(new Point(20, 20));
			assertThat(visitor.getShapes()).isEmpty();
		}

		@Test
		void parseHorizontal() {
			walkPathWith(visitor, "h10");

			assertThat(visitor.getOrigin()).isEqualTo(new Point(10, 10));
			assertThat(visitor.getPosition()).isEqualTo(new Point(20, 10));
			assertThat(visitor.getShapes()).hasSize(1).usingElementComparator(shapeComparator)
					.contains(new Line2D.Double(10, 10, 20, 10));
		}

		@Test
		void parseVertical() {
			walkPathWith(visitor, "v10");

			assertThat(visitor.getOrigin()).isEqualTo(new Point(10, 10));
			assertThat(visitor.getPosition()).isEqualTo(new Point(10, 20));
			assertThat(visitor.getShapes()).hasSize(1).usingElementComparator(shapeComparator)
					.contains(new Line2D.Double(10, 10, 10, 20));
		}

		@Test
		void parseLine() {
			walkPathWith(visitor, "l10,10");

			assertThat(visitor.getOrigin()).isEqualTo(new Point(10, 10));
			assertThat(visitor.getPosition()).isEqualTo(new Point(20, 20));
			assertThat(visitor.getShapes()).hasSize(1).usingElementComparator(shapeComparator)
					.contains(new Line2D.Double(10, 10, 20, 20));
		}

		@Test
		void parseCubicCurve() {
			walkPathWith(visitor, "c5,0 0,5 10,10");

			assertThat(visitor.getOrigin()).isEqualTo(new Point(10, 10));
			assertThat(visitor.getPosition()).isEqualTo(new Point(20, 20));
			assertThat(visitor.getShapes()).hasSize(1).usingElementComparator(shapeComparator)
					.contains(new CubicCurve2D.Double(10, 10, 5, 0, 0, 5, 20, 20));
		}

		@Test
		void parseQuadraticCurve() {
			walkPathWith(visitor, "q5,0 10,10");

			assertThat(visitor.getOrigin()).isEqualTo(new Point(10, 10));
			assertThat(visitor.getPosition()).isEqualTo(new Point(20, 20));
			assertThat(visitor.getShapes()).hasSize(1).usingElementComparator(shapeComparator)
					.contains(new QuadCurve2D.Double(10, 10, 5, 0, 20, 20));
		}

		@Test
		void parseArc() {
			walkPathWith(visitor, "a5,10 15 10,10 0 1");

			assertThat(visitor.getOrigin()).isEqualTo(new Point(10, 10));
			assertThat(visitor.getPosition()).isEqualTo(new Point(20, 20));
			assertThat(visitor.getShapes()).hasSize(1).usingElementComparator(shapeComparator)
					.contains(new Arc2D.Double(10, 10, 5, 10, 90, 180, OPEN));
		}

	}

	@Configuration
	public static class Config {

	}

}