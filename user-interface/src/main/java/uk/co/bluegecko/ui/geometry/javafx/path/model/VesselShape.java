package uk.co.bluegecko.ui.geometry.javafx.path.model;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.Accessors;
import lombok.experimental.FieldDefaults;

@RequiredArgsConstructor
@Getter
@Accessors(fluent = true)
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public enum VesselShape {
	SLOW(new double[]{
			190, 195,
			190, 205,
			205, 205,
			210, 200,
			205, 195}, false),
	SLOW_TAG(new double[]{
			190, 195,
			195, 200,
			190, 205,
			205, 205,
			210, 200,
			205, 195}, true),
	FAST(new double[]{
			190, 195,
			190, 205,
			210, 200}, false),
	FAST_TAG(new double[]{
			190, 195,
			195, 200,
			190, 205,
			210, 200}, true),
	TANKER(new double[]{
			190, 195,
			190, 205,
			210, 205,
			210, 195}, false),
	TANKER_TAG(new double[]{
			190, 195,
			195, 200,
			190, 205,
			210, 205,
			210, 195}, true);

	double[] outline;
	boolean tagged;
}