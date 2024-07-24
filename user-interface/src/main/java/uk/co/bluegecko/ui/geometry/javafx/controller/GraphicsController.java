package uk.co.bluegecko.ui.geometry.javafx.controller;

import java.awt.Point;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.net.URL;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.shape.Shape;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import net.rgielen.fxweaver.core.FxmlView;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Component;
import uk.co.bluegecko.ui.geometry.javafx.concurrent.PeriodicPulse;

@Slf4j
@Component
@FxmlView("/views/graphics.fxml")
public class GraphicsController implements Initializable {

	private static final int SPACING = 20;
	private static final String SHAPE = "shape";
	private static final String POINTS = "points";
	private static final String MARKER = "markers";

	private ResourceBundle rb;

	@FXML
	private Pane canvas;

	@Setter
	private StatusController statusController;

	public void drawGrid() {
		Group grid = new Group();
		grid.setId("grid");
		double width = canvas.getWidth() - 10;
		double height = canvas.getHeight() - 10;
		for (int i = 0; i <= width / SPACING; i++) {
			grid.getChildren().add(line(5 + i * SPACING, 5, 5 + i * SPACING, (int) (height - 5)));
		}
		for (int i = 0; i <= height / SPACING; i++) {
			grid.getChildren().add(line(5, 5 + i * SPACING, (int) (width - 5), 5 + i * SPACING));
		}
		canvas.getChildren().add(grid);
	}

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
		statusController.status("%s has a length of %1.2f", shape, length);
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
		statusController.status("Clicked at [%1.1f,%1.1f] on %s", e.getX(), e.getY(), e.getSource());
	}

	private ObservableList<Node> getOrAdd(Pane parent, String name) {
		ObservableList<Node> children = parent.getChildren();
		Optional<Node> group = children.filtered(n -> name.equals(n.getId()))
				.filtered(n -> n instanceof Group)
				.stream().findFirst();
		if (group.isPresent()) {
			return ((Group) group.get()).getChildren();
		} else {
			Group node = new Group();
			node.setId(name);
			children.add(node);
			return node.getChildren();
		}
	}

	public void clearGraphics() {
		canvas.getChildren().removeIf(n -> MARKER.equals(n.getId()));
		canvas.getChildren().removeIf(n -> SHAPE.equals(n.getId()));
		canvas.getChildren().removeIf(n -> POINTS.equals(n.getId()));
		statusController.clearProgress();
	}

	private Line line(int x1, int y1, int x2, int y2) {
		Line line = new Line(x1, y1, x2, y2);
		line.setStroke(Color.gray(0.5, 0.5));
		line.setStrokeWidth(1.0);
		return line;
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		this.rb = resources;
	}

	private static class ShowPointsOverTime extends PeriodicPulse {

		private final ResourceBundle resourceBundle;
		private final List<? extends Shape> shapes;
		private final ObservableList<Node> children;

		public ShowPointsOverTime(ResourceBundle resourceBundle, Duration pause, int number,
				List<? extends Shape> shapes, ObservableList<Node> children) {
			super(pause, number);
			this.resourceBundle = resourceBundle;
			this.shapes = shapes;
			this.children = children;
		}

		@Override
		protected void run() {
			int i = getTotal().get() - shapes.size();
			updateMessage(resourceBundle.getString("drawing").formatted(i));
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