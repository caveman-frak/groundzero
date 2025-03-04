package uk.co.bluegecko.ui.geometry.javafx.control;

import static java.lang.Math.abs;
import static uk.co.bluegecko.ui.geometry.javafx.common.input.DragMove.registerDragMove;
import static uk.co.bluegecko.ui.geometry.javafx.common.input.ScrollZoom.registerScrollZoom;

import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.IntStream;
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
import javafx.scene.text.Text;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;

@Slf4j
public class XYCanvas extends XYChart<Number, Number> {

	private static final int ZOOM_ROUNDING = 5;
	private static final int MOVE_ROUNDING = 10;

	private final Pane contentPane;
	private final ObjectProperty<Color> circleColorProperty;
	private final IntegerProperty circleRadiusProperty;

	public XYCanvas(@NamedArg("xAxis") NumberAxis xAxis, @NamedArg("yAxis") NumberAxis yAxis) {
		super(xAxis, yAxis);
		setData(FXCollections.observableArrayList());

		circleColorProperty = StyleableProperties.createCircleColorProperty(this);
		circleRadiusProperty = StyleableProperties.createCircleRadiusProperty(this);

		contentPane = new Pane();
		contentPane.setManaged(false);
		contentPane.getStyleClass().setAll("chart-content");
		getPlotChildren().add(contentPane);
		registerDragMove(this, dragHandler(), "/images/move-icon-32.png");
		registerScrollZoom(this, zoomHandler());
		drawMarkerPoints();
		setOnMouseClicked(drawCircle());
	}

	@NotNull
	private Consumer<Point3D> dragHandler() {
		return v -> {
			double x = (double) ((int) v.getX() / MOVE_ROUNDING) * MOVE_ROUNDING;
			double y = (double) ((int) v.getY() / MOVE_ROUNDING) * MOVE_ROUNDING;
			if (x != 0) {
				NumberAxis xa = getXAxis();
				xa.setLowerBound(xa.getLowerBound() - x);
				xa.setUpperBound(xa.getUpperBound() - x);
			}
			if (y != 0) {
				NumberAxis ya = getYAxis();
				ya.setLowerBound(ya.getLowerBound() + y);
				ya.setUpperBound(ya.getUpperBound() + y);
			}
		};
	}

	@NotNull
	private Consumer<Double> zoomHandler() {
		BiFunction<Double, Double, Double> calcZoom = (value, bound) ->
				(double) ((int) (bound * (1.0 + value) / ZOOM_ROUNDING)) * ZOOM_ROUNDING;
		return d -> {
			NumberAxis xa = getXAxis();
			NumberAxis ya = getYAxis();
			double x = (1.0 - d) / (xa.getUpperBound() - xa.getLowerBound());
			double y = (1.0 - d) / (ya.getUpperBound() - ya.getLowerBound());
			xa.setLowerBound(calcZoom.apply(x, xa.getLowerBound()));
			xa.setUpperBound(calcZoom.apply(x, xa.getUpperBound()));
			ya.setLowerBound(calcZoom.apply(y, ya.getLowerBound()));
			ya.setUpperBound(calcZoom.apply(y, ya.getUpperBound()));
		};
	}

	private void drawMarkerPoints() {
		IntStream.range(1, 10).map(i -> i * 100).boxed().flatMap(i -> Stream.of(
						new double[]{-i, -i}, new double[]{-i, i}, new double[]{i, -i}, new double[]{i, i}))
				.forEach(a -> getContentChildren().add(new Circle(a[0], a[1], 5, Color.GREEN)));
	}

	protected EventHandler<MouseEvent> drawCircle() {
		Function<PickResult, Point3D> relocatePoint = result ->
				relocatePoint(result.getIntersectedPoint(), result.getIntersectedNode(), contentPane);
		return e -> {
			double x = relocatePoint.apply(e.getPickResult()).getX();
			double y = relocatePoint.apply(e.getPickResult()).getY();
			getContentChildren().addAll(
					new Circle(x, y, circleRadiusProperty.get(), circleColorProperty.get()),
					new Text(x - 30, y + 7, String.format("%03.0f, %03.0f", x, y)));
		};
	}

	public static Point3D relocatePoint(Point3D point, Node origin, Node destination) {
		if (origin == destination) {
			return point;
		} else {
			Point3D pointAtScene = origin.localToScene(point);
			return destination.sceneToLocal(pointAtScene);
		}
	}

	@Override
	protected void layoutPlotChildren() {
		NumberAxis xa = getXAxis();
		NumberAxis ya = getYAxis();
		contentPane.setClip(new Rectangle(xa.getLowerBound(), ya.getLowerBound(),
				xa.getUpperBound() - xa.getLowerBound(),
				ya.getUpperBound() - getYAxis().getLowerBound()));
		contentPane.setLayoutX(xa.getDisplayPosition(0.0));
		contentPane.setLayoutY(ya.getDisplayPosition(0.0));
		contentPane.setScaleX(abs(xa.getScale()));
		contentPane.setScaleY(abs(ya.getScale()));
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

		public static ObjectProperty<Color> createCircleColorProperty(XYCanvas node) {
			return new SimpleStyleableObjectProperty<>(CIRCLE_COLOR_CSS_META_DATA, node, "circleColor",
					CIRCLE_COLOR_CSS_META_DATA.getInitialValue(node));
		}

		public static IntegerProperty createCircleRadiusProperty(XYCanvas node) {
			return new SimpleStyleableIntegerProperty(CIRCLE_RADIUS_CSS_META_DATA, node, "circleRadius",
					CIRCLE_RADIUS_CSS_META_DATA.getInitialValue(node).intValue());
		}

	}

	public static List<CssMetaData<? extends Styleable, ?>> getClassCssMetaData() {
		return StyleableProperties.cssMetaDataList;
	}

	@Override
	public List<CssMetaData<? extends Styleable, ?>> getCssMetaData() {
		return getClassCssMetaData();
	}

}