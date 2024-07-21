package uk.co.bluegecko.ui.geometry.javafx.event;

import javafx.stage.Stage;
import org.springframework.context.ApplicationEvent;

public class StageEvent extends ApplicationEvent {


	public StageEvent(Stage stage) {
		super(stage);
	}

	public Stage getStage() {
		return (Stage) getSource();
	}
	
}