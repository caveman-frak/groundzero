package uk.co.bluegecko.parser.path;

import uk.co.bluegecko.parser.path.PathParser.LineContext;
import uk.co.bluegecko.parser.path.PathParser.MoveContext;

public class PathVisitorDrawer extends PathBaseVisitor<String> implements PathPrinter {

	@Override
	public String visitMove(MoveContext ctx) {
		return super.visitMove(ctx);
	}

	@Override
	public String visitLine(LineContext ctx) {
		return super.visitLine(ctx);
	}

	@Override
	public String prefix() {
		return "Visitor: ";
	}

}