package uk.co.bluegecko.utility.svg;

import java.io.File;
import java.io.IOException;
import org.jfree.svg.SVGGraphics2D;
import org.jfree.svg.SVGHints;
import org.jfree.svg.SVGUtils;

public class JFreeSvg extends AbstractSvg<SVGGraphics2D> {

	public static void main(String[] args) throws IOException {
		new JFreeSvg(new SVGGraphics2D(600, 600))
				.build().write();
	}

	public JFreeSvg(SVGGraphics2D g) {
		super(g);
	}

	@Override
	protected void write() throws IOException {
		SVGUtils.writeToSVG(new File("output/radar-jfree.svg"), graphics.getSVGElement());
	}

	@Override
	protected void startGroup(String key) {
		graphics.setRenderingHint(SVGHints.KEY_BEGIN_GROUP, key);
	}

	@Override
	protected void endGroup(String key) {
		graphics.setRenderingHint(SVGHints.KEY_END_GROUP, key);
	}

}