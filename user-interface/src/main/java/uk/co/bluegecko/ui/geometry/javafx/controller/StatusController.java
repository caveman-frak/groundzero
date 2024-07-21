package uk.co.bluegecko.ui.geometry.javafx.controller;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import net.rgielen.fxweaver.core.FxmlView;
import org.springframework.stereotype.Component;
import uk.co.bluegecko.ui.geometry.javafx.concurrent.PeriodicPulse;

@Component
@FxmlView("/views/status.fxml")
public class StatusController implements Initializable {

	@FXML
	private Label status;
	@FXML
	private Label action;
	@FXML
	private ProgressBar progress;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
	}

	public void bindProgress(PeriodicPulse showPoints) {
		progress.progressProperty().bind(showPoints.progressProperty());
		action.textProperty().bind(showPoints.messageProperty());
	}

	public void clearProgress() {
		action.textProperty().unbind();
		action.setText("");
		progress.progressProperty().unbind();
		progress.setProgress(0);
	}
	
}