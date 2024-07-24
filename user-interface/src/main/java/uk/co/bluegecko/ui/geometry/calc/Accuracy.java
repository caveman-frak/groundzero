package uk.co.bluegecko.ui.geometry.calc;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.Accessors;

@RequiredArgsConstructor
@Getter
@Accessors(fluent = true)
public enum Accuracy {
	MIN(2),
	POOR(3),
	LOW(4),
	MEDIUM(5),
	HIGH(6),
	MAX(7);

	private final int iterations;
}