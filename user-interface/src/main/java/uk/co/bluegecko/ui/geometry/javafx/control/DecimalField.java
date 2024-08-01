package uk.co.bluegecko.ui.geometry.javafx.control;

import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.util.converter.DoubleStringConverter;

public class DecimalField extends TextField {

	public DecimalField(double defaultValue) {
		super();

		setTextFormatter(new TextFormatter<>(new DoubleStringConverter(), defaultValue, change -> {
			if (!change.getText().matches("\\d*\\.?\\d*")) {
				change.setText(""); //else make no change
				change.setRange(    //don't remove any selected text either.
						change.getRangeStart(),
						change.getRangeStart()
				);
			}
			return change;
		}));
	}

	public DecimalField() {
		this(0);
	}

	public double value() {
		return (double) getTextFormatter().getValue();
	}

}