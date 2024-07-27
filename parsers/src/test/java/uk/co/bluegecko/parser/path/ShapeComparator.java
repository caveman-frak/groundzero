package uk.co.bluegecko.parser.path;

import java.awt.Shape;
import java.awt.geom.Arc2D;
import java.awt.geom.CubicCurve2D;
import java.awt.geom.Line2D;
import java.awt.geom.QuadCurve2D;
import java.util.Comparator;

/**
 * Only worries about testing for equality, as this is only used by AssertJ.
 */
public class ShapeComparator implements Comparator<Shape> {

	@Override
	public int compare(Shape s1, Shape s2) {
		if ((s1 instanceof Line2D.Double l1) && (s2 instanceof Line2D.Double l2)) {
			return (l1.x1 == l2.x1) &&
					(l1.y1 == l2.y1) &&
					(l1.x2 == l2.x2) &&
					(l1.y2 == l2.y2) ? 0 : -1;
		} else if ((s1 instanceof CubicCurve2D.Double c1) && (s2 instanceof CubicCurve2D.Double c2)) {
			return (c1.x1 == c2.x1) &&
					(c1.y1 == c2.y1) &&
					(c1.ctrlx1 == c2.ctrlx1) &&
					(c1.ctrly1 == c2.ctrly1) &&
					(c1.ctrlx2 == c2.ctrlx2) &&
					(c1.ctrly2 == c2.ctrly2) &&
					(c1.x2 == c2.x2) &&
					(c1.y2 == c2.y2) ? 0 : -1;
		} else if ((s1 instanceof QuadCurve2D.Double q1) && (s2 instanceof QuadCurve2D.Double q2)) {
			return (q1.x1 == q2.x1) &&
					(q1.y1 == q2.y1) &&
					(q1.ctrlx == q2.ctrlx) &&
					(q1.ctrly == q2.ctrly) &&
					(q1.x2 == q2.x2) &&
					(q1.y2 == q2.y2) ? 0 : -1;
		} else if ((s1 instanceof Arc2D.Double a1) && (s2 instanceof Arc2D.Double a2)) {
			return (a1.x == a2.x) &&
					(a1.y == a2.y) &&
					(a1.height == a2.height) &&
					(a1.width == a2.width) &&
					(a1.start == a2.start) &&
					(a1.extent == a2.extent) ? 0 : -1;
		} else {
			return 1;
		}
	}

}