package client.view;

import client.controller.ClientController;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;

public class LoginHandler {

	@FXML
	private TextField loginField;

	@FXML
	private TextField passwordField;

	@FXML
	private Button loginButton;
	@FXML
	private Pane root;
	@FXML
	private VBox box;

	private ClientController controller;

	public LoginHandler() {
		System.out.println("first");
		controller = ClientController.getInstance();
	}

	@FXML
	public void initialize() {
		System.out.println("second");
		loginFieldInitialize();
	}

	@FXML
	private void loginHandler() {
		String name = loginField.getText();
		String password = passwordField.getText();
		controller.login(name, password);
	}

	private void loginFieldInitialize() {
		loginField.focusedProperty().addListener(new ChangeListener<Boolean>() {
			@Override
			public void changed(ObservableValue<? extends Boolean> arg0, Boolean oldPropertyValue,
					Boolean newPropertyValue) {
				if (loginField.getText().equals("username")) {
					loginField.setText("");
				} else if (loginField.getText().equals("")) {
					loginField.setText("username");
				}
			}
		});
	}
}
