package uk.co.bluegecko.utility.convert;

import java.awt.Point;
import org.springframework.core.convert.converter.Converter;
import org.springframework.core.convert.converter.ConverterRegistry;
import org.springframework.util.StringUtils;

public class PointConvertor implements BiConverter<Point, String> {


	@Override
	public Converter<Point, String> from() {
		return source -> "[%1d;%d]".formatted(source.x, source.y);
	}

	@Override
	public Converter<String, Point> to() {
		return source -> {
			String s = source.trim();
			if (StringUtils.hasText(s) && s.length() > 4 && s.startsWith("[") && s.endsWith("]")) {
				String[] p = s.substring(1, s.length() - 1).split(";");
				return new Point(Integer.parseInt(p[0]), Integer.parseInt(p[1]));
			}
			throw new IllegalArgumentException("Unable to parse %s as a point, expected '[x,y]'".formatted(s));
		};
	}

	public void register(ConverterRegistry registry) {
		register(Point.class, String.class, registry);
	}

}