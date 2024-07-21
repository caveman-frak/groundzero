package uk.co.bluegecko.ui.geometry.javafx.concurrent;

import static javafx.application.Platform.isFxApplicationThread;
import static javafx.application.Platform.runLater;

import java.time.Duration;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;
import javafx.animation.AnimationTimer;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.ReadOnlyDoubleProperty;
import javafx.beans.property.ReadOnlyStringProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import lombok.AccessLevel;
import lombok.Getter;

@Getter
public abstract class PeriodicPulse extends AnimationTimer {

	@Getter(AccessLevel.PROTECTED)
	private long lastPulse;
	private final Duration pause;
	private final DoubleProperty progress;
	private final AtomicInteger total;
	private final AtomicInteger progressUpdate;
	private final StringProperty message;
	private final AtomicReference<String> messageUpdate;

	public PeriodicPulse(Duration pause, int total) {
		this.pause = pause;
		this.lastPulse = 0;
		this.total = new AtomicInteger(total);
		progress = new SimpleDoubleProperty(this, "progress", -1);
		progressUpdate = new AtomicInteger(0);
		message = new SimpleStringProperty(this, "message", "");
		messageUpdate = new AtomicReference<>();
	}

	@Override
	public final void handle(long now) {
		if (now - lastPulse > pause.toNanos()) {
			lastPulse = now;
			run();
		}
	}

	protected abstract void run();

	public final ReadOnlyStringProperty messageProperty() {
		return message;
	}

	public final ReadOnlyDoubleProperty progressProperty() {
		return progress;
	}

	protected final void updateMessage(String message) {
		if (isFxApplicationThread()) {
			this.message.set(message);
		} else {
			if (messageUpdate.getAndSet(message) == null) {
				runLater(() -> {
					final String message1 = messageUpdate.getAndSet(null);
					PeriodicPulse.this.message.set(message1);
				});
			}
		}
	}

	protected final void updateProgress(int progress) {
		if (isFxApplicationThread()) {
			this.progress.set((double) progress / total.get());
		} else {
			if (progressUpdate.getAndSet(progress) == -1) {
				runLater(() -> {
					final int progress1 = progressUpdate.getAndSet(-1);
					PeriodicPulse.this.progress.set((double) progress1 / total.get());
				});
			}
		}
	}

}