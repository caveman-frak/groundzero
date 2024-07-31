package uk.co.bluegecko.ui.geometry.javafx.shape.controller;

import java.time.Duration;
import java.util.List;
import java.util.ResourceBundle;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.shape.Shape;
import uk.co.bluegecko.ui.geometry.javafx.animation.PeriodicPulse;

class ShowPointsOverTime extends PeriodicPulse {

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
	protected void pulse() {
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