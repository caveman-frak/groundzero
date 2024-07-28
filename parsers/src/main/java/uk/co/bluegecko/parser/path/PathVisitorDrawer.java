package uk.co.bluegecko.parser.path;

import java.awt.Point;
import java.awt.Shape;
import java.awt.geom.Arc2D;
import java.awt.geom.CubicCurve2D;
import java.awt.geom.Line2D;
import java.awt.geom.Path2D;
import java.awt.geom.QuadCurve2D;
import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import uk.co.bluegecko.parser.path.PathParser.ArcContext;
import uk.co.bluegecko.parser.path.PathParser.CloseContext;
import uk.co.bluegecko.parser.path.PathParser.CommandContext;
import uk.co.bluegecko.parser.path.PathParser.CubicContext;
import uk.co.bluegecko.parser.path.PathParser.HorizontalContext;
import uk.co.bluegecko.parser.path.PathParser.LineContext;
import uk.co.bluegecko.parser.path.PathParser.MoveContext;
import uk.co.bluegecko.parser.path.PathParser.PathContext;
import uk.co.bluegecko.parser.path.PathParser.QuadraticContext;
import uk.co.bluegecko.parser.path.PathParser.SegmentContext;
import uk.co.bluegecko.parser.path.PathParser.VerticalContext;

@Getter
public class PathVisitorDrawer extends PathBaseVisitor<Shape> implements PathHelper {

	private final List<Shape> shapes = new ArrayList<>();
	private final Point origin = new Point();
	private final Point position = new Point();

	@Override
	public Shape visitMove(MoveContext ctx) {
		Point point = point(ctx.destination.getText());
		Point location = isRelative(ctx.M()) ? offset(position, point) : point;
		origin.setLocation(location);
		position.setLocation(location);
		return null;
	}

	@Override
	public Shape visitHorizontal(HorizontalContext ctx) {
		Point start = position.getLocation();
		if (isRelative(ctx.H())) {
			position.translate(Integer.parseInt(ctx.distance.getText()), 0);
		} else {
			position.setLocation(Integer.parseInt(ctx.distance.getText()), 0);
		}
		return add(new Line2D.Double(start.x, start.y, position.x, position.y));
	}

	@Override
	public Shape visitVertical(VerticalContext ctx) {
		Point start = position.getLocation();
		if (isRelative(ctx.V())) {
			position.translate(0, Integer.parseInt(ctx.distance.getText()));
		} else {
			position.setLocation(0, Integer.parseInt(ctx.distance.getText()));
		}
		return add(new Line2D.Double(start.x, start.y, position.x, position.y));
	}

	@Override
	public Shape visitLine(LineContext ctx) {
		Point start = position.getLocation();
		Point point = point(ctx.destination.getText());
		Point location = isRelative(ctx.L()) ? offset(start, point) : point;
		position.setLocation(location);
		return add(new Line2D.Double(start.x, start.y, position.x, position.y));
	}

	@Override
	public Shape visitClose(CloseContext ctx) {
		if (!position.equals(origin)) {
			Point start = position.getLocation();
			position.setLocation(origin);
			return add(new Line2D.Double(start.x, start.y, position.x, position.y));
		}
		return null;
	}

	@Override
	public Shape visitArc(ArcContext ctx) {
		Point start = position.getLocation();
		Point point = point(ctx.destination.getText());
		Point location = isRelative(ctx.A()) ? offset(start, point) : point;
		Point radius = point(ctx.radius.getText());
		position.setLocation(location);
		return add(new Arc2D.Double(start.x, start.y, radius.x, radius.y, 90, 180, Arc2D.OPEN));
	}

	@Override
	public Shape visitCubic(CubicContext ctx) {
		Point start = position.getLocation();
		Point point = point(ctx.destination.getText());
		Point location = isRelative(ctx.C()) ? offset(position, point) : point;
		Point control1 = point(ctx.control1.getText());
		Point control2 = point(ctx.control2.getText());
		position.setLocation(location);
		return add(new CubicCurve2D.Double(start.x, start.y, control1.x, control1.y, control2.x, control2.y,
				position.x, position.y));
	}

	@Override
	public Shape visitQuadratic(QuadraticContext ctx) {
		Point start = position.getLocation();
		Point point = point(ctx.destination.getText());
		Point location = isRelative(ctx.Q()) ? offset(position, point) : point;
		Point control = point(ctx.control.getText());
		position.setLocation(location);
		return add(new QuadCurve2D.Double(start.x, start.y, control.x, control.y, position.x, position.y));
	}

	@Override
	public Shape visitCommand(CommandContext ctx) {
		return super.visitCommand(ctx);
	}

	@Override
	public Shape visitSegment(SegmentContext ctx) {
		return super.visitSegment(ctx);
	}

	@Override
	public Shape visitPath(PathContext ctx) {
		return visitChildren(ctx);
	}

	@Override
	protected Shape aggregateResult(Shape aggregate, Shape nextResult) {
		if (aggregate != null && nextResult != null) {
			Path2D path = aggregate instanceof Path2D p ? p : new Path2D.Double(aggregate);
			path.append(nextResult, true);
			return path;
		}
		return nextResult == null ? aggregate : nextResult;
	}

	private Shape add(Shape shape) {
		if (shape != null) {
			shapes.add(shape);
		}
		return shape;
	}

}