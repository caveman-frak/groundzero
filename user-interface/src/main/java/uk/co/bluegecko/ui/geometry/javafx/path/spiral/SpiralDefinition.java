package uk.co.bluegecko.ui.geometry.javafx.path.spiral;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;
import javafx.geometry.Point2D;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import uk.co.bluegecko.ui.geometry.javafx.path.spiral.Element.Cubic;
import uk.co.bluegecko.ui.geometry.javafx.path.spiral.Element.Move;

@Builder(toBuilder = true)
@Data
@AllArgsConstructor
public class SpiralDefinition {

	private Point2D center;
	private int radius;
	private int segments;
	private double skew1;
	private double skew2;
	private boolean clockwise;

	public SpiralDefinition() {
		this(new Point2D(400, 400), 100, 6, 1.5, 1.0, true);
	}

	public Stream<Element> generateSpiral() {
		List<Element> elements = new ArrayList<>();
		elements.add(new Move(true, center.getX(), center.getY()));
		for (int i = 1; i <= segments; i++) {
			boolean left = i % 2 == 1 ^ clockwise;
			boolean up = i % 2 == 0;
			int r = radius * i;
			int x = (int) (r / 2.0) * (left ? -1 : 1);
			int x1 = (int) (x * skew1);
			int x2 = (int) (x * skew2);
			int y = r * (up ? -1 : 1);

			elements.add(new Cubic(false, x1, 0, x2, y, 0, y));
		}
		return elements.stream();
	}

}