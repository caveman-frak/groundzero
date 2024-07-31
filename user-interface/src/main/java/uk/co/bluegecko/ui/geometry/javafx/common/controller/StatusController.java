package uk.co.bluegecko.ui.geometry.javafx.common.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import net.rgielen.fxweaver.core.FxmlView;
import org.springframework.stereotype.Component;
import uk.co.bluegecko.ui.geometry.javafx.animation.PeriodicPulse;

@Component
@FxmlView("/views/status.fxml")
public class StatusController {

	@FXML
	private Label status;
	@FXML
	private Label message;
	@FXML
	private ProgressBar progress;

	public void bindProgress(PeriodicPulse showPoints) {
		status.textProperty().bind(showPoints.statusProperty());
		message.textProperty().bind(showPoints.messageProperty());
		progress.progressProperty().bind(showPoints.progressProperty());
	}

	public void clearProgress() {
		status.textProperty().unbind();
		status.setText("");
		message.textProperty().unbind();
		message.setText("");
		progress.progressProperty().unbind();
		progress.setProgress(0);
	}

	public void status(String status, Object... args) {
		this.status.setText(String.format(status, args));
	}

	public void message(String message, Object... args) {
		this.message.setText(String.format(message, args));
	}

}