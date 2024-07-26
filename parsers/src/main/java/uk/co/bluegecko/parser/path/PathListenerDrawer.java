package uk.co.bluegecko.parser.path;

import java.awt.Graphics;
import java.awt.Point;
import java.nio.charset.StandardCharsets;
import java.text.ParseException;
import java.util.Locale;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.antlr.v4.runtime.tree.TerminalNode;
import uk.co.bluegecko.common.format.PointFormatter;
import uk.co.bluegecko.parser.path.PathParser.ArcContext;
import uk.co.bluegecko.parser.path.PathParser.CloseContext;
import uk.co.bluegecko.parser.path.PathParser.CubicContext;
import uk.co.bluegecko.parser.path.PathParser.HorizontalContext;
import uk.co.bluegecko.parser.path.PathParser.LineContext;
import uk.co.bluegecko.parser.path.PathParser.MoveContext;
import uk.co.bluegecko.parser.path.PathParser.QuadraticContext;
import uk.co.bluegecko.parser.path.PathParser.VerticalContext;

@Slf4j
@Getter
@RequiredArgsConstructor
public class PathListenerDrawer extends PathBaseListener implements PathPrinter {

	private final Graphics graphics;

	private final PointFormatter formatter = new PointFormatter();
	private final Point origin = new Point();
	private final Point position = new Point();

	@Override
	public void enterMove(MoveContext ctx) {
		Point point = point(ctx.destination.getText());
		Point location = isRelative(ctx.M()) ? offset(position, point) : point;
		origin.setLocation(location);
		position.setLocation(location);
	}

	@Override
	public void enterHorizontal(HorizontalContext ctx) {
		Point start = position.getLocation();
		if (isRelative(ctx.H())) {
			position.translate(Integer.parseInt(ctx.distance.getText()), 0);
		} else {
			position.setLocation(Integer.parseInt(ctx.distance.getText()), 0);
		}
		graphics.drawLine(start.x, start.y, position.x, position.y);
	}

	@Override
	public void enterVertical(VerticalContext ctx) {
		Point start = position.getLocation();
		if (isRelative(ctx.V())) {
			position.translate(0, Integer.parseInt(ctx.distance.getText()));
		} else {
			position.setLocation(0, Integer.parseInt(ctx.distance.getText()));
		}
		graphics.drawLine(start.x, start.y, position.x, position.y);
	}

	@Override
	public void enterLine(LineContext ctx) {
		Point start = position.getLocation();
		Point point = point(ctx.destination.getText());
		Point location = isRelative(ctx.L()) ? offset(start, point) : point;
		position.setLocation(location);
		graphics.drawLine(start.x, start.y, position.x, position.y);
	}

	@Override
	public void enterClose(CloseContext ctx) {
		if (!position.equals(origin)) {
			Point start = position.getLocation();
			position.setLocation(origin);
			graphics.drawLine(start.x, start.y, position.x, position.y);
		}
	}

	@Override
	public void enterArc(ArcContext ctx) {
		Point start = position.getLocation();
		Point point = point(ctx.destination.getText());
		Point location = isRelative(ctx.A()) ? offset(start, point) : point;
		Point radius = point(ctx.radius.getText());
		position.setLocation(location);
		graphics.drawArc(start.x, start.y, radius.x, radius.y, location.x, location.y);
	}

	@Override
	public void enterCubic(CubicContext ctx) {
		Point point = point(ctx.destination.getText());
		Point location = isRelative(ctx.C()) ? offset(position, point) : point;
		position.setLocation(location);
	}

	@Override
	public void enterQuadratic(QuadraticContext ctx) {
		Point point = point(ctx.destination.getText());
		Point location = isRelative(ctx.Q()) ? offset(position, point) : point;
		position.setLocation(location);
	}

	@Override
	public String prefix() {
		return "Listener: ";
	}

	protected boolean isRelative(TerminalNode node) {
		return Character.isLowerCase(node.getText().getBytes(StandardCharsets.UTF_8)[0]);
	}

	protected Point offset(Point start, Point offset) {
		Point location = start.getLocation();
		location.translate(offset.x, offset.y);
		return location;
	}

	protected Point point(String text) {
		try {
			String point = String.format("[%s]", text.replaceAll("[ \t]", "")
					.replace(',', ';'));
			return formatter.parse(point, Locale.UK);
		} catch (ParseException e) {
			log.error("Unable to parse point '{}' because {}", text, e.getMessage(), e);
			return position;
		}
	}

}