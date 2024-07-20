package uk.co.bluegecko.ui.geometry.javafx;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import lombok.extern.slf4j.Slf4j;
import uk.co.bluegecko.ui.geometry.javafx.controller.FXMLControlsController;
import uk.co.bluegecko.ui.geometry.javafx.controller.FXMLGraphicsController;

@Slf4j
public class GeometryDisplay extends Application {

	@Override
	public void init() throws Exception {
		super.init();
		log.info("Initialising application");
	}

	@Override
	public void stop() throws Exception {
		log.info("Stopping application");
		super.stop();
	}

	@Override
	public void start(Stage stage) throws Exception {
		log.info("Starting application");
		FXMLLoader graphicsLoader = new FXMLLoader(getClass().getResource("/views/graphics.fxml"));
		FXMLLoader controlsLoader = new FXMLLoader(getClass().getResource("/views/controls.fxml"));

		BorderPane root = new BorderPane(graphicsLoader.load());
		root.setTop(controlsLoader.load());

		FXMLGraphicsController graphicsController = graphicsLoader.getController();
		((FXMLControlsController) controlsLoader.getController()).setGraphicsController(graphicsController);

		Scene scene = new Scene(root);
		scene.getStylesheets().add(getClass().getResource("/styles/styles.css").toExternalForm());

		stage.setTitle("Geometry Display (FX)");
		stage.setScene(scene);
		stage.show();

		graphicsController.drawGrid();
	}

	public static void main(String[] args) {
		launch(args);
	}

}