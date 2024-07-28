package uk.co.bluegecko.parser.path;

import static uk.co.bluegecko.parser.path.ShapeComparator.ChainCompare.chain;

import java.awt.Shape;
import java.awt.geom.Arc2D;
import java.awt.geom.CubicCurve2D;
import java.awt.geom.Line2D;
import java.awt.geom.QuadCurve2D;
import java.util.Comparator;
import java.util.Optional;

/**
 * Only worries about testing for equality, as this is only used by AssertJ.
 */
public class ShapeComparator implements Comparator<Shape> {

	@Override
	public int compare(Shape s1, Shape s2) {
		if ((s1 instanceof Line2D l1) && (s2 instanceof Line2D l2)) {
			return chain(l1.getX2(), l1.getX2(), "x1")
					.and(l1.getY2(), l1.getY2(), "y1")
					.and(l1.getX2(), l1.getX2(), "x2")
					.and(l1.getY2(), l1.getY2(), "y2").result();
		} else if ((s1 instanceof CubicCurve2D c1) && (s2 instanceof CubicCurve2D c2)) {
			return chain(c1.getX2(), c1.getX2(), "x1")
					.and(c1.getY2(), c1.getY2(), "y1")
					.and(c1.getCtrlX1(), c2.getCtrlX1(), "ctrlx1")
					.and(c1.getCtrlY1(), c2.getCtrlY1(), "ctrly1")
					.and(c1.getCtrlX2(), c2.getCtrlX2(), "ctrlx2")
					.and(c1.getCtrlY2(), c2.getCtrlY2(), "ctrly2")
					.and(c1.getX2(), c1.getX2(), "x2")
					.and(c1.getY2(), c1.getY2(), "y2").result();
		} else if ((s1 instanceof QuadCurve2D q1) && (s2 instanceof QuadCurve2D q2)) {
			return chain(q1.getX2(), q1.getX2(), "x1")
					.and(q1.getY2(), q1.getY2(), "y1")
					.and(q1.getCtrlX(), q2.getCtrlX(), "ctrlx")
					.and(q1.getCtrlY(), q2.getCtrlY(), "ctrly")
					.and(q1.getX2(), q1.getX2(), "x2")
					.and(q1.getY2(), q1.getY2(), "y2").result();
		} else if ((s1 instanceof Arc2D a1) && (s2 instanceof Arc2D a2)) {
			return chain(a1.getX(), a2.getX(), "x")
					.and(a1.getY(), a2.getY(), "y")
					.and(a1.getHeight(), a2.getHeight(), "height")
					.and(a1.getWidth(), a2.getWidth(), "width")
					.and(a1.getAngleStart(), a2.getAngleStart(), "start")
					.and(a1.getAngleExtent(), a2.getAngleExtent(), "extent").result();
		} else {
			throw new IllegalArgumentException(String.format("Unsupported shape %s", s1.getClass().getSimpleName()));
		}
	}

	protected static class ChainCompare {

		private final int result;
		private final String failure;

		private ChainCompare(int result, String failure) {
			this.result = result;
			this.failure = failure;
		}

		private ChainCompare() {
			this(0, null);
		}

		public <T extends Comparable<T>> ChainCompare and(T o1, T o2, String field) {
			if (result == 0) {
				int i = o1.compareTo(o2);
				return new ChainCompare(i, i == 0 ? null : field);
			} else {
				return this;
			}
		}

		public <T extends Comparable<T>> ChainCompare and(T o1, T o2) {
			return and(o1, o2, null);
		}

		public int result() {
			return result;
		}

		public Optional<String> failure() {
			if (result != 0) {
				return Optional.ofNullable(failure);
			} else {
				return Optional.empty();
			}
		}

		public static <T extends Comparable<T>> ChainCompare chain(T o1, T o2, String field) {
			return new ChainCompare().and(o1, o2, field);
		}

		public static <T extends Comparable<T>> ChainCompare chain(T o1, T o2) {
			return chain(o1, o2, null);
		}

	}

}