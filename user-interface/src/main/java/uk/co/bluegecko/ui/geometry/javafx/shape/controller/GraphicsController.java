package uk.co.bluegecko.ui.geometry.javafx.shape.controller;

import java.awt.Point;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import javafx.collections.ObservableList;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.shape.Shape;
import lombok.extern.slf4j.Slf4j;
import net.rgielen.fxweaver.core.FxmlView;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Component;
import uk.co.bluegecko.ui.geometry.javafx.common.controller.BaseGraphicsController;
import uk.co.bluegecko.ui.geometry.javafx.concurrent.PeriodicPulse;

@Slf4j
@Component
@FxmlView("/views/graphics.fxml")
public class GraphicsController extends BaseGraphicsController implements Initializable {

	private static final String SHAPE = "shape";
	private static final String POINTS = "points";
	private static final String MARKER = "markers";

	public void drawShape(Shape shape) {
		shape.setFill(Color.TRANSPARENT);
		shape.setStroke(Color.BLACK);
		getOrAdd(canvas, SHAPE).add(shape);
	}

	public void drawMarkers(Stream<Point> points) {
		getOrAdd(canvas, MARKER).addAll(points.map(p -> new Circle(p.x, p.y, 2, Color.RED)).toList());
	}

	public void drawPoints(Stream<Point2D> points, Duration duration) {
		Stream<Circle> shapes = points.map(p -> new Circle(p.getX(), p.getY(), 2, Color.BLUE));
		PeriodicPulse showPoints = drawPointsOverTime(shapes, duration, getOrAdd(canvas, POINTS));
		statusController.bindProgress(showPoints);
		showPoints.start();
	}

	public void drawLines(Stream<Line2D> lines, Duration duration) {
		Stream<Shape> shapes = lines.map(p -> new Shape[]{new Circle(p.getX1(), p.getY1(), 2, Color.BLUE),
						new Line(p.getX1(), p.getY1(), p.getX2(), p.getY2())})
				.flatMap(Arrays::stream);
		PeriodicPulse showPoints = drawPointsOverTime(shapes, duration, getOrAdd(canvas, POINTS));
		statusController.bindProgress(showPoints);
		showPoints.start();
	}

	public void showLength(ControlsController.Shape shape, double length) {
		statusController.status(rb.getString("length"), shape, length);
	}

	@NotNull
	private PeriodicPulse drawPointsOverTime(Stream<? extends Shape> shapes, Duration duration,
			ObservableList<Node> children) {
		List<? extends Shape> mutableList =
				shapes.peek(s -> s.setOnMouseClicked(this::showTangent))
						.collect(Collectors.toCollection(ArrayList::new));
		int number = mutableList.size();
		Duration pause = duration.toSeconds() > 0 ? Duration.ofNanos(duration.toNanos() / number) : Duration.ZERO;

		return new ShowPointsOverTime(rb, pause, number, mutableList, children);
	}

	private void showTangent(MouseEvent e) {
		statusController.status(rb.getString("clicked"), e.getX(), e.getY(), e.getSource());
	}

	@Override
	public void clearGraphics() {
		super.clearGraphics();
		canvas.getChildren().removeIf(n -> MARKER.equals(n.getId()));
		canvas.getChildren().removeIf(n -> SHAPE.equals(n.getId()));
		canvas.getChildren().removeIf(n -> POINTS.equals(n.getId()));
	}

	private static class ShowPointsOverTime extends PeriodicPulse {

		private final ResourceBundle rb;
		private final List<? extends Shape> shapes;
		private final ObservableList<Node> children;

		public ShowPointsOverTime(ResourceBundle rb, Duration pause, int number,
				List<? extends Shape> shapes, ObservableList<Node> children) {
			super(pause, number);
			this.rb = rb;
			this.shapes = shapes;
			this.children = children;
		}

		@Override
		protected void run() {
			int i = getTotal().get() - shapes.size();
			updateMessage(rb.getString("drawing").formatted(i));
			updateProgress(i);
			if (!shapes.isEmpty()) {
				Shape point = shapes.removeFirst();
				children.add(point);
			} else {
				updateMessage("");
				updateProgress(0);
				stop();
			}
		}

	}

}