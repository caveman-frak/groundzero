package uk.co.bluegecko.utility.geo.format;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.ParseException;
import java.text.ParsePosition;
import java.util.Locale;
import lombok.NonNull;
import org.springframework.format.Formatter;
import uk.co.bluegecko.utility.geo.Angle;
import uk.co.bluegecko.utility.geo.Bearing;
import uk.co.bluegecko.utility.geo.Compass;
import uk.co.bluegecko.utility.geo.Hemisphere;
import uk.co.bluegecko.utility.geo.Latitude;
import uk.co.bluegecko.utility.geo.Longitude;

public class AngleFormatter implements Formatter<Angle> {

	@NonNull
	@Override
	public Angle parse(@NonNull String text, @NonNull Locale locale) throws ParseException {
		DecimalFormat numberFormat = new DecimalFormat("#0", new DecimalFormatSymbols(locale));
		final ParsePosition pos = new ParsePosition(0);
		Number degrees = orDefault(forDegrees(numberFormat).parse(text, pos), 0L);
		Number minutes = orDefault(forMinutes(numberFormat).parse(text, pos), 0L);
		Number seconds = orDefault(forSeconds(numberFormat).parse(text, pos), 0.0);
		if (pos.getErrorIndex() > -1) {
			throw new ParseException(
					String.format("Cannot parse '%s', error at index %d.", text, pos.getErrorIndex()),
					pos.getErrorIndex());
		}
		if (text.length() > pos.getIndex()) {
			String hemisphere = text.substring(pos.getIndex(), pos.getIndex() + 1);
			Compass compass = Compass.valueOf(hemisphere);
			if (Compass.latitude().contains(compass)) {
				return new Latitude((int) (long) degrees, (int) (long) minutes, (double) seconds, compass);
			} else if (Compass.longitude().contains(compass)) {
				return new Longitude((int) (long) degrees, (int) (long) minutes, (double) seconds, compass);
			}
		}
//		final int length = text.length();
//		final int origin = pos.getIndex();
//		for (int index = origin; index < length; index++) {
//			if (!Character.isWhitespace(text.charAt(index))) {
//				index = Math.max(origin, pos.getErrorIndex());
//				throw new ParseException(
//						"Cannot parse '" + text + "', error at index " + index + ".", index);
//			}
//		}

		return new Bearing((int) (long) degrees, (int) (long) minutes, (double) seconds);
	}

	@NonNull
	@Override
	public String print(Angle a, @NonNull Locale locale) {
		DecimalFormat format = new DecimalFormat("#0", new DecimalFormatSymbols(locale));
		return String.format("%s%s%s%s",
				forDegrees(format).format(Math.abs(a.getDegrees())),
				forMinutes(format).format(a.getMinutes()),
				forSeconds(format).format(a.getSeconds()),
				withHemisphere(a));
	}

	private Number orDefault(Number value, Number def) {
		return value == null ? def : value;
	}

	private DecimalFormat forDegrees(DecimalFormat format) {
		format.applyLocalizedPattern("##0°");
		return format;
	}

	private DecimalFormat forMinutes(DecimalFormat format) {
		format.applyLocalizedPattern("#0''");
		return format;
	}

	private DecimalFormat forSeconds(DecimalFormat format) {
		format.applyLocalizedPattern("#0.######\"");
		return format;
	}

	private String withHemisphere(Angle a) {
		if (a instanceof Hemisphere h) {
			return h.getHemisphere().name();
		} else {
			return "";
		}
	}
}