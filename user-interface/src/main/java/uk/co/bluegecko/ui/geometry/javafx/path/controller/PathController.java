package uk.co.bluegecko.ui.geometry.javafx.path.controller;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextArea;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import net.rgielen.fxweaver.core.FxmlView;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@FxmlView("/views/path.fxml")
public class PathController implements Initializable {

	@FXML
	private TextArea pathText;
	@Setter
	private GraphicsController graphicsController;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		log.info("Initialising. URL = {}, Bundle = {}", location, resources);
		pathText.setText("""
				M 500 500
				c75 0 50 100 0 100
				c-150 0 -100 -200 0 -200
				c 225 0 150 300 0 300
				c-300 0 -200 -400 0 -400
				c375 0 250 500 0 500
				c-450 0 -300 -600 0 -600
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
}