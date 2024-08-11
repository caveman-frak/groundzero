package uk.co.bluegecko.ui.geometry.javafx.common.input;

import java.util.function.Consumer;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.input.ScrollEvent;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;

@Slf4j
public record ScrollZoom(Consumer<Double> consumer) {

	public static ScrollZoom scrollZoom(Node node, Consumer<Double> consumer) {
		ScrollZoom scrollZoom = new ScrollZoom(consumer);
		node.setOnScroll(scrollZoom.scroll());
		return scrollZoom;
	}

	@NotNull
	private EventHandler<ScrollEvent> scroll() {
		return e -> {
			consumer.accept(e.getDeltaY());
			e.consume();
		};
	}

}