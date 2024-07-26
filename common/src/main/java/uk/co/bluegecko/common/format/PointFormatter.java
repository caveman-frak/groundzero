package uk.co.bluegecko.common.format;

import java.awt.Point;
import java.text.ParseException;
import java.util.Locale;
import org.springframework.format.Formatter;
import org.springframework.util.StringUtils;

public class PointFormatter implements Formatter<Point> {

	@Override
	public Point parse(String text, Locale locale) throws ParseException {
		String s = text.trim();
		if (StringUtils.hasText(s) && s.length() > 4 && s.startsWith("[") && s.endsWith("]")) {
			String[] p = s.substring(1, s.length() - 1).split(";");
			return new Point(Integer.parseInt(p[0]), Integer.parseInt(p[1]));
		}
		throw new ParseException("Unable to parse %s as a point, expected '[x,y]'".formatted(s), -1);
	}

	@Override
	public String print(Point source, Locale locale) {
		return "[%1d;%d]".formatted(source.x, source.y);
	}

}