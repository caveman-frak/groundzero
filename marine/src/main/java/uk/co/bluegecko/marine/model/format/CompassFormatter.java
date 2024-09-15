package uk.co.bluegecko.marine.model.format;

import static systems.uom.ucum.UCUM.DEGREE;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.ParseException;
import java.text.ParsePosition;
import java.util.Locale;
import javax.measure.quantity.Angle;
import lombok.NonNull;
import org.springframework.format.Formatter;
import tech.units.indriya.ComparableQuantity;
import tech.units.indriya.quantity.Quantities;
import uk.co.bluegecko.marine.model.compass.Bearing;
import uk.co.bluegecko.marine.model.compass.Compass;
import uk.co.bluegecko.marine.model.compass.Hemisphere;
import uk.co.bluegecko.marine.model.compass.Hemisphere.Spheriod;
import uk.co.bluegecko.marine.model.compass.Latitude;
import uk.co.bluegecko.marine.model.compass.Limit;
import uk.co.bluegecko.marine.model.compass.Longitude;

public class CompassFormatter implements Formatter<Compass> {

	@NonNull
	@Override
	public Compass parse(@NonNull String text, @NonNull Locale locale) throws ParseException {
		DecimalFormat numberFormat = new DecimalFormat("#0", new DecimalFormatSymbols(locale));
		final ParsePosition pos = new ParsePosition(0);
		int degrees = orDefault(forDegrees(numberFormat).parse(text, pos), 0L).intValue();
		int minutes = orDefault(forMinutes(numberFormat).parse(text, pos), 0L).intValue();
		double seconds = orDefault(forSeconds(numberFormat).parse(text, pos), 0.0).doubleValue();
		ComparableQuantity<Angle> angle = Quantities.getQuantity(
				(double) degrees + ((double) minutes / 60) + seconds / 3600, DEGREE);
		Double decimal = (Double) forDecimal(numberFormat).parse(text, pos);
		if (pos.getErrorIndex() > pos.getIndex()) {
			throw new ParseException(
					String.format("Cannot parse '%s', error at %d:%d.",
							text, pos.getIndex(), pos.getErrorIndex()),
					pos.getErrorIndex());
		} else if (decimal != null) {
			return new Bearing(Quantities.getQuantity(decimal, DEGREE));
		} else if (text.length() > pos.getIndex()) {
			String abbrev = text.substring(pos.getIndex());
			return Hemisphere.fromAbbreviation(abbrev).map(h -> {
				if (Limit.LATITUDE.hemispheres().contains(h)) {
					return new Latitude(h.positive() ? angle : (ComparableQuantity<Angle>) angle.negate());
				} else if (Limit.LONGITUDE.hemispheres().contains(h)) {
					return new Longitude(h.positive() ? angle : (ComparableQuantity<Angle>) angle.negate());
				} else {
					return null;
				}
			}).orElseThrow(() -> new ParseException(
					String.format("Invalid compass direction '%s' at %d", abbrev, pos.getIndex()),
					pos.getIndex()));
		}
		return new Bearing(angle);
	}


	@NonNull
	@Override
	public String print(@NonNull Compass compass, @NonNull Locale locale) {
		DecimalFormat format = new DecimalFormat("#0", new DecimalFormatSymbols(locale));
		Number degrees = Math.abs(compass.to(DEGREE).getValue().doubleValue());
		Number minutes = (degrees.doubleValue() - degrees.intValue()) * 60;
		Number seconds = (minutes.doubleValue() - minutes.intValue()) * 60;
		return String.format("%s%s%s%s",
				forDegrees(format).format(degrees.intValue()),
				forMinutes(format).format(minutes.intValue()),
				forSeconds(format).format(seconds),
				withHemisphere(compass));
	}

	private String withHemisphere(@NonNull Compass compass) {
		return compass instanceof Spheriod spheriod ?
				spheriod.hemisphere(compass.getValue().intValue() >= 0)
						.map(Hemisphere::abbreviation).orElse("") : "";
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
		format.applyLocalizedPattern("#0.####\"");
		format.setParseIntegerOnly(false);
		return format;
	}

	private DecimalFormat forDecimal(DecimalFormat format) {
		format.applyLocalizedPattern("##0.######°");
		format.setParseIntegerOnly(false);
		return format;
	}

}