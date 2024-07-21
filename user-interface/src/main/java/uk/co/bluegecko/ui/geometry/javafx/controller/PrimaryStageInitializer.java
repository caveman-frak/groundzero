package uk.co.bluegecko.ui.geometry.javafx.controller;

import java.io.IOException;
import java.net.URL;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.rgielen.fxweaver.core.FxControllerAndView;
import net.rgielen.fxweaver.core.FxWeaver;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;
import uk.co.bluegecko.ui.geometry.javafx.event.StageEvent;

@Slf4j
@Component
@RequiredArgsConstructor
public class PrimaryStageInitializer implements ApplicationListener<StageEvent> {

	private final FxWeaver fxWeaver;

	@Override
	public void onApplicationEvent(StageEvent event) {
		Stage stage = event.getStage();
		stage.setTitle("Geometry Display");
		loadIcons(stage, "/images/geometry-icon-32.png");

		FxControllerAndView<GraphicsController, Node> graphicsLoader = fxWeaver.load(GraphicsController.class);
		FxControllerAndView<ControlsController, Node> controlsLoader = fxWeaver.load(ControlsController.class);
		FxControllerAndView<StatusController, Node> statusLoader = fxWeaver.load(StatusController.class);
		BorderPane root = new BorderPane();
		graphicsLoader.getView().ifPresent(root::setCenter);
		controlsLoader.getView().ifPresent(root::setTop);
		statusLoader.getView().ifPresent(root::setBottom);

		graphicsLoader.getController().setStatusController(statusLoader.getController());
		controlsLoader.getController().setGraphicsController(graphicsLoader.getController());

		Scene scene = new Scene(root);
		scene.getStylesheets().add(getClass().getResource("/styles/styles.css").toExternalForm());

		stage.setTitle("Geometry Display (FX)");
		stage.setScene(scene);
		stage.setOnShown(e -> graphicsLoader.getController().drawGrid());
		stage.show();
	}

	private void loadIcons(Stage stage, String... locations) {
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