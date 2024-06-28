package uk.co.bluegecko.utility.convert;

import java.awt.Point;
import org.springframework.core.convert.converter.Converter;

public class PointToStringConverter implements Converter<Point, String> {

	@Override
	public String convert(Point source) {
		return "[%1d;%d]".formatted(source.x, source.y);
	}

}