package uk.co.bluegecko.ui.geometry.javafx.common.controller;


import static java.lang.Math.abs;

import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.concurrent.atomic.AtomicReference;
import javafx.beans.property.ReadOnlyDoubleProperty;
import javafx.beans.value.ChangeListener;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Bounds;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import lombok.Setter;

public abstract class BaseGraphicsController implements Initializable {

	private static final int SPACING = 20;
	private static final String GRID = "grid";

	protected ResourceBundle rb;
	private final AtomicReference<Bounds> canvasBounds = new AtomicReference<>();

	@Setter
	protected StatusController statusController;

	@FXML
	protected Pane canvas;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		rb = resources;
		canvas.widthProperty().addListener(resizeListener());
		canvas.heightProperty().addListener(resizeListener());
	}

	public void drawGrid() {
		drawGrid(canvas.getBoundsInLocal());
	}

	private void drawGrid(Bounds bounds) {
		ObservableList<Node> grid = getOrAdd(canvas, GRID);
		grid.clear();
		double width = bounds.getWidth() - 10;
		double height = bounds.getHeight() - 10;
		for (int i = 0; i <= width / SPACING; i++) {
			grid.add(line(5 + i * SPACING, 5, 5 + i * SPACING, (int) (height - 5)));
		}
		for (int i = 0; i <= height / SPACING; i++) {
			grid.add(line(5, 5 + i * SPACING, (int) (width - 5), 5 + i * SPACING));
		}
	}

	public void clearGraphics() {
		statusController.clearProgress();
	}

	private ChangeListener<Number> resizeListener() {
		return (observable, _, _) -> {
			if (exceedsThreshold((ReadOnlyDoubleProperty) observable, 10.0)) {
				drawGrid(canvasBounds.getAndSet(null));
			}
		};
	}

	protected boolean exceedsThreshold(ReadOnlyDoubleProperty property, Double threshold) {
		canvasBounds.compareAndSet(null, ((Pane) property.getBean()).getBoundsInLocal());
		if ("height".equals(property.getName())) {
			return abs(canvasBounds.get().getHeight() - property.getValue()) > threshold;
		} else if ("width".equals(property.getName())) {
			return abs(canvasBounds.get().getWidth() - property.getValue()) > threshold;
		}
		return false;
	}

	private Line line(int x1, int y1, int x2, int y2) {
		Line line = new Line(x1, y1, x2, y2);
		line.setStroke(Color.gray(0.5, 0.5));
		line.setStrokeWidth(1.0);
		return line;
	}

	protected ObservableList<Node> getOrAdd(Pane parent, String name) {
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

}