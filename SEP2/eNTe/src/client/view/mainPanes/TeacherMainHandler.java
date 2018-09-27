package client.view.mainPanes;

import client.controller.ClientController;
import client.view.IconImage;
import client.view.managingPosts.*;
import client.view.ClientViewManager;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.scene.text.TextFlow;
import javafx.stage.Stage;
import model.Announcement;
import model.Discussion;
import model.Homework;
import model.Post;

import java.io.IOException;
import java.util.ArrayList;

public class TeacherMainHandler {
    @FXML
    private VBox box;
    private ClientController controller;
    private Stage stage;
    private FXMLLoader backLoader = new FXMLLoader(getClass().getResource("/client/view/fxml/mainPaneTeacher.fxml"));

    public TeacherMainHandler() {
        controller = ClientController.getInstance();
        stage = ClientViewManager.getStage();
    }

    @FXML
    public void initialize() {
        System.out.println(box);
        loadPosts();
    }

    public void reload() {
        box.getChildren().clear();
        loadPosts();
    }

    private void loadPosts() {
        ArrayList<Post> posts = controller.getAllPosts();
        for (Post p : posts) {
            switch (p.getClass().getSimpleName()) {
                case "Homework":
                    loadHomework((Homework) p);
                    break;
                case "Announcement":
                    loadAnnouncement((Announcement) p);
                    break;
                case "Discussion":
                    loadDiscussion((Discussion) p);
                default:
                    break;
            }
        }
    }

    private void loadDiscussion(Discussion discussion) {
        Text title = new Text(discussion.getTitle());
        title.setId("title");
        Text content = new Text(discussion.getContent());
        content.setId("content");
        Button showComments = new Button("COMMENTS");
        showComments.getStyleClass().add("smallButton");
        showComments.addEventHandler(MouseEvent.MOUSE_CLICKED, new TeacherMainHandler.ShowComments(discussion));
        Button delete = new Button("DELETE");
        delete.getStyleClass().add("smallButton");
        delete.addEventHandler(MouseEvent.MOUSE_CLICKED, new TeacherMainHandler.DeleteDiscussionHandler(discussion));

        Text separator = new Text("\n" + "\n");
        Text separator1 = new Text("\n" + "\n" + " ");
        Text separator2 = new Text(" ");

        TextFlow textFlow;
        if (discussion.getSpecialType().toString().toLowerCase().equals("parental")) {
            textFlow = new TextFlow(IconImage.getDisIcon(), IconImage.getParIcon(), title, separator, content, separator1, showComments,separator2,delete);
        } else {
            textFlow = new TextFlow(IconImage.getDisIcon(), title, separator, content, separator1, showComments,separator2,delete);
        }
        textFlow.setTextAlignment(TextAlignment.JUSTIFY);
        textFlow.setAccessibleText(discussion.getContent());
        textFlow.setPrefWidth(830);
        textFlow.getStyleClass().add("textPane");

        addPane(textFlow);
    }

    private void loadHomework(Homework homework) {
        Text title = new Text(homework.getTitle());
        title.setId("title");
        Text content = new Text(homework.getContent());
        content.setId("content");
        Text deadline = new Text(homework.getDeadline().toString());
        Text separator = new Text("\n" + "\n");
        Text separator1 = new Text("\n" + "\n" + " ");

        TextFlow textFlow = new TextFlow(IconImage.getHomIcon(),title, separator, content, separator1, deadline);
        textFlow.setTextAlignment(TextAlignment.JUSTIFY);
        textFlow.setAccessibleText(homework.getContent());
        textFlow.setPrefWidth(830);
        textFlow.getStyleClass().add("textPane");

        addPane(textFlow);
    }

