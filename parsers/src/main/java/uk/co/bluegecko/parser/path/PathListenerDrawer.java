package uk.co.bluegecko.parser.path;

import java.awt.Graphics;
import java.awt.Point;
import java.util.Objects;
import java.util.stream.Collectors;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.antlr.v4.runtime.ParserRuleContext;
import uk.co.bluegecko.parser.path.PathParser.HorizontalContext;
import uk.co.bluegecko.parser.path.PathParser.MoveContext;

@Getter
@RequiredArgsConstructor
public class PathListenerDrawer extends PathBaseListener {

	private final Graphics graphics;

	private final Point position = new Point();
	private ParserRuleContext context;

	@Override
	public void enterMove(MoveContext ctx) {
		context = ctx;
		System.out.println(">M: " + string(ctx));
	}

	@Override
	public void enterHorizontal(HorizontalContext ctx) {
		context = ctx;
		System.out.println(">H: " + string(ctx));
	}

	private String string(ParserRuleContext ctx) {
		return ctx.children.stream().map(Objects::toString).collect(Collectors.joining(", "));
	}

}