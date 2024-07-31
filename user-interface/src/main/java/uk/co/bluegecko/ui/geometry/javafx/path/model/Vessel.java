package uk.co.bluegecko.ui.geometry.javafx.path.model;

import javafx.beans.property.StringProperty;
import javafx.geometry.Bounds;
import javafx.scene.control.Tooltip;
import javafx.scene.shape.Polygon;

public class Vessel extends Polygon {

	private final StringProperty tooltipText;

	public Vessel(Tooltip tooltip, double... points) {
		super(points);
		tooltipText = tooltip.textProperty();
		updateTooltip();
	}

	public String getPosition() {
		Bounds b = getBoundsInParent();
		String lat = b.getCenterY() > 0 ? "N" : "S";
		String lng = b.getCenterX() > 0 ? "E" : "W";
		return String.format("Lat: %1.0f%s, Lng: %1.0f%s", b.getCenterY(), lat, b.getCenterX(), lng);
	}

	public String getBearing() {
		return String.format("Bearing %1.2f°", getRotate());
	}

	public void updateTooltip() {
		tooltipText.setValue(String.format("%s\n%s", getPosition(), getBearing()));
	}

}