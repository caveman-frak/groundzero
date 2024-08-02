package uk.co.bluegecko.ui.geometry.javafx.control;

import javafx.beans.NamedArg;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.chart.Axis;
import javafx.scene.chart.XYChart;

public class XYCanvas<X, Y> extends XYChart<X, Y> {

	/**
	 * Constructs a XYChart given the two axes. The initial content for the chart plot background and plot area that
	 * includes vertical and horizontal grid lines and fills, are added.
	 *
	 * @param xAxis X Axis for this XY chart
	 * @param yAxis Y Axis for this XY chart
	 */
	public XYCanvas(@NamedArg("xAxis") Axis<X> xAxis, @NamedArg("yAxis") Axis<Y> yAxis) {
		super(xAxis, yAxis);
		setData(FXCollections.observableArrayList());
	}

	@Override
	protected void dataItemAdded(Series<X, Y> series, int itemIndex, Data<X, Y> item) {

	}

	@Override
	protected void dataItemRemoved(Data<X, Y> item, Series<X, Y> series) {

	}

	@Override
	protected void dataItemChanged(Data<X, Y> item) {

	}

	@Override
	protected void seriesAdded(Series<X, Y> series, int seriesIndex) {

	}

	@Override
	protected void seriesRemoved(Series<X, Y> series) {

	}

	@Override
	protected void layoutPlotChildren() {

	}

	@Override
	public ObservableList<Node> getChildren() {
		return super.getChildren();
	}

}