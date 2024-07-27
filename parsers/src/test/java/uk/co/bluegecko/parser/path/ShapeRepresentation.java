package uk.co.bluegecko.parser.path;

import java.awt.geom.Arc2D;
import java.awt.geom.CubicCurve2D;
import java.awt.geom.Line2D;
import java.awt.geom.QuadCurve2D;
import org.assertj.core.presentation.StandardRepresentation;

public class ShapeRepresentation extends StandardRepresentation {

	@Override
	public String fallbackToStringOf(Object o) {
		return switch (o) {
			case Line2D.Double l -> String.format("Line[x1=%1.0f,y1=%1.0f,x2=%1.0f,y2=%1.0f]",
					l.x1, l.y1, l.x2, l.y2);
			case CubicCurve2D.Double l -> String.format(
					"Cubic[x1=%1.0f,y1=%1.0f,cx1=%1.0f,cy1=%1.0f,cx2=%1.0f,c2y1=%1.0f,x2=%1.0f,y2=%1.0f]",
					l.x1, l.y1, l.ctrlx1, l.ctrly1, l.ctrlx2, l.ctrly2, l.x2, l.y2);
			case QuadCurve2D.Double l -> String.format("Quad[x1=%1.0f,y1=%1.0f,cx1=%1.0f,cy1=%1.0f,x2=%1.0f,y2=%1.0f]",
					l.x1, l.y1, l.ctrlx, l.ctrly, l.x2, l.y2);
			case Arc2D.Double l -> String.format("Line[x=%1.0f,y=%1.0f,h=%1.0f,w=%1.0f,s=%1.0f,e=%1.0f,t=%d]",
					l.x, l.y, l.height, l.width, l.start, l.extent, l.getArcType());
			case null, default -> super.fallbackToStringOf(o);
		};
	}
}