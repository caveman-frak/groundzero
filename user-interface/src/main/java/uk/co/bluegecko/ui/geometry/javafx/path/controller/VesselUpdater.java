package uk.co.bluegecko.ui.geometry.javafx.path.controller;

import java.time.Duration;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import uk.co.bluegecko.ui.geometry.javafx.animation.PeriodicPulse;
import uk.co.bluegecko.ui.geometry.javafx.path.model.Vessel;

class VesselUpdater extends PeriodicPulse {

	private final ObservableList<Node> children;

	public VesselUpdater(ObservableList<Node> children) {
		super(Duration.ofSeconds(1), 0);
		this.children = children;
	}

	@Override
	protected void pulse() {
		children.stream().filter(c -> c instanceof Vessel).map(c -> (Vessel) c).findFirst()
				.ifPresent(v -> {
					v.updateTooltip();
					updateStatus(v.getPosition());
					updateMessage(v.getBearing());
				});
	}

}