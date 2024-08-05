package uk.co.bluegecko.ui.geometry.javafx.common.controller;


import java.net.URL;
import java.util.Collection;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.function.Supplier;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Group;
import javafx.scene.Node;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import uk.co.bluegecko.ui.geometry.javafx.control.XYCanvas;

@Slf4j
public abstract class BaseGraphicsController implements Initializable {

	protected ResourceBundle rb;

	@Setter
	protected StatusController statusController;

	@FXML
	protected XYCanvas canvas;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		rb = resources;
	}

	public void clearGraphics() {
		statusController.clearProgress();
	}

	protected ObservableList<Node> getOrAdd(XYCanvas parent, String groupId) {
		ObservableList<Node> children = parent.getContentChildren();
		Optional<Node> group = children.filtered(n -> groupId.equals(n.getId()))
				.filtered(n -> n instanceof Group)
				.stream().findFirst();
		if (group.isPresent()) {
			return ((Group) group.get()).getChildren();
		} else {
			Group node = new Group();
			node.setId(groupId);
			children.add(node);
			return node.getChildren();
		}
	}

	protected Optional<ObservableList<Node>> get(XYCanvas parent, String groupId) {
		return parent.getContentChildren().stream().filter(n -> groupId.equals(n.getId()))
				.filter(n -> n instanceof Group)
				.map(n -> (Group) n)
				.map(Group::getChildren).findFirst();
	}

}