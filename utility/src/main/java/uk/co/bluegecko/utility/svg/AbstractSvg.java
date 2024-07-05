package uk.co.bluegecko.utility.svg;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Shape;
import java.awt.geom.AffineTransform;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Line2D;
import java.awt.geom.Path2D;
import java.io.IOException;
import java.util.Random;

public abstract class AbstractSvg<T extends Graphics2D> {

	private final Color BACKGROUND_COLOR = new Color(0x293021);
	private final Color RADAR_COLOR = new Color(0x216e1f);

	protected final T graphics;

	public AbstractSvg(T graphics) {
		this.graphics = graphics;
	}

	protected AbstractSvg<T> build() {
		graphics.setClip(0, 0, 600, 600);

		Rectangle bounds = drawBackground();
		bounds.grow(-20, -20);
		Shape radar = drawRadar(bounds);

		Random random = new Random();
		for (int i = 0; i < random.nextInt(10); i++) {
			drawRandomVessel(random, i, radar);
		}
		return this;
	}

	protected Shape drawRadar(Rectangle bounds) {
		startGroup("radar");
		Shape shape = drawOuterCircle(bounds);
		drawInnerRings(bounds);
		drawAxis(bounds);
		endGroup("radar");
		return shape;
	}

	protected Rectangle drawBackground() {
		Rectangle bounds = graphics.getClipBounds();
		graphics.setPaint(BACKGROUND_COLOR);
		graphics.fill(bounds);
		return bounds;
	}

	private Shape drawOuterCircle(Rectangle bounds) {
		Ellipse2D circle = new Ellipse2D.Double();
		circle.setFrame(bounds);

		graphics.setPaint(RADAR_COLOR);
		graphics.fill(circle);

		graphics.setPaint(Color.GREEN);
		graphics.setStroke(new BasicStroke(2));
		graphics.draw(circle);
		return circle;
	}

	private void drawInnerRings(Rectangle bounds) {
		graphics.setStroke(dottedLine());
		Rectangle b = new Rectangle(bounds);
		for (int x = 20; x < Math.min(bounds.width, bounds.height) / 2; x += 40) {
			b.grow(-40, -40);
			Ellipse2D circle = new Ellipse2D.Double();
			circle.setFrame(b);
			graphics.draw(circle);
		}
	}

	private void drawAxis(Rectangle bounds) {
		graphics.setStroke(new BasicStroke(0.5f));
		graphics.draw(new Line2D.Double(bounds.getMinX(), bounds.getCenterY(), bounds.getMaxX(), bounds.getCenterY()));
		graphics.draw(new Line2D.Double(bounds.getCenterX(), bounds.getMinY(), bounds.getCenterX(), bounds.getMaxY()));
	}

	protected void drawRandomVessel(Random random, int count, Shape radar) {
		Rectangle r = radar.getBounds();
		Point p = new Point(random.nextInt(r.x, r.width), random.nextInt(r.y, r.height));
		if (radar.contains(p.x, p.y)) {
			drawVessel(p, random.nextDouble(Math.PI * 2), count);
		}
	}

	protected void drawVessel(Point p, double rotation, int count) {
		String key = "vessel-" + count;
		Path2D vessel = vesselPath();
		Point centre = new Point(10, 5);
		vessel.transform(AffineTransform.getRotateInstance(rotation, centre.x, centre.y));
		vessel.transform(AffineTransform.getTranslateInstance(p.x, p.y));
		centre.translate(p.x, p.y);

		startGroup(key);
		graphics.setPaint(Color.WHITE);
		graphics.fill(vessel);
		graphics.setPaint(Color.BLACK);
		graphics.draw(vessel);
		graphics.setPaint(Color.GREEN);
		graphics.drawString(String.valueOf(count), p.x + 20, p.y);
		endGroup(key);
	}

	private Path2D vesselPath() {
		Path2D vessel = new Path2D.Double(Path2D.WIND_EVEN_ODD, 5);
		vessel.moveTo(0, 0);
		vessel.lineTo(0, 10);
		vessel.lineTo(15, 10);
		vessel.lineTo(20, 5);
		vessel.lineTo(15, 0);
		vessel.closePath();
		return vessel;
	}

	private BasicStroke dottedLine() {
		return new BasicStroke(0.5f,
				BasicStroke.CAP_BUTT,
				BasicStroke.JOIN_MITER,
				1.0f,
				new float[]{2.0f},
				0.0f);
	}

	protected abstract void write() throws IOException;

	protected void startGroup(String key) {
	}

	protected void endGroup(String key) {
	}

}