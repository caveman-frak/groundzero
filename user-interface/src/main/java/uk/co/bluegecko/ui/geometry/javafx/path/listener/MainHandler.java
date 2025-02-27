package uk.co.bluegecko.ui.geometry.javafx.path.listener;

import java.util.ResourceBundle;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.MenuBar;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import net.rgielen.fxweaver.core.FxControllerAndView;
import net.rgielen.fxweaver.core.FxWeaver;
import org.springframework.stereotype.Component;
import uk.co.bluegecko.ui.geometry.javafx.common.controller.StatusController;
import uk.co.bluegecko.ui.geometry.javafx.control.XYCanvas;
import uk.co.bluegecko.ui.geometry.javafx.listener.PrimaryStageHandler;
import uk.co.bluegecko.ui.geometry.javafx.path.controller.GraphicsController;
import uk.co.bluegecko.ui.geometry.javafx.path.controller.MenuController;
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
		FxControllerAndView<GraphicsController, XYCanvas> graphicsLoader = fxWeaver.load(GraphicsController.class,
				rb);
		FxControllerAndView<StatusController, HBox> statusLoader = fxWeaver.load(StatusController.class, rb);
		FxControllerAndView<MenuController, MenuBar> menuLoader = fxWeaver.load(MenuController.class, rb);
		BorderPane border = new BorderPane();
		pathLoader.getView().ifPresent(border::setTop);
		graphicsLoader.getView().ifPresent(border::setCenter);
		statusLoader.getView().ifPresent(border::setBottom);

		Scene scene = new Scene(
				menuLoader.getView().map(m -> (Parent) new VBox(m, border))
						.orElse(border));

		scene.getStylesheets().add(getClass().getResource("/styles/styles.css").toExternalForm());

		stage.setScene(scene);
		stage.show();
	}
}