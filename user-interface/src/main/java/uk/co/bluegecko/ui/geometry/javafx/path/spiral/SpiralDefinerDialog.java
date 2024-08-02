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

	private final NumberFormat integerFormat;
	private final NumberFormat decimalFormat;
	@FXML
	NumericField centerX;
	@FXML
	NumericField centerY;
	@FXML
	NumericField radius;
	@FXML
	NumericField segments;
	@FXML
	DecimalField skew1;
	@FXML
	DecimalField skew2;
	@FXML
	CheckBox clockwise;

	public SpiralDefinerDialog(SpiralDefinition definition, ResourceBundle resourceBundle) {
		integerFormat = new DecimalFormat("0");
		decimalFormat = new DecimalFormat("0.##");
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
			return SpiralDefinition.builder()
					.center(new Point2D(centerX.value(), centerY.value()))
					.radius(radius.value())
					.segments(segments.value())
					.skew1(skew1.value())
					.skew2(skew2.value())
					.clockwise(clockwise.isSelected())
					.build();
		}
	}

	private void fromDefinition(SpiralDefinition definition) {
		centerX.setText(integerFormat.format(definition.getCenter().getX()));
		centerY.setText(integerFormat.format(definition.getCenter().getY()));
		radius.setText(integerFormat.format(definition.getRadius()));
		segments.setText(integerFormat.format(definition.getSegments()));
		skew1.setText(decimalFormat.format(definition.getSkew1()));
		skew2.setText(decimalFormat.format(definition.getSkew2()));
		clockwise.setSelected(definition.isClockwise());
	}

}