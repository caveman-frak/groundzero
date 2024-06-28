package uk.co.bluegecko.utility.convert;

import java.awt.Point;
import org.jetbrains.annotations.NotNull;
import org.springframework.core.convert.converter.Converter;
import org.springframework.util.StringUtils;

public class StringToPointConverter implements Converter<String, Point> {

	@Override
	public Point convert(@NotNull String source) {
		String s = source.trim();
		if (StringUtils.hasText(s) && s.length() > 4 && s.startsWith("[") && s.endsWith("]")) {
			String[] p = s.substring(1, s.length() - 1).split(";");
			return new Point(Integer.parseInt(p[0]), Integer.parseInt(p[1]));
		}
		throw new IllegalArgumentException("Unable to parse %s as a point, expected '[x,y]'".formatted(s));
	}

}