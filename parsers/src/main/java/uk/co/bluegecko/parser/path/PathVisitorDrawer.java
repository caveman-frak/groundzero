package uk.co.bluegecko.parser.path;

import java.awt.Point;
import java.awt.geom.Path2D;
import java.awt.geom.Path2D.Double;
import java.awt.geom.Point2D;
import uk.co.bluegecko.parser.path.PathParser.ArcContext;
import uk.co.bluegecko.parser.path.PathParser.CloseContext;
import uk.co.bluegecko.parser.path.PathParser.CubicContext;
import uk.co.bluegecko.parser.path.PathParser.HorizontalContext;
import uk.co.bluegecko.parser.path.PathParser.LineContext;
import uk.co.bluegecko.parser.path.PathParser.MoveContext;
import uk.co.bluegecko.parser.path.PathParser.QuadraticContext;
import uk.co.bluegecko.parser.path.PathParser.VerticalContext;

public class PathVisitorDrawer extends PathBaseVisitor<Path2D> implements PathHelper {

	private final Path2D path;

	public PathVisitorDrawer(Path2D path) {
		this.path = path;
	}

	public PathVisitorDrawer(int x, int y) {
		Double path = new Double();
		path.moveTo(x, y);
		this(path);
	}

	public PathVisitorDrawer() {
		this(0, 0);
	}

	@Override
	public Path2D visitMove(MoveContext ctx) {
		Point point = point(ctx.destination.getText());
		Point location = isRelative(ctx.M()) ? offset(current(), point) : point;
		path.moveTo(location.x, location.y);
		return path;
	}

	@Override
	public Path2D visitHorizontal(HorizontalContext ctx) {
		Point position = current();
		if (isRelative(ctx.H())) {
			position.translate(Integer.parseInt(ctx.distance.getText()), 0);
		} else {
			position.setLocation(Integer.parseInt(ctx.distance.getText()), 0);
		}
		path.lineTo(position.x, position.y);
		return path;
	}

	@Override
	public Path2D visitVertical(VerticalContext ctx) {
		Point position = current();
		if (isRelative(ctx.V())) {
			position.translate(0, Integer.parseInt(ctx.distance.getText()));
		} else {
			position.setLocation(0, Integer.parseInt(ctx.distance.getText()));
		}
		path.lineTo(position.x, position.y);
		return path;
	}

	@Override
	public Path2D visitLine(LineContext ctx) {
		Point destination = point(ctx.destination.getText());
		Point location = isRelative(ctx.L()) ? offset(current(), destination) : destination;
		path.lineTo(location.x, location.y);
		return path;
	}

	@Override
	public Path2D visitClose(CloseContext ctx) {
		path.closePath();
		return path;
	}

	@Override
	public Path2D visitArc(ArcContext ctx) {
		return path;
	}

	@Override
	public Path2D visitCubic(CubicContext ctx) {
		Point destination = point(ctx.destination.getText());
		Point location = isRelative(ctx.C()) ? offset(current(), destination) : destination;
		Point control1 = point(ctx.control1.getText());
		Point control2 = point(ctx.control2.getText());
		path.curveTo(control2.x, control1.y, control2.x, control2.y, location.x, location.y);
		return path;
	}

	@Override
	public Path2D visitQuadratic(QuadraticContext ctx) {
		Point point = point(ctx.destination.getText());
		Point location = isRelative(ctx.Q()) ? offset(current(), point) : point;
		Point control = point(ctx.control.getText());
		path.quadTo(control.x, control.y, location.x, location.y);
		return path;
	}

	@Override
	protected Path2D aggregateResult(Path2D aggregate, Path2D nextResult) {
		return aggregate;
	}

	@Override
	protected Path2D defaultResult() {
		return path;
	}

	public Point current() {
		Point2D point = path.getCurrentPoint();
		return new Point((int) point.getX(), (int) point.getY());
	}

}