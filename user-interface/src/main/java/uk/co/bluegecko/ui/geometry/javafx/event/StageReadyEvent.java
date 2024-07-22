package uk.co.bluegecko.ui.geometry.javafx.event;

import javafx.stage.Stage;

public class StageReadyEvent extends StageEvent {

	public StageReadyEvent(Stage stage, boolean primary) {
		super(stage, primary, StageLifecycle.READY);
	}

}