package uk.co.bluegecko.ui.geometry.javafx.shape.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import net.rgielen.fxweaver.core.FxmlView;
import org.springframework.stereotype.Component;
import uk.co.bluegecko.ui.geometry.javafx.concurrent.PeriodicPulse;

@Component
@FxmlView("/views/status.fxml")
public class StatusController {

	@FXML
	private Label status;
	@FXML
	private Label action;
	@FXML
	private ProgressBar progress;

	public void bindProgress(PeriodicPulse showPoints) {
		progress.progressProperty().bind(showPoints.progressProperty());
		action.textProperty().bind(showPoints.messageProperty());
	}

	public void clearProgress() {
		status.setText("");
		action.textProperty().unbind();
		action.setText("");
		progress.progressProperty().unbind();
		progress.setProgress(0);
	}

	public void status(String message, Object... args) {
		status.setText(String.format(message, args));
	}

}