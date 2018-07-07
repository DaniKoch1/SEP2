package client.view.Administrator;

import client.controller.ClientController;
import client.view.ClientViewManager;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.scene.text.TextFlow;
import javafx.stage.Stage;
import model.Discussion;
import model.Post;
import model.DiscussionComment;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class DiscussionCommentsHandler {

	@FXML
	private VBox box;
	@FXML
	private Label title;
	private ClientController controller;
	private Stage stage;
	private Parent mainPane;

	public DiscussionCommentsHandler() {
		controller = ClientController.getInstance();
		stage = ClientViewManager.getStage();
		System.out.println("DiscussionHandler");
		FXMLLoader loader = new FXMLLoader(getClass().getResource("/client/view/fxml/mainPaneAdmin.fxml"));
		try {
			mainPane = loader.load();
			mainPane.getStylesheets().add(getClass().getResource("/client/view/fxml/login.css").toExternalForm());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@FXML
	public void initialize() {
		System.out.println("second");
		System.out.println(box);
		//loadComments();
	}

	public void loadComments(Discussion discussion) {
		title.setText(discussion.getTitle());
		discussion.getComments().forEach(this::loadUserComments);
	}

	private void loadUserComments(DiscussionComment comment) {
		Text content = new Text(comment.getContent());
		content.setId("content");
		String name = comment.getUser();
		Text userName = new Text(name);
		Text separator = new Text("\n" + "\n");
		TextFlow textFlow = new TextFlow(content, separator, userName);
		textFlow.setTextAlignment(TextAlignment.JUSTIFY);
		textFlow.setAccessibleText(comment.getContent());
		textFlow.setPrefWidth(830);
		textFlow.getStyleClass().add("textPane");

		addPane(textFlow);
	}

	private void addPane(Pane pane) {
		box.getChildren().add(pane);

	}

	public void addComment() {
		TextArea content = new TextArea();
		content.setText("Write a content");
		content.getStyleClass().add("content");
		Button save = new Button("save");
		save.getStyleClass().add("smallButton");
		save.setOnAction(e -> createComment());

		VBox text = new VBox();
		text.getChildren().addAll(content, save);
		box.getChildren().add(0, text);
		System.out.println(content.getText());
	}

	private void createComment() {
		VBox text = (VBox) box.getChildren().get(0);
		TextArea content = (TextArea) text.getChildren().get(0);
		controller.addDiscussionComment(content.getText());
		reload();
	}

	private void reload() {
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource("/client/view/fxml/discussionCommentsHandler.fxml"));
			mainPane = loader.load();
			mainPane.getStylesheets().add(getClass().getResource("/client/view/fxml/login.css").toExternalForm());
			stage.getScene().setRoot(mainPane);
			stage.show();

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void goBack() {
		stage.getScene().setRoot(mainPane);
		stage.show();
	}
//
//	public void setDiscussion(Discussion discussion) {
//		this.discussion = discussion;
//		title.setText(discussion.getTitle());
//	}
}