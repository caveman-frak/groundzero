package uk.co.bluegecko.ui.geometry.javafx.path.spiral;

import static org.assertj.core.api.Assertions.assertThat;
import static uk.co.bluegecko.ui.geometry.javafx.path.spiral.SpiralDefinition.asString;

import javafx.geometry.Point2D;
import org.junit.jupiter.api.Test;
import uk.co.bluegecko.ui.geometry.javafx.path.spiral.SpiralDefinition.Cubic;
import uk.co.bluegecko.ui.geometry.javafx.path.spiral.SpiralDefinition.Move;

class SpiralDefinitionTest {

	@Test
	void generateClockwise() {
		SpiralDefinition d = new SpiralDefinition(Point2D.ZERO, 100, 3, 1.5, true);
		assertThat(d.generateSpiral().toList())
				.hasSize(4)
				.contains(new Move(0, 0),
						new Cubic(75, 0, 50, 100, 0, 100),
						new Cubic(-150, 0, -100, -200, 0, -200),
						new Cubic(225, 0, 150, 300, 0, 300));
	}

	@Test
	void generateAntiClockwise() {
		SpiralDefinition d = new SpiralDefinition(Point2D.ZERO, 100, 3, 1.5, false);
		assertThat(d.generateSpiral().toList())
				.hasSize(4)
				.contains(new Move(0, 0),
						new Cubic(-75, 0, -50, 100, 0, 100),
						new Cubic(150, 0, 100, -200, 0, -200),
						new Cubic(-225, 0, -150, 300, 0, 300));
	}

	@Test
	void generateAsString() {
		SpiralDefinition d = new SpiralDefinition(Point2D.ZERO, 100, 3, 1.5, true);
		assertThat(asString(d.generateSpiral()))
				.contains("""
						M 0, 0
						c 75, 0 50, 100 0, 100
						c -150, 0 -100, -200 0, -200
						c 225, 0 150, 300 0, 300""");
	}

}