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
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.nio.charset.StandardCharsets;
import java.util.Random;
import org.apache.batik.dom.GenericDOMImplementation;
import org.apache.batik.svggen.SVGGeneratorContext;
import org.apache.batik.svggen.SVGGraphics2D;
import org.w3c.dom.DOMImplementation;
import org.w3c.dom.Document;
import org.w3c.dom.DocumentType;
import org.w3c.dom.Element;

public class Batik {

	public static final Color BACKGROUND_COLOR = new Color(0x293021);
	public static final Color RADAR_COLOR = new Color(0x216e1f);

	public static void main(String[] args) throws IOException {
		SVGGraphics2D graphics2D = createGraphics2D();
		graphics2D.setClip(0, 0, 600, 600);

		Rectangle bounds = drawBackground(graphics2D);
		bounds.grow(-20, -20);
		Shape radar = drawRadar(graphics2D, bounds);

		Random random = new Random();
		for (int i = 0; i < random.nextInt(10); i++) {
			drawRandomVessel(graphics2D, random, i, radar);
		}

		Writer out = new FileWriter("output/radar-batik.svg", StandardCharsets.UTF_8);
		graphics2D.stream(out, true);
	}

	private static SVGGraphics2D createGraphics2D() {
		DOMImplementation dom = GenericDOMImplementation.getDOMImplementation();
		String ns = "http://www.w3.org/2000/svg";
		DocumentType docType = dom.createDocumentType("svg", "-//W3C//DTD SVG 1.1//EN",
				"http://www.w3.org/Graphics/SVG/1.1/DTD/svg11.dtd");
		Document document = dom.createDocument(ns, "svg", docType);

		Element root = document.getDocumentElement();
		root.setAttributeNS(null, "width", "600");
		root.setAttributeNS(null, "height", "600");

		SVGGeneratorContext ctx = SVGGeneratorContext.createDefault(document);
		ctx.setEmbeddedFontsOn(true);
		ctx.setComment("Created by Caveman Frak at Blue Gecko 2024");

		return new SVGGraphics2D(ctx, true);
	}

	private static void drawRandomVessel(Graphics2D g, Random random, int count, Shape radar) {
		Rectangle r = g.getClipBounds();
		Point p = new Point(random.nextInt(20, (int) r.getWidth() - 40), random.nextInt(20,
				(int) r.getWidth() - 40));
		if (radar.contains(p.x, p.y)) {
			drawVessel(g, p, random.nextDouble(Math.PI * 2), count);
		}
	}

	private static void drawVessel(Graphics2D g, Point p, double rotation, int count) {
		Path2D vessel = vesselPath();
		Point centre = new Point(10, 5);
		vessel.transform(AffineTransform.getRotateInstance(rotation, centre.x, centre.y));
		vessel.transform(AffineTransform.getTranslateInstance(p.x, p.y));
		centre.translate(p.x, p.y);

		g.setPaint(Color.WHITE);
		g.fill(vessel);
		g.setPaint(Color.BLACK);
		g.draw(vessel);
		g.setPaint(Color.GREEN);
		g.drawString(String.valueOf(count), p.x + 20, p.y);
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
//		g.setRenderingHint(SVGHints.KEY_BEGIN_GROUP, "radar");
		Shape shape = drawOuterCircle(g, bounds);
		drawInnerRings(g, bounds);
		drawRadials(g, bounds);
//		g.setRenderingHint(SVGHints.KEY_END_GROUP, "radar");
		return shape;
	}

	private static Rectangle drawBackground(Graphics2D g) {
		Rectangle r = g.getClipBounds();
		Rectangle bounds = new Rectangle((int) r.getWidth(), (int) r.getHeight());
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

	private static void drawRadials(SVGGraphics2D g, Rectangle b) {
		g.setStroke(new BasicStroke(0.5f));
		g.draw(new Line2D.Double(b.getMinX(), b.getCenterY(), b.getMaxX(), b.getCenterY()));
		g.draw(new Line2D.Double(b.getCenterX(), b.getMinY(), b.getCenterX(), b.getMaxY()));
	}

	private static BasicStroke dottedLine() {
		return new BasicStroke(0.5f,
				BasicStroke.CAP_BUTT,
				BasicStroke.JOIN_MITER,
				1.0f,
				new float[]{2.0f},
				0.0f);
	}

}