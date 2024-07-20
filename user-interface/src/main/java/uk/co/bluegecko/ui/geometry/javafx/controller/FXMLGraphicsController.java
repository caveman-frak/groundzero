package uk.co.bluegecko.ui.geometry.javafx.controller;

import java.awt.Point;
import java.awt.geom.Point2D;
import java.net.URL;
import java.time.Duration;
import java.util.ResourceBundle;
import java.util.stream.Stream;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Group;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.shape.Shape;

public class FXMLGraphicsController implements Initializable {

	private static final int SPACING = 20;
	private static final String SHAPE = "shape";
	private static final String POINTS = "points";
	private static final String MARKER = "markers";

	@FXML
	private Pane canvas;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
	}

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
		Group group = new Group();
		group.setId(SHAPE);
		shape.setFill(Color.TRANSPARENT);
		shape.setStroke(Color.BLACK);
		group.getChildren().add(shape);
		canvas.getChildren().add(group);
	}

	public void drawMarkers(Stream<Point> points) {
		Group group = new Group();
		group.setId(MARKER);
		group.getChildren().addAll(points.map(p -> new Circle(p.x, p.y, 2, Color.RED)).toList());
		canvas.getChildren().add(group);
	}

	public void drawPoints(Stream<Point2D> points, Duration duration) {
		Group group = new Group();
		group.setId(POINTS);
		group.getChildren().addAll(points.map(p -> new Circle(p.getX(), p.getY(), 2, Color.BLUE)).toList());
		canvas.getChildren().add(group);
	}

	public void clearGraphics() {
		canvas.getChildren().removeIf(n -> MARKER.equals(n.getId()));
		canvas.getChildren().removeIf(n -> SHAPE.equals(n.getId()));
		canvas.getChildren().removeIf(n -> POINTS.equals(n.getId()));
	}

	private Line line(int x1, int y1, int x2, int y2) {
		Line line = new Line(x1, y1, x2, y2);
		line.setStroke(Color.gray(0.5));
		line.setStrokeWidth(1.0);
		return line;
	}

}