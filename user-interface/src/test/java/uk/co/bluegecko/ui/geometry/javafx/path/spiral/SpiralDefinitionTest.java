package uk.co.bluegecko.ui.geometry.javafx.path.spiral;

import static org.assertj.core.api.Assertions.assertThat;
import static uk.co.bluegecko.ui.geometry.javafx.path.spiral.Element.applyTo;
import static uk.co.bluegecko.ui.geometry.javafx.path.spiral.Element.asString;

import javafx.geometry.Point2D;
import javafx.scene.shape.CubicCurveTo;
import javafx.scene.shape.MoveTo;
import javafx.scene.shape.Path;
import javafx.scene.shape.PathElement;
import org.junit.jupiter.api.Test;
import uk.co.bluegecko.ui.geometry.javafx.path.spiral.Element.Cubic;
import uk.co.bluegecko.ui.geometry.javafx.path.spiral.Element.Move;

class SpiralDefinitionTest {

	@Test
	void generateClockwise() {
		SpiralDefinition d = new SpiralDefinition(Point2D.ZERO, 100, 3, 1.5, 1.0, true);
		assertThat(d.generateSpiral().toList())
				.hasSize(4)
				.contains(new Move(true, 0, 0),
						new Cubic(false, 75, 0, 50, 100, 0, 100),
						new Cubic(false, -150, 0, -100, -200, 0, -200),
						new Cubic(false, 225, 0, 150, 300, 0, 300));
	}

	@Test
	void generateAntiClockwise() {
		SpiralDefinition d = new SpiralDefinition(Point2D.ZERO, 100, 3, 1.5, 1.0, false);
		assertThat(d.generateSpiral().toList())
				.hasSize(4)
				.contains(new Move(true, 0, 0),
						new Cubic(false, -75, 0, -50, 100, 0, 100),
						new Cubic(false, 150, 0, 100, -200, 0, -200),
						new Cubic(false, -225, 0, -150, 300, 0, 300));
	}

	@Test
	void generateAsString() {
		SpiralDefinition d = new SpiralDefinition(Point2D.ZERO, 100, 3, 1.5, 1.0, true);
		assertThat(asString(d.generateSpiral()))
				.contains("""
						M 0, 0
						c 75, 0 50, 100 0, 100
						c -150, 0 -100, -200 0, -200
						c 225, 0 150, 300 0, 300""");
	}

	@Test
	void applyToPath() {
		SpiralDefinition d = new SpiralDefinition(Point2D.ZERO, 100, 3, 1.5, 1.0, true);
		Path path = new Path();
		applyTo(path, d.generateSpiral());
		assertThat(path.getElements())
				.hasSize(4)
				.flatExtracting(PathElement::getClass)
				.contains(MoveTo.class, CubicCurveTo.class, CubicCurveTo.class, CubicCurveTo.class);
	}

}