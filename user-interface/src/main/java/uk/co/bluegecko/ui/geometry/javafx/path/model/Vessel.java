package uk.co.bluegecko.ui.geometry.javafx.path.model;

import java.time.Duration;
import javafx.beans.property.StringProperty;
import javafx.geometry.Bounds;
import javafx.geometry.Point2D;
import javafx.scene.control.Tooltip;
import javafx.scene.shape.Polygon;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class Vessel extends Polygon {

	private final VesselShape shape;
	private final StringProperty tooltipText;
	private Point2D lastPosition;
	private Double speed;

	public Vessel(Tooltip tooltip, VesselShape shape) {
		super(shape.outline());
		this.shape = shape;
		tooltipText = tooltip.textProperty();
		lastPosition = null;
		speed = Double.NaN;
		updateTooltip(Duration.ZERO);
	}

	public String getPosition() {
		Bounds b = getBoundsInParent();
		String lat = b.getCenterY() > 0 ? "N" : "S";
		String lng = b.getCenterX() > 0 ? "E" : "W";
		return String.format("Lat: %1.0f%s, Lng: %1.0f%s", b.getCenterY(), lat, b.getCenterX(), lng);
	}

	public String getSpeed(Duration duration) {
		Bounds b = getBoundsInParent();
		Point2D newPosition = new Point2D(b.getCenterX(), b.getCenterY());
		if (lastPosition != null && duration != Duration.ZERO) {
			double distance = lastPosition.distance(newPosition);
			if (distance > 0) {
				speed = distance / duration.toSeconds();
			}
		}
		lastPosition = newPosition;
		return Double.isNaN(speed) ? "Stationary" : String.format("%1.2fm/s", speed);
	}

	public String getPositionAndSpeed(Duration duration) {
		return String.format("%s, %s", getPosition(), getSpeed(duration));
	}

	public String getBearing() {
		return String.format("%1.2f°", getRotate());
	}

	public void updateTooltip(Duration duration) {
		tooltipText.setValue(String.format("Type: %s\nPosition: %s\nSpeed: %s\nBearing: %s",
				shape.type(), getPosition(), getSpeed(duration), getBearing()));
	}

}