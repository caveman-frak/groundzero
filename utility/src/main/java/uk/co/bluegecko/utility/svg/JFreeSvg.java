package uk.co.bluegecko.utility.svg;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Shape;
import java.awt.geom.AffineTransform;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Line2D;
import java.awt.geom.Path2D;
import java.io.File;
import java.io.IOException;
import java.util.Random;
import org.jfree.svg.SVGGraphics2D;
import org.jfree.svg.SVGHints;
import org.jfree.svg.SVGUtils;

public class JFreeSvg {

	public static final Color BACKGROUND_COLOR = new Color(0x293021);
	public static final Color RADAR_COLOR = new Color(0x216e1f);

	public static void main(String[] args) throws IOException {
		SVGGraphics2D g = new SVGGraphics2D(600, 600);

		Rectangle bounds = drawBackground(g);
		bounds.grow(-20, -20);
		Shape radar = drawRadar(g, bounds);

		Random random = new Random();
		for (int i = 0; i < random.nextInt(10); i++) {
			drawRandomVessel(g, random, i, radar);
		}

		SVGUtils.writeToSVG(new File("output/radar.svg"), g.getSVGElement());
	}

	private static void drawRandomVessel(SVGGraphics2D g, Random random, int count, Shape radar) {
		Point p = new Point(random.nextInt(20, (int) g.getWidth() - 40), random.nextInt(20,
				(int) g.getWidth() - 40));
		if (radar.contains(p.x, p.y)) {
			drawVessel(g, p, random.nextDouble(Math.PI * 2), count);
		}
	}

	private static void drawVessel(SVGGraphics2D g, Point p, double rotation, int count) {
		String identifier = "vessel-" + count;
		Path2D vessel = vesselPath();
		Point centre = new Point(10, 5);
		vessel.transform(AffineTransform.getRotateInstance(rotation, centre.x, centre.y));
		vessel.transform(AffineTransform.getTranslateInstance(p.x, p.y));
		centre.translate(p.x, p.y);

		g.setRenderingHint(SVGHints.KEY_BEGIN_GROUP, identifier);
		g.setPaint(Color.WHITE);
		g.fill(vessel);
		g.setPaint(Color.BLACK);
		g.draw(vessel);
		g.setPaint(Color.GREEN);
		g.drawString(String.valueOf(count), p.x + 20, p.y);
		g.setRenderingHint(SVGHints.KEY_END_GROUP, identifier);
	}

	private static Path2D vesselPath() {
		Path2D vessel = new Path2D.Double(Path2D.WIND_EVEN_ODD, 5);
		vessel.moveTo(0, 0);
		vessel.lineTo(0, 10);
		vessel.lineTo(15, 10);
		vessel.lineTo(15, 10);
		vessel.lineTo(20, 5);
		vessel.lineTo(15, 0);
		vessel.closePath();
		return vessel;
	}

	private static Shape drawRadar(SVGGraphics2D g, Rectangle bounds) {
		g.setRenderingHint(SVGHints.KEY_BEGIN_GROUP, "radar");
		Shape shape = drawOuterCircle(g, bounds);
		drawInnerRings(g, bounds);
		drawRadials(g, bounds);
		g.setRenderingHint(SVGHints.KEY_END_GROUP, "radar");
		return shape;
	}

	private static Rectangle drawBackground(SVGGraphics2D g) {
		Rectangle bounds = new Rectangle((int) g.getWidth(), (int) g.getHeight());
		g.setPaint(BACKGROUND_COLOR);
		g.fill(bounds);
		return bounds;
	}

	private static Shape drawOuterCircle(SVGGraphics2D g, Rectangle bounds) {
		Ellipse2D circle = new Ellipse2D.Double();
		circle.setFrame(bounds);

		g.setPaint(RADAR_COLOR);
		g.fill(circle);

		g.setPaint(Color.GREEN);
		g.setStroke(new BasicStroke(2));
		g.draw(circle);
		return circle;
	}

	private static void drawInnerRings(SVGGraphics2D g, Rectangle bounds) {
		g.setStroke(dottedLine());
		Rectangle b = new Rectangle(bounds);
		for (int x = 20; x < Math.min(bounds.width, bounds.height) / 2; x += 40) {
			b.grow(-40, -40);
			Ellipse2D circle = new Ellipse2D.Double();
			circle.setFrame(b);
			g.draw(circle);
		}
	}

	private static BasicStroke dottedLine() {
		return new BasicStroke(0.5f,
				BasicStroke.CAP_BUTT,
				BasicStroke.JOIN_MITER,
				1.0f,
				new float[]{2.0f},
				0.0f);
	}

	private static void drawRadials(SVGGraphics2D g, Rectangle b) {
		g.setStroke(new BasicStroke(0.5f));
		g.draw(new Line2D.Double(b.getMinX(), b.getCenterY(), b.getMaxX(), b.getCenterY()));
		g.draw(new Line2D.Double(b.getCenterX(), b.getMinY(), b.getCenterX(), b.getMaxY()));
	}

	private static Color withAlpha(Color color, int alpha) {
		return new Color(color.getRed(), color.getGreen(), color.getBlue(), alpha);
	}

	private static String p(Shape s) {
		Rectangle r = s.getBounds();
		return "[t=%1.0f,l=%1.0f;b=%1.0f,r=%1.0f]".formatted(r.getMinY(), r.getMinX(), r.getMaxY(), r.getMaxX());
	}

}