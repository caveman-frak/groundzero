package uk.co.bluegecko.utility.svg;

import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.nio.charset.StandardCharsets;
import org.apache.batik.dom.GenericDOMImplementation;
import org.apache.batik.svggen.SVGGeneratorContext;
import org.apache.batik.svggen.SVGGraphics2D;
import org.w3c.dom.DOMImplementation;
import org.w3c.dom.Document;
import org.w3c.dom.DocumentType;

public class Batik extends AbstractSvg<SVGGraphics2D> {

	public static void main(String[] args) throws IOException {
		new Batik(createGraphics2D())
				.build().write();
	}

	public Batik(SVGGraphics2D g) {
		super(g);
	}

	@Override
	protected void write() throws IOException {
		Writer out = new FileWriter("output/radar-batik.svg", StandardCharsets.UTF_8);
		graphics.stream(out, true);
	}

	private static SVGGraphics2D createGraphics2D() {
		DOMImplementation dom = GenericDOMImplementation.getDOMImplementation();
		String ns = "http://www.w3.org/2000/svg";
		DocumentType docType = dom.createDocumentType("svg", "-//W3C//DTD SVG 1.1//EN",
				"http://www.w3.org/Graphics/SVG/1.1/DTD/svg11.dtd");
		Document document = dom.createDocument(ns, "svg", docType);

		SVGGeneratorContext ctx = SVGGeneratorContext.createDefault(document);
		ctx.setEmbeddedFontsOn(true);
		ctx.setComment("Created by Caveman Frak at Blue Gecko Ⓒ 2024");

		return new SVGGraphics2D(ctx, true);
	}

}