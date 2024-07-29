package uk.co.bluegecko.ui.geometry.javafx.shape.listener;

import java.util.ResourceBundle;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import net.rgielen.fxweaver.core.FxControllerAndView;
import net.rgielen.fxweaver.core.FxWeaver;
import org.springframework.stereotype.Component;
import uk.co.bluegecko.ui.geometry.javafx.listener.PrimaryStageHandler;
import uk.co.bluegecko.ui.geometry.javafx.shape.controller.ControlsController;
import uk.co.bluegecko.ui.geometry.javafx.shape.controller.GraphicsController;
import uk.co.bluegecko.ui.geometry.javafx.shape.controller.StatusController;

@Component
public class MainHandler extends PrimaryStageHandler {

	public MainHandler(FxWeaver fxWeaver) {
		super(fxWeaver);
	}

	@Override
	protected void stageReady(Stage stage) {
		ResourceBundle rb = ResourceBundle.getBundle("messages.geometry");
		stage.setTitle(rb.getString("title"));
		loadIcons(stage, "/images/geometry-icon-16.png",
				"/images/geometry-icon-32.png",
				"/images/geometry-icon-64.png");

		FxControllerAndView<GraphicsController, Node> graphicsLoader = fxWeaver.load(GraphicsController.class, rb);
		FxControllerAndView<ControlsController, Node> controlsLoader = fxWeaver.load(ControlsController.class, rb);
		FxControllerAndView<StatusController, Node> statusLoader = fxWeaver.load(StatusController.class, rb);
		BorderPane root = new BorderPane();
		graphicsLoader.getView().ifPresent(root::setCenter);
		controlsLoader.getView().ifPresent(root::setTop);
		statusLoader.getView().ifPresent(root::setBottom);

		graphicsLoader.getController().setStatusController(statusLoader.getController());
		controlsLoader.getController().setGraphicsController(graphicsLoader.getController());

		Scene scene = new Scene(root);
		scene.getStylesheets().add(getClass().getResource("/styles/styles.css").toExternalForm());

		stage.setScene(scene);
		stage.setOnShown(e -> graphicsLoader.getController().drawGrid());
		stage.show();
	}
}