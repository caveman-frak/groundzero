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
			205, 195}, "Slow Vessel", false),
	SLOW_TAG(new double[]{
			190, 195,
			195, 200,
			190, 205,
			205, 205,
			210, 200,
			205, 195}, "Slow Vessel", true),
	FAST(new double[]{
			190, 195,
			190, 205,
			210, 200}, "Fast Vessel", false),
	FAST_TAG(new double[]{
			190, 195,
			195, 200,
			190, 205,
			210, 200}, "Fast Vessel", true),
	TANKER(new double[]{
			190, 195,
			190, 205,
			210, 205,
			210, 195}, "Tanker", false),
	TANKER_TAG(new double[]{
			190, 195,
			195, 200,
			190, 205,
			210, 205,
			210, 195}, "Tanker", true);

	double[] outline;
	String type;
	boolean tagged;
}