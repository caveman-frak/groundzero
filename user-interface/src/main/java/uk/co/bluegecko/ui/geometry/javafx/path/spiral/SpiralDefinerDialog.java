package uk.co.bluegecko.ui.geometry.javafx.path.spiral;

import java.io.IOException;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Point2D;
import javafx.scene.control.ButtonType;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Dialog;
import uk.co.bluegecko.ui.geometry.javafx.control.DecimalField;
import uk.co.bluegecko.ui.geometry.javafx.control.NumericField;

public class SpiralDefinerDialog extends Dialog<SpiralDefinition> {

	@FXML
	NumericField centerX;
	@FXML
	NumericField centerY;
	@FXML
	NumericField radius;
	@FXML
	NumericField segments;
	@FXML
	DecimalField skew;
	@FXML
	CheckBox clockwise;

	public SpiralDefinerDialog(SpiralDefinition definition, ResourceBundle resourceBundle) {
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/spiral-dialog.fxml"),
					resourceBundle);
			loader.setController(this);
			setDialogPane(loader.load());

			fromDefinition(definition);
			setResultConverter(this::toDefinition);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	private SpiralDefinition toDefinition(ButtonType buttonType) {
		if (buttonType.getButtonData().isCancelButton()) {
			return null;
		} else {
			return new SpiralDefinition(
					new Point2D(centerX.value(), centerY.value()),
					radius.value(),
					segments.value(),
					skew.value(),
					clockwise.isSelected());
		}
	}

	private void fromDefinition(SpiralDefinition definition) {
		NumberFormat formatter = new DecimalFormat("0");
		centerX.setText(formatter.format(definition.getCenter().getX()));
		centerY.setText(formatter.format(definition.getCenter().getY()));
		radius.setText(formatter.format(definition.getRadius()));
		segments.setText(formatter.format(definition.getSegments()));
		formatter.setParseIntegerOnly(false);
		formatter.setMaximumFractionDigits(2);
		skew.setText(formatter.format(definition.getSkew()));
		clockwise.setSelected(definition.isClockwise());
	}

}