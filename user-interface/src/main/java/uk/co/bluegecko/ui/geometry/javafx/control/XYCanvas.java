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
import javafx.geometry.Point3D;
import javafx.scene.Node;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.PickResult;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class XYCanvas extends XYChart<Number, Number> {

	private final Pane contentPane;
	private final ObjectProperty<Color> circleColorProperty;
	private final IntegerProperty circleRadiusProperty;

	public XYCanvas(@NamedArg("xAxis") NumberAxis xAxis, @NamedArg("yAxis") NumberAxis yAxis) {
		super(xAxis, yAxis);
		setData(FXCollections.observableArrayList());

		circleColorProperty = new SimpleStyleableObjectProperty<>(
				StyleableProperties.CIRCLE_COLOR_CSS_META_DATA, this, "circleColor", Color.rgb(128, 0, 0, 0.5));
		circleRadiusProperty = new SimpleStyleableIntegerProperty(
				StyleableProperties.CIRCLE_RADIUS_CSS_META_DATA, this, "circleRadius", 40);

		contentPane = new Pane();
		contentPane.getStyleClass().setAll("content-pane");
		getPlotChildren().add(contentPane);
		setOnMouseClicked(drawCircleHandler());
	}

	protected EventHandler<MouseEvent> drawCircleHandler() {
		return e -> {
			getContentChildren().add(
					new Circle(atContent(e.getPickResult()).getX(), atContent(e.getPickResult()).getY(),
							circleRadiusProperty.get(), circleColorProperty.get()));
		};
	}

	public Point3D atContent(PickResult pick) {
		Point3D intersectedPoint = pick.getIntersectedPoint();
		if (pick.getIntersectedNode() == contentPane) {
			return intersectedPoint;
		} else {
			Point3D pointAtScene = pick.getIntersectedNode().localToScene(intersectedPoint);
			return contentPane.sceneToLocal(pointAtScene);
		}
	}

	@Override
	protected void layoutPlotChildren() {
		NumberAxis xa = getXAxis();
		NumberAxis yz = getYAxis();
		contentPane.setClip(new Rectangle(xa.getLowerBound(), yz.getLowerBound(),
				xa.getUpperBound() - xa.getLowerBound(),
				yz.getUpperBound() - getYAxis().getLowerBound()));
		contentPane.setLayoutX(xa.getDisplayPosition(0.0));
		contentPane.setLayoutY(yz.getDisplayPosition(0.0));
		contentPane.setScaleX(xa.getScale());
		contentPane.setScaleY(yz.getScale());
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
		return contentPane.getChildren();
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