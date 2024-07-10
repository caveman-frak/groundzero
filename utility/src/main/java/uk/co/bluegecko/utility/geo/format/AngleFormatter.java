package uk.co.bluegecko.utility.geo.format;

import static systems.uom.ucum.UCUM.DEGREE;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.ParseException;
import java.text.ParsePosition;
import java.util.Locale;
import lombok.NonNull;
import org.springframework.format.Formatter;
import tech.units.indriya.quantity.Quantities;
import uk.co.bluegecko.utility.geo.Angle;
import uk.co.bluegecko.utility.geo.Bearing;
import uk.co.bluegecko.utility.geo.Compass;
import uk.co.bluegecko.utility.geo.DMS;
import uk.co.bluegecko.utility.geo.Hemisphere;
import uk.co.bluegecko.utility.geo.Latitude;
import uk.co.bluegecko.utility.geo.Longitude;

public class AngleFormatter implements Formatter<Angle> {

	@NonNull
	@Override
	public Angle parse(@NonNull String text, @NonNull Locale locale) throws ParseException {
		DecimalFormat numberFormat = new DecimalFormat("#0", new DecimalFormatSymbols(locale));
		final ParsePosition pos = new ParsePosition(0);
		int degrees = orDefault(forDegrees(numberFormat).parse(text, pos), 0L).intValue();
		int minutes = orDefault(forMinutes(numberFormat).parse(text, pos), 0L).intValue();
		double seconds = orDefault(forSeconds(numberFormat).parse(text, pos), 0.0).doubleValue();
		Double decimal = (Double) forDecimal(numberFormat).parse(text, pos);
		if (pos.getErrorIndex() > pos.getIndex()) {
			throw new ParseException(
					String.format("Cannot parse '%s', error at %d:%d.",
							text, pos.getIndex(), pos.getErrorIndex()),
					pos.getErrorIndex());
		} else if (decimal != null) {
			return Bearing.fromAngle(Quantities.getQuantity(decimal, DEGREE));
		} else if (text.length() > pos.getIndex()) {
			String hemisphere = text.substring(pos.getIndex());
			if (Compass.latitude().stream().map(Compass::name).anyMatch(n -> n.equals(hemisphere))) {
				return new Latitude(degrees, minutes, seconds, Compass.valueOf(hemisphere));
			} else if (Compass.longitude().stream().map(Compass::name).anyMatch(n -> n.equals(hemisphere))) {
				return new Longitude(degrees, minutes, seconds, Compass.valueOf(hemisphere));
			} else {
				throw new ParseException(
						String.format("Invalid compass direction '%s' at %d", hemisphere, pos.getIndex()),
						pos.getIndex());
			}
		}
		return new Bearing(degrees, minutes, seconds);
	}

	@NonNull
	@Override
	public String print(@NonNull Angle angle, @NonNull Locale locale) {
		DecimalFormat format = new DecimalFormat("#0", new DecimalFormatSymbols(locale));
		if (angle instanceof DMS dms) {
			return String.format("%s%s%s%s",
					forDegrees(format).format(Math.abs(dms.getDegrees())),
					forMinutes(format).format(dms.getMinutes()),
					forSeconds(format).format(dms.getSeconds()),
					withHemisphere(angle));
		}
		return forDecimal(format).format(angle.decimal().getValue().doubleValue());
	}

	private Number orDefault(Number value, Number def) {
		return value == null ? def : value;
	}

	private DecimalFormat forDegrees(DecimalFormat format) {
		format.applyLocalizedPattern("##0°");
		format.setParseIntegerOnly(true);
		return format;
	}

	private DecimalFormat forMinutes(DecimalFormat format) {
		format.applyLocalizedPattern("#0''");
		format.setParseIntegerOnly(true);
		return format;
	}

	private DecimalFormat forSeconds(DecimalFormat format) {
		format.applyLocalizedPattern("#0.######\"");
		format.setParseIntegerOnly(false);
		return format;
	}

	private DecimalFormat forDecimal(DecimalFormat format) {
		format.applyLocalizedPattern("##0.######°");
		format.setParseIntegerOnly(false);
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