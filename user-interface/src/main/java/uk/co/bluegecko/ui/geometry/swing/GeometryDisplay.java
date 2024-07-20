package uk.co.bluegecko.ui.geometry.swing;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JLabel;
import org.jetbrains.annotations.NotNull;

public class GeometryDisplay {

	public static void main(String[] args) {
		JFrame frame = createFrame(
				createLabel());

		frame.setVisible(true);
	}

	private static JLabel createLabel() {
		JLabel label = new JLabel("Look");
		label.setBorder(BorderFactory.createLineBorder(Color.BLACK));
		label.setPreferredSize(new Dimension(200, 100));
		label.setHorizontalAlignment(JLabel.CENTER);
		label.setVerticalAlignment(JLabel.CENTER);
		return label;
	}

	@NotNull
	private static JFrame createFrame(Component... components) {
		JFrame frame = new JFrame("Geometry Display (Swing)");
		frame.setLayout(new FlowLayout());
		frame.setSize(600, 600);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		for (Component component : components) {
			frame.add(component);
		}

		return frame;
	}
}