package uk.co.bluegecko.ui.geometry.javafx.path.controller;


import java.net.URL;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import lombok.RequiredArgsConstructor;
import net.rgielen.fxweaver.core.FxmlView;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@FxmlView("/views/menu.fxml")
public class MenuController implements Initializable {

	private final PathController pathController;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
	}

	public void drawPath(ActionEvent e) {
		pathController.drawPath(e);
	}

	public void clearGraphics(ActionEvent e) {
		pathController.clearGraphics(e);
	}

	public void animatePath(ActionEvent e) {
		pathController.animatePath(e);
	}

	public void defineSpiral(ActionEvent e) {
		pathController.defineSpiral(e);
	}

	public void exit(ActionEvent e) {
		Platform.exit();
	}

}