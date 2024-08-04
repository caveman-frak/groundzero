package uk.co.bluegecko.ui.geometry.javafx.control;

import java.util.regex.Pattern;
import javafx.util.converter.DoubleStringConverter;

public class DecimalField extends RestrictedField<Double> {

	private static final Pattern PATTERN = Pattern.compile("[+-]?\\d*\\.?\\d*");

	public DecimalField(double defaultValue) {
		super(new DoubleStringConverter(), defaultValue, change -> matchesPattern(PATTERN, change));
	}

	public DecimalField() {
		this(0);
	}

}