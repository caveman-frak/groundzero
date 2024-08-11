package uk.co.bluegecko.ui.geometry.javafx.common.input;

import java.io.IOException;
import java.io.Serializable;
import java.net.URL;
import java.util.Optional;
import java.util.function.Consumer;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.geometry.Point3D;
import javafx.scene.Node;
import javafx.scene.image.Image;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.DataFormat;
import javafx.scene.input.DragEvent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.TransferMode;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;

@Slf4j
public record DragMove(Consumer<Point3D> consumer, String cursorLocation) {

	public static void registerDragMove(Node node, Consumer<Point3D> consumer, String cursorLocation) {
		DragMove dragMove = new DragMove(consumer, cursorLocation);
		node.setOnDragDetected(dragMove.dragDetected());
		node.setOnDragDropped(dragMove.dragDropped());
		node.setOnDragDone(dragMove.dragDone());
		node.setOnDragOver(dragMove.dragOver());
		node.setOnDragEntered(dragMove.dragEntered());
		node.setOnDragExited(dragMove.dragExited());
	}

	@NotNull
	private EventHandler<MouseEvent> dragDetected() {
		return e -> {
			Dragboard db = ((Node) e.getSource()).startDragAndDrop(TransferMode.MOVE);
			ClipboardContent content = new ClipboardContent();
			content.put(DRAG_MOVE_INFO, Origin.from(e.getPickResult().getIntersectedPoint()));
			db.setContent(content);
			moveCursor(cursorLocation)
					.ifPresent(i -> db.setDragView(i, 0, 0));
			e.consume();
		};
	}

	@NotNull
	private EventHandler<DragEvent> dragDropped() {
		return e -> {
			if (e.getDragboard().getContent(DRAG_MOVE_INFO) instanceof Origin origin) {
				e.setDropCompleted(true);
				consumer.accept(origin.vector(e.getPickResult().getIntersectedPoint()));
			}
			e.consume();
		};
	}

	@NotNull
	private EventHandler<DragEvent> dragDone() {
		return Event::consume;
	}

	@NotNull
	private EventHandler<DragEvent> dragOver() {
		return e -> {
			e.acceptTransferModes(TransferMode.MOVE);
			e.consume();
		};
	}

	@NotNull
	private EventHandler<DragEvent> dragEntered() {
		return e -> {
			e.acceptTransferModes(TransferMode.MOVE);
			e.consume();
		};
	}

	private Optional<Image> moveCursor(String location) {
		try {
			URL resource = getClass().getResource(location);
			if (resource != null) {
				return Optional.of(new Image(resource.openStream()));
			} else {
				log.warn("Skipping icon '{}' as resource missing", location);
			}
		} catch (IOException e) {
			log.warn("Unable to load icon '{}' due to {}", location, e.getMessage(), e);
		}
		return Optional.empty();
	}

	@NotNull
	private EventHandler<DragEvent> dragExited() {
		return Event::consume;
	}

	private static final DataFormat DRAG_MOVE_INFO = new DataFormat("dragMove.origin");

	private record Origin(double x, double y, double z) implements Serializable {

		public static Origin from(Point3D point) {
			return new Origin(point.getX(), point.getY(), point.getZ());
		}

		public Point3D vector(Point3D destination) {
			return destination.subtract(x, y, z);
		}

	}

}