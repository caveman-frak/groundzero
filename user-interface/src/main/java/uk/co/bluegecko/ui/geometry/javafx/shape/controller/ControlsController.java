package uk.co.bluegecko.ui.geometry.javafx.shape.controller;

import java.awt.Point;
import java.net.URL;
import java.time.Duration;
import java.util.ResourceBundle;
import java.util.stream.Stream;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.CheckBox;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.shape.Arc;
import javafx.scene.shape.CubicCurve;
import javafx.scene.shape.Ellipse;
import javafx.scene.shape.Line;
import javafx.scene.shape.QuadCurve;
import javafx.scene.shape.Rectangle;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.rgielen.fxweaver.core.FxmlView;
import org.springframework.stereotype.Component;
import uk.co.bluegecko.ui.geometry.calc.Accuracy;
import uk.co.bluegecko.ui.geometry.calc.GeometryCalculator;
import uk.co.bluegecko.ui.geometry.javafx.control.NumericField;

@Slf4j
@Component
@FxmlView("/views/controls.fxml")
@RequiredArgsConstructor
public class ControlsController implements Initializable {

	private final GeometryCalculator calculator;

	@FXML
	private NumericField startPointX;
	@FXML
	private NumericField startPointY;
	@FXML
	private NumericField endPointX;
	@FXML
	private NumericField endPointY;
	@FXML
	private NumericField control1X;
	@FXML
	private NumericField control1Y;
	@FXML
	private NumericField control2X;
	@FXML
	private NumericField control2Y;
	@FXML
	private CheckBox showLines;
	@FXML
	public NumericField duration;
	@FXML
	private NumericField points;
	@FXML
	private ToggleGroup shapeSelect;

	private final GraphicsController graphicsController;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		log.info("Initialising. URL = {}, Bundle = {}", location, resources);
		controlDefaults();
	}

	@FXML
	protected void drawShape(ActionEvent e) {
		log.info("Drawing markers");
		graphicsController.drawMarkers(Stream.of(start(), end(), control1(), control2()));
		Shape shape = shape();
		log.info("Drawing shape {}", shape);
		graphicsController.drawShape(switch (shape) {
			case QUADRATIC -> new QuadCurve(start().x, start().y, control1().x, control1().y, end().x, end().y);
			case CUBIC -> new CubicCurve(start().x, start().y, control1().x, control1().y, control2().x, control2().y,
					end().x, end().y);
			case ARC -> new Arc(start().x, start().y, control1().x, control1().y, end().x, end().y);
			case ELLIPSE -> new Ellipse(start().x, start().y, control1().x, control1().y);
			case LINE -> new Line(start().x, start().y, end().x, end().y);
			case RECT -> new Rectangle(start().x, start().y, end().x, end().y);
		});
	}

	@FXML
	protected void drawPoints(ActionEvent e) {
		showLength();
		if (showLines()) {
			drawLines();
		} else {
			drawPoints();
		}
	}

	@FXML
	protected void clearGraphics(ActionEvent e) {
		log.info("Clearing canvas");
		graphicsController.clearGraphics();
	}

	@FXML
	protected void resetControls(ActionEvent e) {
		log.info("Resetting control state");
		controlDefaults();
	}

	private void drawLines() {
		graphicsController.drawLines(
				switch (shape()) {
					case CUBIC -> calculator.calculateTangentsAlongCubicBezierCurve(
							start(), control1(), control2(), end(), points());
					case QUADRATIC -> calculator.calculateTangentsAlongQuadraticBezierCurve(
							start(), control1(), end(), points());
					case ARC -> calculator.calculateTangentsAlongEllipticArc(
							start(), control1(), end().x, end().y, points());
					case ELLIPSE -> calculator.calculateTangentsAlongEllipticArc(start(), control1(), 0, 360, points());
					default -> Stream.of();
				}, duration());
	}

	private void drawPoints() {
		graphicsController.drawPoints(
				switch (shape()) {
					case CUBIC -> calculator.calculatePointsAlongCubicBezierCurve(
							start(), control1(), control2(), end(), points());
					case QUADRATIC -> calculator.calculatePointsAlongQuadraticBezierCurve(
							start(), control1(), end(), points());
					case ARC -> calculator.calculatePointsAlongEllipticArc(
							start(), control1(), end().x, end().y, points());
					case ELLIPSE -> calculator.calculatePointsAlongEllipticArc(start(), control1(), 0, 360, points());
					case LINE -> calculator.calculatePointsAlongLine(start(), end(), points());
					default -> Stream.of();
				}, duration());
	}

	private void showLength() {
		final Accuracy accuracy = Accuracy.MAX;
		graphicsController.showLength(shape(),
				switch (shape()) {
					case CUBIC -> calculator.calculateLengthOfCubicBezierCurve(
							start(), control1(), control2(), end(), accuracy);
					case QUADRATIC -> calculator.calculateLengthOfQuadraticBezierCurve(
							start(), control1(), end(), accuracy);
					case ARC -> calculator.calculateLengthOfEllipticArc(
							control1(), end().x, end().y, accuracy);
					case ELLIPSE -> calculator.calculateLengthOfEllipticArc(control1(), 0, 360, accuracy);
					case LINE -> calculator.calculateLengthOfLine(start(), end());
					default -> 0.0;
				});
	}

	private void controlDefaults() {
		startPointX.setText("200");
		startPointY.setText("200");
		endPointX.setText("400");
		endPointY.setText("200");
		control1X.setText("200");
		control1Y.setText("250");
		control2X.setText("400");
		control2Y.setText("150");
		duration.setText("10");
		points.setText("100");
	}

	public enum Shape {
		CUBIC, QUADRATIC, ARC, ELLIPSE, LINE, RECT
	}

	private Shape shape() {
		return switch (((RadioButton) shapeSelect.getSelectedToggle()).getId()) {
			case "cubic" -> Shape.CUBIC;
			case "quad" -> Shape.QUADRATIC;
			case "arc" -> Shape.ARC;
			case "ellipse" -> Shape.ELLIPSE;
			case "rect" -> Shape.RECT;
			default -> Shape.LINE;
		};
	}

	private Point start() {
		return new Point(startPointX.value(), startPointY.value());
	}

	private Point end() {
		return new Point(endPointX.value(), endPointY.value());
	}

	private Point control1() {
		return new Point(control1X.value(), control1Y.value());
	}

	private Point control2() {
		return new Point(control2X.value(), control2Y.value());
	}

	private Duration duration() {
		return duration.value() > 0 ? Duration.ofSeconds(duration.value()) : Duration.ZERO;
	}

	private int points() {
		return points.value();
	}

	private boolean showLines() {
		return showLines.isSelected();
	}

}