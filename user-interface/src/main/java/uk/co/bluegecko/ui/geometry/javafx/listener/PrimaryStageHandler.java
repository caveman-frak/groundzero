package uk.co.bluegecko.ui.geometry.javafx.listener;

import java.io.IOException;
import java.net.URL;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.rgielen.fxweaver.core.FxWeaver;
import org.springframework.context.ApplicationListener;
import uk.co.bluegecko.ui.geometry.javafx.event.StageEvent;

@Slf4j
@RequiredArgsConstructor
public abstract class PrimaryStageHandler implements ApplicationListener<StageEvent> {

	protected final FxWeaver fxWeaver;

	@Override
	public void onApplicationEvent(StageEvent event) {
		Stage stage = event.getStage();
		if (!event.isPrimary()) {
			log.info("Ignoring non-primary stage event");
			return;
		}
		switch (event.getLifecycle()) {
			case READY -> stageReady(stage);
		}
	}

	protected abstract void stageReady(Stage stage);

	protected void loadIcons(Stage stage, String... locations) {
		for (String location : locations) {
			try {
				URL resource = getClass().getResource(location);
				if (resource != null) {
					stage.getIcons().add(new Image(resource.openStream()));
				} else {
					log.warn("Skipping icon '{}' as resource missing", location);
				}
			} catch (IOException e) {
				log.warn("Unable to load icon '{}' due to {}", location, e.getMessage(), e);
			}
		}
	}

}