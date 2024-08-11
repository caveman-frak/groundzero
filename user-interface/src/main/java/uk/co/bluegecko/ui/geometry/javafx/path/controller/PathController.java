package uk.co.bluegecko.ui.geometry.javafx.path.controller;

import static uk.co.bluegecko.ui.geometry.javafx.path.spiral.Element.asString;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextArea;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.rgielen.fxweaver.core.FxmlView;
import org.springframework.stereotype.Component;
import uk.co.bluegecko.ui.geometry.javafx.path.spiral.SpiralDefinerDialog;
import uk.co.bluegecko.ui.geometry.javafx.path.spiral.SpiralDefinition;

@Slf4j
@Component
@RequiredArgsConstructor
@FxmlView("/views/path.fxml")
public class PathController implements Initializable {
 
	@FXML
	private TextArea pathText;

	private final GraphicsController graphicsController;

	private ResourceBundle resourceBundle;
	private SpiralDefinition definition;

	@Override
	public void initialize(URL location, ResourceBundle resourceBundle) {
		this.resourceBundle = resourceBundle;

		log.info("Initialising. URL = {}, Bundle = {}", location, resourceBundle);
		pathText.setText("""
				M -100 -100
				l 200 0
				l 0 200
				l -200 0
				Z""");
	}

	public void drawPath(ActionEvent e) {
		graphicsController.drawPath(pathText.getText());
	}

	public void clearGraphics(ActionEvent e) {
		graphicsController.clearGraphics();
	}

	public void animatePath(ActionEvent e) {
		graphicsController.animatePath();
	}

	public void defineSpiral(ActionEvent e) {
		SpiralDefinerDialog dialog = new SpiralDefinerDialog(
				definition != null ? definition : new SpiralDefinition(), resourceBundle);

		dialog.showAndWait().ifPresent(d -> {
			definition = d;
			pathText.setText(asString(d.generateSpiral()));
		});

	}

}