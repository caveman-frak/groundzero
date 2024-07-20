package uk.co.bluegecko.ui.geometry.javafx;

import static uk.co.bluegecko.ui.geometry.calc.GeometryCalculator.calculatePointsAlongCubicBezierCurve;
import static uk.co.bluegecko.ui.geometry.calc.GeometryCalculator.calculatePointsAlongEllipticArc;
import static uk.co.bluegecko.ui.geometry.calc.GeometryCalculator.calculatePointsAlongLine;
import static uk.co.bluegecko.ui.geometry.calc.GeometryCalculator.calculatePointsAlongQuadraticBezierCurve;

import java.awt.Point;
import java.net.URL;
import java.time.Duration;
import java.util.ResourceBundle;
import java.util.stream.Stream;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.shape.Arc;
import javafx.scene.shape.CubicCurve;
import javafx.scene.shape.Ellipse;
import javafx.scene.shape.Line;
import javafx.scene.shape.QuadCurve;
import javafx.scene.shape.Rectangle;
import lombok.Setter;

public class FXMLControlsController implements Initializable {

	@FXML
	private TextField startPointX;
	@FXML
	private TextField startPointY;
	@FXML
	private TextField endPointX;
	@FXML
	private TextField endPointY;
	@FXML
	private TextField control1X;
	@FXML
	private TextField control1Y;
	@FXML
	private TextField control2X;
	@FXML
	private TextField control2Y;
	@FXML
	public TextField delay;
	@FXML
	private TextField points;
	@FXML
	private ToggleGroup shape;

	@Setter
	private FXMLGraphicsController graphicsController;

	@FXML
	protected void drawShape(ActionEvent e) {
		graphicsController.drawMarkers(Stream.of(start(), end(), control1(), control2()));
		graphicsController.drawShape(switch (shape()) {
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
		graphicsController.drawPoints(
				switch (shape()) {
					case CUBIC ->
							calculatePointsAlongCubicBezierCurve(start(), control1(), control2(), end(), points());
					case QUADRATIC -> calculatePointsAlongQuadraticBezierCurve(start(), control1(), end(), points());
					case ARC -> calculatePointsAlongEllipticArc(start(), control1(), end().x, end().y, points());
					case ELLIPSE -> calculatePointsAlongEllipticArc(start(), control1(), 0, 360, points());
					case LINE -> calculatePointsAlongLine(start(), end(), points());
					default -> Stream.of();
				}
				, Duration.ZERO);
	}

	@FXML
	protected void clearGraphics(ActionEvent e) {
		graphicsController.clearGraphics();
	}

	@FXML
	protected void resetControls(ActionEvent e) {
		controlDefaults();
	}

	@Override
	public void initialize(URL url, ResourceBundle rb) {
		controlDefaults();
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
		delay.setText("10s");
		points.setText("100");
	}

	public enum Shape {
		CUBIC, QUADRATIC, ARC, ELLIPSE, LINE, RECT
	}

	private Shape shape() {
		return switch (((RadioButton) shape.getSelectedToggle()).getId()) {
			case "cubic" -> Shape.CUBIC;
			case "quad" -> Shape.QUADRATIC;
			case "arc" -> Shape.ARC;
			case "ellipse" -> Shape.ELLIPSE;
			case "rect" -> Shape.RECT;
			default -> Shape.LINE;
		};
	}

	private Point start() {
		return new Point(Integer.parseInt(startPointX.getText()), Integer.parseInt(startPointY.getText()));
	}

	private Point end() {
		return new Point(Integer.parseInt(endPointX.getText()), Integer.parseInt(endPointY.getText()));
	}

	private Point control1() {
		return new Point(Integer.parseInt(control1X.getText()), Integer.parseInt(control1Y.getText()));
	}

	private Point control2() {
		return new Point(Integer.parseInt(control2X.getText()), Integer.parseInt(control2Y.getText()));
	}

	private Duration delay() {
		return Duration.ZERO;
	}

	private int points() {
		return Integer.parseInt(points.getText());
	}

}