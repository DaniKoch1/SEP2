package client.view.managingPosts;

import java.io.IOException;
import java.util.ArrayList;

import client.controller.ClientController;
import client.view.ClientViewManager;
import client.view.GoBackMap;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.scene.text.TextFlow;
import javafx.stage.Stage;
import model.Homework;
import model.MyDate;
import model.Post;


public class AnnouncementListHandler {

    @FXML
    private VBox box;
    private ClientController controller;
    private Stage stage;

    public AnnouncementListHandler() {
        controller = ClientController.getInstance();
        stage = ClientViewManager.getStage();
        System.out.println("AnnouncementListHandler");
    }

    @FXML
    public void initialize() {
        ArrayList<Post> posts = controller.getAllPosts();
        for(Post p : posts) {
            if(p.getClass().getSimpleName().equals("Post")) {
                loadPosts(p);
            }
        }
    }

    private void loadPosts(Post post) {
        Text title = new Text(post.getTitle());
        title.setId("title");
        Text content = new Text(post.getContent());
        content.setId("content");
        Text separator = new Text("\n" + "\n");
        TextFlow textFlow = new TextFlow(title, separator, content);
        textFlow.setTextAlignment(TextAlignment.JUSTIFY);
        textFlow.setAccessibleText(post.getContent());
        textFlow.setPrefWidth(830);
        textFlow.getStyleClass().add("textPane");
        addPane(textFlow);
    }

    private void addPane(Pane pane) {
        box.getChildren().add(pane);
    }

    public void createAnnouncement() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/client/view/fxml/createAnnouncement.fxml"));
            Parent mainPane = loader.load();
            mainPane.getStylesheets().add(getClass().getResource("/client/view/fxml/login.css").toExternalForm());
            stage.getScene().setRoot(mainPane);
            stage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void goBack() {
        String path = GoBackMap.getLoader(this.getClass(), controller.getCurrentUserType());
        FXMLLoader backLoader = new FXMLLoader(getClass().getResource(path));
        try {
            Parent mainPane = backLoader.load();
            mainPane.getStylesheets().add(getClass().getResource("/client/view/fxml/login.css").toExternalForm());
            stage.getScene().setRoot(mainPane);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}