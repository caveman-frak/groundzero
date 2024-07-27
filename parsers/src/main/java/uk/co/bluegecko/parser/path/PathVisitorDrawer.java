package uk.co.bluegecko.parser.path;

import java.awt.Point;
import java.awt.Shape;
import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import uk.co.bluegecko.parser.path.PathParser.ArcContext;
import uk.co.bluegecko.parser.path.PathParser.CloseContext;
import uk.co.bluegecko.parser.path.PathParser.CubicContext;
import uk.co.bluegecko.parser.path.PathParser.HorizontalContext;
import uk.co.bluegecko.parser.path.PathParser.LineContext;
import uk.co.bluegecko.parser.path.PathParser.MoveContext;
import uk.co.bluegecko.parser.path.PathParser.QuadraticContext;
import uk.co.bluegecko.parser.path.PathParser.VerticalContext;

@Getter
public class PathVisitorDrawer extends PathBaseVisitor<String> implements PathHelper, PathPrinter {

	private final List<Shape> shapes = new ArrayList<>();
	private final Point origin = new Point();
	private final Point position = new Point();

	@Override
	public String visitMove(MoveContext ctx) {
		return super.visitMove(ctx);
	}

	@Override
	public String visitLine(LineContext ctx) {
		return super.visitLine(ctx);
	}

	@Override
	public String visitHorizontal(HorizontalContext ctx) {
		return super.visitHorizontal(ctx);
	}

	@Override
	public String visitVertical(VerticalContext ctx) {
		return super.visitVertical(ctx);
	}

	@Override
	public String visitArc(ArcContext ctx) {
		return super.visitArc(ctx);
	}

	@Override
	public String visitCubic(CubicContext ctx) {
		return super.visitCubic(ctx);
	}

	@Override
	public String visitQuadratic(QuadraticContext ctx) {
		return super.visitQuadratic(ctx);
	}

	@Override
	public String visitClose(CloseContext ctx) {
		return super.visitClose(ctx);
	}

	@Override
	public String prefix() {
		return "Visitor: ";
	}
}