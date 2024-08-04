package uk.co.bluegecko.ui.geometry.javafx.control;

import java.util.regex.Pattern;
import javafx.util.converter.IntegerStringConverter;

public class NumericField extends RestrictedField<Integer> {

	private static final Pattern PATTERN = Pattern.compile("[+-]?\\d*");

	public NumericField(int defaultValue) {
		super(new IntegerStringConverter(), defaultValue, change -> matchesPattern(PATTERN, change));
	}

	public NumericField() {
		this(0);
	}

}