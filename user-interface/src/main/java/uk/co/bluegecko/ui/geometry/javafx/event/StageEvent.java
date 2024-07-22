package uk.co.bluegecko.ui.geometry.javafx.event;

import javafx.stage.Stage;
import lombok.Getter;
import org.springframework.context.ApplicationEvent;

@Getter
public class StageEvent extends ApplicationEvent {

	private final boolean primary;
	private final StageLifecycle lifecycle;

	public StageEvent(Stage stage, boolean primary, StageLifecycle lifecycle) {
		super(stage);

		this.primary = primary;
		this.lifecycle = lifecycle;
	}

	public Stage getStage() {
		return (Stage) getSource();
	}

}