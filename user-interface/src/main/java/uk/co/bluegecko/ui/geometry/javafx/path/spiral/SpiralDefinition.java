package uk.co.bluegecko.ui.geometry.javafx.path.spiral;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import javafx.geometry.Point2D;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Builder
@Data
@AllArgsConstructor
public class SpiralDefinition {

	private Point2D center;
	private int radius;
	private int segments;
	private double skew;
	private boolean clockwise;

	public SpiralDefinition() {
		this(new Point2D(400, 400), 100, 6, 1.5, true);
	}

	public Stream<Element> generateSpiral() {
		List<Element> elements = new ArrayList<>();
		elements.add(new Move(center.getX(), center.getY()));
		for (int i = 1; i <= segments; i++) {
			boolean left = i % 2 == 1 ^ clockwise;
			boolean up = i % 2 == 0;
			int r = radius * i;
			int x = (int) (r / 2.0) * (left ? -1 : 1);
			int x1 = (int) (x * skew);
			int y = r * (up ? -1 : 1);

			elements.add(new Cubic(x1, 0, x, y, 0, y));
		}
		return elements.stream();
	}

	public static String asString(Stream<Element> elements) {
		return elements.map(Element::toString).collect(Collectors.joining("\n"));
	}

	public interface Element {

		@Override
		String toString();

	}

	public record Move(double x, double y) implements Element {

		@Override
		public String toString() {
			return String.format("M %1.0f, %1.0f", x, y);
		}

	}

	public record Cubic(double c1x, double c1y, double c2x, double c2y, double dx, double dy) implements Element {

		@Override
		public String toString() {
			return String.format("c %1.0f, %1.0f %1.0f, %1.0f %1.0f, %1.0f", c1x, c1y, c2x, c2y, dx, dy);
		}

	}

}