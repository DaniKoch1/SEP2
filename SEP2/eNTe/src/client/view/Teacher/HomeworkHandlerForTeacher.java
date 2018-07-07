package client.view.Teacher;

import client.controller.ClientController;
import client.view.ClientViewManager;
import client.view.Administrator.HomeworkRepliesListHandler;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.scene.text.TextFlow;
import javafx.stage.Stage;
import model.Homework;
import model.Post;

import java.io.IOException;
import java.util.ArrayList;

public class HomeworkHandlerForTeacher {
    @FXML
    private VBox box;
    @FXML
    private ImageView ente;
    private ClientController controller;
    private Stage stage;
    private Parent mainPane;

    public HomeworkHandlerForTeacher() {
        controller = ClientController.getInstance();
        stage = ClientViewManager.getStage();
        System.out.println("HomeworkHandler");
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/client/view/fxml/mainPaneTeacher.fxml"));
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
        loadPosts();
    }

    private void loadPosts() {
        ArrayList<Post> posts = controller.getAllPosts();
        for (Post p : posts) {
            if (p.getClass().getSimpleName().equals("Homework")) {
                loadHomework((Homework) p);
            }
        }
    }

    private void loadHomework(Homework homework) {
        Text title = new Text(homework.getTitle());
        title.setId("title");
        Text content = new Text(homework.getContent());
        content.setId("content");
        Text deadline = new Text(homework.getDeadline().toString());
        Text separator = new Text("\n" + "\n");
        Text separator1 = new Text("\n" + "\n" + " ");
        Text separator2 = new Text("\n" + "\n" + " ");
        Text separator3 = new Text(" ");
        Text separator4 = new Text(" ");

        Button list = new Button("DONE BY:");
        list.addEventHandler(MouseEvent.MOUSE_CLICKED, new HomeworkHandlerForTeacher.ListOfHomeworkHandlerTeacher(homework));
        list.getStyleClass().add("smallButton");
        Button edit = new Button("EDIT");
        edit.addEventHandler(MouseEvent.MOUSE_CLICKED, new HomeworkHandlerForTeacher.EditHomeworkHandlerTeacher(homework));
        edit.getStyleClass().add("smallButton");
        Button delete = new Button("DELETE");
        delete.addEventHandler(MouseEvent.MOUSE_CLICKED, new HomeworkHandlerForTeacher.DeleteHomeworkHandlerTeacher(homework));
        delete.getStyleClass().add("smallButton");

        TextFlow textFlow = new TextFlow(title, separator, content, separator1, deadline, separator2, list, separator3, edit, separator4, delete);
        textFlow.setTextAlignment(TextAlignment.JUSTIFY);
        textFlow.setAccessibleText(homework.getContent());
        textFlow.setPrefWidth(830);
        textFlow.getStyleClass().add("textPane");

        addPane(textFlow);
    }

    private void addPane(Pane pane) {
        box.getChildren().add(pane);
    }

    public void createHomework() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/client/view/fxml/createHomeworkTeacher.fxml"));
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

    private class EditHomeworkHandlerTeacher implements EventHandler<Event> {

        private Homework homework;

        private EditHomeworkHandlerTeacher(Homework homework) {
            this.homework = homework;
        }

        @Override
        public void handle(Event event) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/client/view/fxml/createHomeworkTeacher.fxml"));
                mainPane = loader.load();
                ((CreateHomeworkHandlerForTeacher) loader.getController()).setHomework(homework);
                mainPane.getStylesheets().add(getClass().getResource("/client/view/fxml/login.css").toExternalForm());
                stage.getScene().setRoot(mainPane);
                stage.show();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }


    }

    private class DeleteHomeworkHandlerTeacher implements EventHandler<Event> {

        private Homework homework;

        private DeleteHomeworkHandlerTeacher(Homework homework) {
            this.homework = homework;
        }

        @Override
        public void handle(Event event) {
            controller.deletePost(homework);
            box.getChildren().clear();
            loadPosts();
        }
    }

    private class ListOfHomeworkHandlerTeacher implements EventHandler<Event> {

        private Homework homework;

        private ListOfHomeworkHandlerTeacher(Homework homework) {
            this.homework = homework;
        }

        @Override
        public void handle(Event event) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/client/view/fxml/homeworkRepliesList.fxml"));
                mainPane = loader.load();
                ((HomeworkRepliesListHandler) loader.getController()).loadReplies(homework);
                mainPane.getStylesheets().add(getClass().getResource("/client/view/fxml/login.css").toExternalForm());
                stage.getScene().setRoot(mainPane);
                stage.show();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }


    }
}