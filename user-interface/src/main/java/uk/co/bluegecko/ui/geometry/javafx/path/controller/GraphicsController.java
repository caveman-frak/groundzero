package uk.co.bluegecko.ui.geometry.javafx.path.controller;

import java.net.URL;
import java.util.Optional;
import java.util.Random;
import java.util.ResourceBundle;
import java.util.concurrent.atomic.AtomicReference;
import java.util.random.RandomGenerator;
import javafx.animation.Animation;
import javafx.animation.Animation.Status;
import javafx.animation.PathTransition;
import javafx.animation.PathTransition.OrientationType;
import javafx.event.ActionEvent;
import javafx.scene.control.Tooltip;
import javafx.scene.paint.Color;
import javafx.scene.shape.SVGPath;
import javafx.scene.shape.Shape;
import javafx.util.Duration;
import lombok.extern.slf4j.Slf4j;
import net.rgielen.fxweaver.core.FxmlView;
import org.springframework.stereotype.Component;
import uk.co.bluegecko.ui.geometry.javafx.common.controller.BaseGraphicsController;
import uk.co.bluegecko.ui.geometry.javafx.path.model.Vessel;
import uk.co.bluegecko.ui.geometry.javafx.path.model.VesselShape;

@Slf4j
@Component
@FxmlView("/views/graphics.fxml")
public class GraphicsController extends BaseGraphicsController {

	private static final String VESSEL = "vessel";
	private static final String PATH = "path";

	private final RandomGenerator randomGenerator;
	private final AtomicReference<Shape> path;
	private final AtomicReference<Animation> transition;
	private VesselUpdater vesselUpdater;

	public GraphicsController() {
		super();

		randomGenerator = new Random();
		path = new AtomicReference<>();
		transition = new AtomicReference<>();
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		super.initialize(location, resources);

		vesselUpdater = new VesselUpdater(getOrAdd(canvas, VESSEL));
	}

	@Override
	public void clearGraphics() {
		super.clearGraphics();
		canvas.getChildren().removeIf(n -> VESSEL.equals(n.getId()));
		canvas.getChildren().removeIf(n -> PATH.equals(n.getId()));
		path.set(null);
		Optional.ofNullable(transition.getAndSet(null)).ifPresent(Animation::stop);
	}

	public void drawPath(String text) {
		SVGPath path = new SVGPath();
		path.setFill(Color.TRANSPARENT);
		path.setStroke(Color.BLACK);
		path.setContent(text);
		statusController.status(rb.getString("drawing-path"));
		getOrAdd(canvas, PATH).add(path);
		this.path.set(path);
	}

	public void animatePath() {
		if (path.get() != null) {
			if (transition.get() == null) {
				Shape vessel = drawVessel();
				PathTransition animation = new PathTransition(Duration.seconds(30.0), path.get(), vessel);
				animation.setOrientation(OrientationType.ORTHOGONAL_TO_TANGENT);
				animation.setCycleCount(1);
				transition.set(animation);
				animation.setOnFinished(this::stopUpdater);
				statusController.bindProgress(vesselUpdater);
				vesselUpdater.start();
				animation.play();
			} else if (transition.get().getStatus() == Status.RUNNING) {
				transition.get().pause();
			} else if (transition.get().getStatus() == Status.PAUSED) {
				transition.get().play();
			}
		} else {
			statusController.status("No path defined!");
		}
	}

	private void stopUpdater(ActionEvent e) {
		vesselUpdater.stop();
		statusController.clearProgress();
	}


	private Vessel drawVessel() {
		VesselShape vesselShape = randomVessel(randomGenerator);
		Tooltip tooltip = new Tooltip();
		tooltip.setShowDelay(Duration.millis(100));
		Vessel vessel = new Vessel(tooltip, vesselShape.outline());
		vessel.setFill(Color.LIGHTBLUE);
		vessel.setStroke(Color.BLUE);
		getOrAdd(canvas, VESSEL).add(vessel);
		Tooltip.install(vessel, tooltip);
		return vessel;
	}

	private VesselShape randomVessel(RandomGenerator randomGenerator) {
		return VesselShape.values()[randomGenerator.nextInt(VesselShape.values().length)];
	}

}