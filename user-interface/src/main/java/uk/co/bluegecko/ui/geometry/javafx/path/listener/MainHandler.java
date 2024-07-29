package uk.co.bluegecko.ui.geometry.javafx.path.listener;

import java.util.ResourceBundle;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import net.rgielen.fxweaver.core.FxControllerAndView;
import net.rgielen.fxweaver.core.FxWeaver;
import org.springframework.stereotype.Component;
import uk.co.bluegecko.ui.geometry.javafx.common.controller.StatusController;
import uk.co.bluegecko.ui.geometry.javafx.listener.PrimaryStageHandler;
import uk.co.bluegecko.ui.geometry.javafx.path.controller.GraphicsController;
import uk.co.bluegecko.ui.geometry.javafx.path.controller.PathController;

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

		FxControllerAndView<PathController, AnchorPane> pathLoader = fxWeaver.load(PathController.class, rb);
		FxControllerAndView<GraphicsController, Pane> graphicsLoader = fxWeaver.load(GraphicsController.class,
				rb);
		FxControllerAndView<StatusController, HBox> statusLoader = fxWeaver.load(StatusController.class, rb);
		BorderPane root = new BorderPane();
		pathLoader.getView().ifPresent(root::setTop);
		graphicsLoader.getView().ifPresent(root::setCenter);
		statusLoader.getView().ifPresent(root::setBottom);

		graphicsLoader.getController().setStatusController(statusLoader.getController());
		pathLoader.getController().setGraphicsController(graphicsLoader.getController());

		Scene scene = new Scene(root);
		scene.getStylesheets().add(getClass().getResource("/styles/styles.css").toExternalForm());

		stage.setScene(scene);
		stage.setOnShown(_ -> graphicsLoader.getController().drawGrid());
		stage.show();
	}
}