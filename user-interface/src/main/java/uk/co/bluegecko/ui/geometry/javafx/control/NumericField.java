package uk.co.bluegecko.ui.geometry.javafx.control;

import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.util.converter.IntegerStringConverter;

public class NumericField extends TextField {

	public NumericField(int defaultValue) {
		super();

		setTextFormatter(new TextFormatter<>(new IntegerStringConverter(), defaultValue, change -> {
			if (!change.getText().matches("\\d*")) {
				change.setText(""); //else make no change
				change.setRange(    //don't remove any selected text either.
						change.getRangeStart(),
						change.getRangeStart()
				);
			}
			return change;
		}));
	}

	public NumericField() {
		this(0);
	}

	public int value() {
		return (int) getTextFormatter().getValue();
	}

}