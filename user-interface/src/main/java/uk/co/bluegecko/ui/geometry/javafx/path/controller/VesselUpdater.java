package uk.co.bluegecko.ui.geometry.javafx.path.controller;

import java.time.Duration;
import javafx.collections.ListChangeListener.Change;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import lombok.extern.slf4j.Slf4j;
import uk.co.bluegecko.ui.geometry.javafx.animation.PeriodicPulse;
import uk.co.bluegecko.ui.geometry.javafx.path.model.Vessel;

@Slf4j
class VesselUpdater extends PeriodicPulse {

	private final ObservableList<Node> children;

	public VesselUpdater(ObservableList<Node> children) {
		super(Duration.ofSeconds(1), 0);
		this.children = children;
		children.addListener(this::onChanged);
	}

	public void onChanged(Change<? extends Node> c) {
		while (c.next()) {
			if (c.wasAdded()) {
				start();
			} else if (c.wasRemoved() && c.getList().isEmpty()) {
				stop();
			}
		}
	}

	@Override
	protected void pulse() {
		children.stream().filter(n -> n instanceof Vessel).map(n -> (Vessel) n)
				.peek(Vessel::updateTooltip)
				.findFirst().ifPresent(this::updateVessel);
	}

	private void updateVessel(Vessel vessel) {
		updateStatus(vessel.getPosition());
		updateMessage(vessel.getBearing());
	}

}