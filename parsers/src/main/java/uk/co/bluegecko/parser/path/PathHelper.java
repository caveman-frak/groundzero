package uk.co.bluegecko.parser.path;

import java.awt.Point;
import java.nio.charset.StandardCharsets;
import org.antlr.v4.runtime.tree.TerminalNode;

public interface PathHelper {

	default boolean isRelative(TerminalNode node) {
		return Character.isLowerCase(node.getText().getBytes(StandardCharsets.UTF_8)[0]);
	}

	default Point offset(Point start, Point offset) {
		Point location = start.getLocation();
		location.translate(offset.x, offset.y);
		return location;
	}

	default Point point(String text) {
		String[] parts = text.split(",");
		return new Point(Integer.parseInt(parts[0].trim()), Integer.parseInt(parts[1].trim()));
	}


}