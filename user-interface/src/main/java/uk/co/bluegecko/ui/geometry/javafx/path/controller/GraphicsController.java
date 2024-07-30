package uk.co.bluegecko.ui.geometry.javafx.path.controller;

import javafx.fxml.Initializable;
import javafx.scene.paint.Color;
import javafx.scene.shape.SVGPath;
import lombok.extern.slf4j.Slf4j;
import net.rgielen.fxweaver.core.FxmlView;
import org.springframework.stereotype.Component;
import uk.co.bluegecko.ui.geometry.javafx.common.controller.BaseGraphicsController;

@Slf4j
@Component
@FxmlView("/views/graphics.fxml")
public class GraphicsController extends BaseGraphicsController implements Initializable {

	private static final String SHAPE = "shape";
	private static final String PATH = "path";

	@Override
	public void clearGraphics() {
		super.clearGraphics();
		canvas.getChildren().removeIf(n -> SHAPE.equals(n.getId()));
		canvas.getChildren().removeIf(n -> PATH.equals(n.getId()));
	}

	public void drawPath(String text) {
		SVGPath path = new SVGPath();
		path.setFill(Color.TRANSPARENT);
		path.setStroke(Color.BLACK);
		path.setContent(text);
		statusController.status(rb.getString("drawing-path"));
		getOrAdd(canvas, PATH).add(path);
	}

	public void animatePath() {
		statusController.status(rb.getString("animating-path"));
	}

}