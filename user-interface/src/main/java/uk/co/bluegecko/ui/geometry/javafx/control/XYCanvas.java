package uk.co.bluegecko.ui.geometry.javafx.control;

import java.util.List;
import java.util.stream.Stream;
import javafx.beans.NamedArg;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.css.CssMetaData;
import javafx.css.SimpleStyleableIntegerProperty;
import javafx.css.SimpleStyleableObjectProperty;
import javafx.css.StyleConverter;
import javafx.css.Styleable;
import javafx.css.StyleableProperty;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;

@Slf4j
public class XYCanvas extends XYChart<Number, Number> {

	private static final String ANCHOR = "-fx-anchor-";
	private static final int ANCHOR_RADIUS = 100;

	private final Pane contentPane;
	private final Group contentGroup;
	private final ObjectProperty<Color> anchorColorProperty;
	private final ObjectProperty<Color> circleColorProperty;
	private final IntegerProperty circleRadiusProperty;

	public XYCanvas(@NamedArg("xAxis") NumberAxis xAxis, @NamedArg("yAxis") NumberAxis yAxis) {
		super(xAxis, yAxis);
		setData(FXCollections.observableArrayList());

		anchorColorProperty = new SimpleStyleableObjectProperty<>(
				StyleableProperties.ANCHOR_COLOR_CSS_META_DATA, this, "color", Color.TRANSPARENT);
		circleColorProperty = new SimpleStyleableObjectProperty<>(
				StyleableProperties.CIRCLE_COLOR_CSS_META_DATA, this, "circleColor", Color.rgb(128, 0, 0, 0.5));
		circleRadiusProperty = new SimpleStyleableIntegerProperty(
				StyleableProperties.CIRCLE_RADIUS_CSS_META_DATA, this, "circleRadius", 40);

		contentPane = new Pane();
		contentPane.setBackground(getBackground());
		contentPane.getStyleClass().setAll("content-pane");
		contentPane.setOnMouseClicked(drawCircleHandler());
		getPlotChildren().add(contentPane);
		contentGroup = new Group();
		contentPane.getChildren().add(contentGroup);
	}

	protected EventHandler<MouseEvent> drawCircleHandler() {
		return e -> getContentChildren().add(new Circle(
				getXAxis().getValueForDisplay(e.getX()).doubleValue(),
				getYAxis().getValueForDisplay(e.getY()).doubleValue(),
				circleRadiusProperty.get(), circleColorProperty.get()));
	}

	private void anchorContent(Color color) {
		if (getContentChildren().stream()
				.filter(n -> n.getId() != null)
				.noneMatch(n -> n.getId().startsWith(ANCHOR))) {
			getContentChildren().addAll(
					createAnchor(getXAxis().getLowerBound(), getYAxis().getLowerBound(), "bottom-left", color),
					createAnchor(getXAxis().getLowerBound(), getYAxis().getUpperBound(), "top-left", color),
					createAnchor(getXAxis().getUpperBound(), getYAxis().getLowerBound(), "bottom-right", color),
					createAnchor(getXAxis().getUpperBound(), getYAxis().getUpperBound(), "top-right", color));
		}
	}

	@NotNull
	private static Circle createAnchor(double centerX, double centerY, String id, Color color) {
		Circle circle = new Circle(centerX, centerY, ANCHOR_RADIUS, color);
		circle.setId(ANCHOR + id);
		return circle;
	}

	@Override
	protected void layoutPlotChildren() {
		super.layoutChildren();
		anchorContent(getAnchorColor());
		contentPane.resizeRelocate(0, 0, getXAxis().getWidth(), getYAxis().getHeight());
		contentGroup.setLayoutX(getXAxis().getDisplayPosition(0.0));
		contentGroup.setLayoutY(getYAxis().getDisplayPosition(0.0));
		contentGroup.setScaleX(getXAxis().getScale());
		contentGroup.setScaleY(getYAxis().getScale());
	}

	public final ObjectProperty<Color> anchorColorProperty() {
		return anchorColorProperty;
	}

	public final Color getAnchorColor() {
		return anchorColorProperty.getValue();
	}

	public final void setAnchorColor(Color color) {
		anchorColorProperty.setValue(color);
	}

	public final ObjectProperty<Color> circleColorProperty() {
		return circleColorProperty;
	}

	public final Color getCircleColor() {
		return circleColorProperty.getValue();
	}

	public final void setCircleColor(Color color) {
		circleColorProperty.setValue(color);
	}

	public final IntegerProperty circleRadiusProperty() {
		return circleRadiusProperty;
	}

	public final int getCircleRadius() {
		return circleRadiusProperty.getValue();
	}

	public final void setCircleRadius(int radius) {
		circleRadiusProperty.setValue(radius);
	}

	public ObservableList<Node> getContentChildren() {
		return contentGroup.getChildren();
	}

	@Override
	public NumberAxis getXAxis() {
		return (NumberAxis) super.getXAxis();
	}

	@Override
	public NumberAxis getYAxis() {
		return (NumberAxis) super.getYAxis();
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

	private static class StyleableProperties {

		private static final CssMetaData<XYCanvas, Color> ANCHOR_COLOR_CSS_META_DATA =
				new CssMetaData<>("-fx-anchor-color", StyleConverter.getColorConverter(),
						Color.TRANSPARENT) {

					@Override
					public boolean isSettable(XYCanvas canvas) {
						return !canvas.anchorColorProperty.isBound();
					}

					@Override
					@SuppressWarnings("unchecked")
					public StyleableProperty<Color> getStyleableProperty(XYCanvas canvas) {
						return (StyleableProperty<Color>) canvas.anchorColorProperty;
					}
				};

		private static final CssMetaData<XYCanvas, Color> CIRCLE_COLOR_CSS_META_DATA =
				new CssMetaData<>("-fx-circle-color", StyleConverter.getColorConverter(),
						Color.rgb(128, 0, 0, 0.5)) {

					@Override
					public boolean isSettable(XYCanvas canvas) {
						return !canvas.circleColorProperty.isBound();
					}

					@Override
					@SuppressWarnings("unchecked")
					public StyleableProperty<Color> getStyleableProperty(XYCanvas canvas) {
						return (StyleableProperty<Color>) canvas.circleColorProperty;
					}
				};

		private static final CssMetaData<XYCanvas, Number> CIRCLE_RADIUS_CSS_META_DATA =
				new CssMetaData<>("-fx-circle-radius", StyleConverter.getSizeConverter(), 40) {

					@Override
					public boolean isSettable(XYCanvas canvas) {
						return !canvas.circleRadiusProperty.isBound();
					}

					@Override
					@SuppressWarnings("unchecked")
					public StyleableProperty<Number> getStyleableProperty(XYCanvas canvas) {
						return (StyleableProperty<Number>) canvas.circleRadiusProperty;
					}
				};

		private static final List<CssMetaData<? extends Styleable, ?>> cssMetaDataList =
				Stream.concat(XYChart.getClassCssMetaData().stream(), Stream.of(
						ANCHOR_COLOR_CSS_META_DATA,
						CIRCLE_COLOR_CSS_META_DATA,
						CIRCLE_RADIUS_CSS_META_DATA)).toList();

	}

	public static List<CssMetaData<? extends Styleable, ?>> getClassCssMetaData() {
		return StyleableProperties.cssMetaDataList;
	}

	@Override
	public List<CssMetaData<? extends Styleable, ?>> getCssMetaData() {
		return getClassCssMetaData();
	}

}