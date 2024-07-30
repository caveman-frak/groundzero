package uk.co.bluegecko.ui.geometry.javafx.path.controller;

import java.util.Optional;
import java.util.Random;
import java.util.concurrent.atomic.AtomicReference;
import java.util.random.RandomGenerator;
import javafx.animation.Animation;
import javafx.animation.PathTransition;
import javafx.animation.PathTransition.OrientationType;
import javafx.fxml.Initializable;
import javafx.scene.paint.Color;
import javafx.scene.shape.Polygon;
import javafx.scene.shape.SVGPath;
import javafx.scene.shape.Shape;
import javafx.util.Duration;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.Accessors;
import lombok.experimental.FieldDefaults;
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

	private final RandomGenerator randomGenerator = new Random();
	private final AtomicReference<Shape> path = new AtomicReference<>();
	private final AtomicReference<Animation> transition = new AtomicReference<>();

	@Override
	public void clearGraphics() {
		super.clearGraphics();
		canvas.getChildren().removeIf(n -> SHAPE.equals(n.getId()));
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
				PathTransition transition = new PathTransition(Duration.seconds(10.0), path.get(), vessel);
				transition.setOrientation(OrientationType.ORTHOGONAL_TO_TANGENT);
				transition.setCycleCount(1);
				this.transition.set(transition);
			}
			statusController.status(rb.getString("animating-path"));
			transition.get().playFromStart();
		} else {
			statusController.status("No path defined!");
		}
	}

	private Shape drawVessel() {
		Polygon vessel = new Polygon(randomVessel(randomGenerator));
		vessel.setFill(Color.LIGHTBLUE);
		vessel.setStroke(Color.BLUE);
		getOrAdd(canvas, SHAPE).add(vessel);
		return vessel;
	}

	private double[] randomVessel(RandomGenerator randomGenerator) {
		return VesselType.values()[randomGenerator.nextInt(VesselType.values().length)]
				.outline();
	}

	@RequiredArgsConstructor
	@Getter
	@Accessors(fluent = true)
	@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
	private enum VesselType {
		SLOW(new double[]{
				190, 195,
				190, 205,
				205, 205,
				210, 200,
				205, 195}, false),
		SLOW_TAG(new double[]{
				190, 195,
				195, 200,
				190, 205,
				205, 205,
				210, 200,
				205, 195}, true),
		FAST(new double[]{
				190, 195,
				190, 205,
				210, 205,
				210, 195}, false),
		FAST_TAG(new double[]{
				190, 195,
				195, 200,
				190, 205,
				210, 205,
				210, 195}, true),
		TANKER(new double[]{
				190, 195,
				190, 205,
				210, 200}, false),
		TANKER_TAG(new double[]{
				190, 195,
				195, 200,
				190, 205,
				210, 200}, true);

		double[] outline;
		boolean tagged;
	}

}