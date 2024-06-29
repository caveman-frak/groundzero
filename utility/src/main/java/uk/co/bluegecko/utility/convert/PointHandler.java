package uk.co.bluegecko.utility.convert;

import java.awt.Point;
import org.springframework.core.convert.ConversionService;

public class PointHandler extends SimpleHandler<Point> {

	public PointHandler(ConversionService conversionService) {
		super(conversionService, Point.class);
	}

}