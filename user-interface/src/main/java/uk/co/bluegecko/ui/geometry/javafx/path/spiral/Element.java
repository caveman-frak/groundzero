package uk.co.bluegecko.ui.geometry.javafx.path.spiral;

import java.util.stream.Collectors;
import java.util.stream.Stream;
import javafx.scene.shape.CubicCurveTo;
import javafx.scene.shape.MoveTo;
import javafx.scene.shape.Path;
import javafx.scene.shape.PathElement;

public interface Element {

	static String asString(Stream<Element> elements) {
		return elements.map(Element::toString).collect(Collectors.joining("\n"));
	}

	static Path applyTo(Path path, Stream<Element> elements) {
		elements.map(Element::toElement).forEach(e -> path.getElements().add(e));
		return path;
	}

	boolean absolute();

	@Override
	String toString();

	PathElement toElement();

	record Move(boolean absolute, double x, double y) implements Element {

		@Override
		public String toString() {
			return String.format("%s %1.0f, %1.0f",
					absolute() ? "M" : "m", x, y);
		}

		@Override
		public PathElement toElement() {
			MoveTo moveTo = new MoveTo(x, y);
			moveTo.setAbsolute(absolute());
			return moveTo;
		}

	}

	record Cubic(boolean absolute, double c1x, double c1y, double c2x, double c2y, double dx, double dy) implements
			Element {

		@Override
		public String toString() {
			return String.format("%s %1.0f, %1.0f %1.0f, %1.0f %1.0f, %1.0f",
					absolute ? "C" : "c",
					c1x, c1y, c2x, c2y, dx, dy);
		}

		@Override
		public PathElement toElement() {
			CubicCurveTo cubicCurveTo = new CubicCurveTo(c1x, c1y, c2x, c2y, dx, dy);
			cubicCurveTo.setAbsolute(absolute());
			return cubicCurveTo;
		}

	}

}