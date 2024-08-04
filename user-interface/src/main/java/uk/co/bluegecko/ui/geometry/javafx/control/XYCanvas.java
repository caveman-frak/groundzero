package uk.co.bluegecko.ui.geometry.javafx.control;

import javafx.beans.NamedArg;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;

@Slf4j
public class XYCanvas extends XYChart<Number, Number> {

	private final Pane contentPane;
	private final Group contentGroup;

	/**
	 * Constructs a XYChart given the two axes. The initial content for the chart plot background and plot area that
	 * includes vertical and horizontal grid lines and fills, are added.
	 *
	 * @param xAxis X Axis for this XY chart
	 * @param yAxis Y Axis for this XY chart
	 */
	public XYCanvas(@NamedArg("xAxis") NumberAxis xAxis, @NamedArg("yAxis") NumberAxis yAxis) {
		super(xAxis, yAxis);
		setData(FXCollections.observableArrayList());

		contentPane = new StackPane();
		contentPane.setBackground(new Background(new BackgroundFill(Color.rgb(0, 0, 64, 0.25), null, null)));
		getPlotChildren().add(contentPane);
		contentGroup = new Group();
		contentPane.getChildren().add(contentGroup);
		contentPane.setOnMouseClicked(drawCircleHandler());
		anchorContent(Color.TRANSPARENT);
	}

	private void anchorContent(Color color) {
		getContentChildren().addAll(
				new Circle(-400, -400, 10, color),
				new Circle(-400, 400, 10, color),
				new Circle(400, -400, 10, color),
				new Circle(400, 400, 10, color));
	}

	@NotNull
	private EventHandler<MouseEvent> drawCircleHandler() {
		return e -> getContentChildren().add(new Circle(
				getXAxis().getValueForDisplay(e.getX()).doubleValue(),
				getYAxis().getValueForDisplay(e.getY()).doubleValue(),
				40, Color.rgb(128, 0, 0, 0.5)));
	}

	@Override
	protected void layoutPlotChildren() {
		super.layoutChildren();
		contentPane.resizeRelocate(0, 0, getXAxis().getWidth(), getYAxis().getHeight());
		contentGroup.setLayoutX(getXAxis().getDisplayPosition(0.0));
		contentGroup.setLayoutY(getYAxis().getDisplayPosition(0.0));
		contentGroup.setScaleX(((NumberAxis) getXAxis()).getScale());
		contentGroup.setScaleY(((NumberAxis) getYAxis()).getScale());
	}

	public ObservableList<Node> getContentChildren() {
		return contentGroup.getChildren();
	}

	@Override
	protected void dataItemAdded(Series<Number, Number> series, int itemIndex, Data<Number, Number> item) {

	}

	@Override
	protected void dataItemRemoved(Data<Number, Number> item, Series<Number, Number> series) {

	}

	@Override
	protected void dataItemChanged(Data<Number, Number> item) {

	}

	@Override
	protected void seriesAdded(Series<Number, Number> series, int seriesIndex) {

	}

	@Override
	protected void seriesRemoved(Series<Number, Number> series) {

	}

}