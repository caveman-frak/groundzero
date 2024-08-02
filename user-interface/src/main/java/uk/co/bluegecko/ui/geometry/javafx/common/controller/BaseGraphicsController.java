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
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import uk.co.bluegecko.ui.geometry.javafx.control.XYCanvas;

@Slf4j
public abstract class BaseGraphicsController implements Initializable {

	protected ResourceBundle rb;
	private final AtomicReference<Bounds> canvasBounds = new AtomicReference<>();

	@Setter
	protected StatusController statusController;

	@FXML
	protected XYCanvas<Double, Double> canvas;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		rb = resources;
		canvas.widthProperty().addListener(resizeListener());
		canvas.heightProperty().addListener(resizeListener());
	}

	private void drawGrid(Bounds bounds) {
		log.info("Bounds: {},{}/{},{}, Center: {},{}", bounds.getMinX(), bounds.getMinY(), bounds.getMaxX(),
				bounds.getMaxY(), bounds.getCenterX(), bounds.getCenterY());
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
		canvasBounds.compareAndSet(null, ((XYCanvas<?, ?>) property.getBean()).getBoundsInLocal());
		if ("height".equals(property.getName())) {
			return abs(canvasBounds.get().getHeight() - property.getValue()) > threshold;
		} else if ("width".equals(property.getName())) {
			return abs(canvasBounds.get().getWidth() - property.getValue()) > threshold;
		}
		return false;
	}

	protected ObservableList<Node> getOrAdd(XYCanvas<?, ?> parent, String name) {
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

	protected Optional<ObservableList<Node>> get(XYCanvas<?, ?> parent, String name) {
		return parent.getChildren().stream().filter(n -> name.equals(n.getId()))
				.filter(n -> n instanceof Group)
				.map(n -> (Group) n)
				.map(Group::getChildren).findFirst();
	}

}