    private void loadAnnouncement(Announcement announcement) {
        Text title = new Text(announcement.getTitle());
        title.setId("title");
        Text content = new Text(announcement.getContent());
        content.setId("content");
        Text separator = new Text("\n" + "\n");
        Text separator1 = new Text("\n" + "\n" + " ");
        Text separator2 = new Text(" ");

        Button delete = new Button("DELETE");
        delete.addEventHandler(MouseEvent.MOUSE_CLICKED, new TeacherMainHandler.DeleteAnnouncementHandler(announcement));
        delete.getStyleClass().add("smallButton");

        Button edit = new Button("EDIT");
        edit.addEventHandler(MouseEvent.MOUSE_CLICKED, new TeacherMainHandler.EditAnnouncementHandler(announcement));
        edit.getStyleClass().add("smallButton");

        TextFlow textFlow;
        switch (announcement.getSpecialType().toString().toLowerCase()) {
            case "important":
                textFlow = new TextFlow(IconImage.getImpIcon(), IconImage.getAnnIcon(), title, separator, content, separator1, delete, separator2, edit);
                break;
            case "parental":
                textFlow = new TextFlow(IconImage.getAnnIcon(), IconImage.getParIcon(), title, separator, content, separator1, delete, separator2, edit);
                break;
            default:
                textFlow = new TextFlow(IconImage.getAnnIcon(), title, separator, content, separator1, delete, separator2, edit);
                break;
        }
        textFlow.setTextAlignment(TextAlignment.JUSTIFY);
        textFlow.setAccessibleText(announcement.getContent());
        textFlow.setPrefWidth(830);
        textFlow.getStyleClass().add("textPane");
        addPane(textFlow);
    }

    private void addPane(Pane pane) {
        box.getChildren().add(pane);
    }

    public void homeworkHandler() {
        Parent mainPane;
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/client/view/fxml/homeworkHandler.fxml"));
            mainPane = loader.load();
            mainPane.getStylesheets().add(getClass().getResource("/client/view/fxml/login.css").toExternalForm());
            stage.getScene().setRoot(mainPane);
            stage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void announcementHandler() {
        Parent mainPane;
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/client/view/fxml/announcementHandler.fxml"));
            mainPane = loader.load();
            mainPane.getStylesheets().add(getClass().getResource("/client/view/fxml/login.css").toExternalForm());
            stage.getScene().setRoot(mainPane);
            stage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void discussionHandler() {
        Parent mainPane;
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/client/view/fxml/discussionHandler.fxml"));
            mainPane = loader.load();
            mainPane.getStylesheets().add(getClass().getResource("/client/view/fxml/login.css").toExternalForm());
            stage.getScene().setRoot(mainPane);
            stage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private class DeleteAnnouncementHandler implements EventHandler<Event> {

        private Announcement announcement;

        private DeleteAnnouncementHandler(Announcement announcement) {
            this.announcement = announcement;
        }

        @Override
        public void handle(Event event) {
            controller.deletePost(announcement);
            box.getChildren().clear();
            loadPosts();
        }
    }

    private class ShowComments implements EventHandler<Event> {

        private Discussion discussion;
        private ShowComments(Discussion discussion) {
            this.discussion = discussion;
        }

        @Override
        public void handle(Event event) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/client/view/fxml/discussionCommentsHandler.fxml"));
                Parent mainPane = loader.load();
                ((DiscussionCommentsHandler) loader.getController()).loadComments(discussion);
                mainPane.getStylesheets().add(getClass().getResource("/client/view/fxml/login.css").toExternalForm());
                stage.getScene().setRoot(mainPane);
                stage.show();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private class EditAnnouncementHandler implements EventHandler<Event> {

        private Announcement announcement;

        private EditAnnouncementHandler(Announcement announcement) {
            this.announcement = announcement;
        }

        @Override
        public void handle(Event event) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/client/view/fxml/createAnnouncement.fxml"));
                Parent mainPane = loader.load();
                ((CreateAnnouncementHandler) loader.getController()).setAnnouncement(announcement);
                mainPane.getStylesheets().add(getClass().getResource("/client/view/fxml/login.css").toExternalForm());
                stage.getScene().setRoot(mainPane);
                stage.show();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    private class DeleteDiscussionHandler implements EventHandler<Event> {

        private Discussion discussion;

        private DeleteDiscussionHandler(Discussion discussion) {
            this.discussion = discussion;
        }

        @Override
        public void handle(Event event) {
            controller.deletePost(discussion);
            box.getChildren().clear();
            loadPosts();
        }
    }
}
