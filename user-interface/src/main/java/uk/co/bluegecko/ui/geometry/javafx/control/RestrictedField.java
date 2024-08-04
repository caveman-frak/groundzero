package uk.co.bluegecko.ui.geometry.javafx.control;

import java.util.function.UnaryOperator;
import java.util.regex.Pattern;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.scene.control.TextFormatter.Change;
import javafx.util.StringConverter;
import org.jetbrains.annotations.NotNull;

public class RestrictedField<T> extends TextField {

	protected RestrictedField(TextFormatter<T> textFormatter) {
		super();

		setTextFormatter(textFormatter);
	}

	protected RestrictedField(StringConverter<T> converter, T defaultValue, UnaryOperator<Change> restriction) {
		this(new TextFormatter<>(converter, defaultValue, restriction));
	}

	@SuppressWarnings("unchecked")
	public T value() {
		return (T) getTextFormatter().getValue();
	}

	@NotNull
	protected static Change matchesPattern(Pattern pattern, Change change) {
		if (!pattern.matcher(change.getText()).matches()) {
			change.setText("");
			change.setRange(change.getRangeStart(), change.getRangeStart());
		}
		return change;
	}

}