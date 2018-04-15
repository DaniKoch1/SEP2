package client.controller;

import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import client.view.ClientView;
import model.ClientModel;
import model.Post;
import model.communication.Auth;
import model.communication.ClientProxy;
import model.communication.Login;
import model.communication.Message;
import model.communication.WelcomingData;

public class ClientController {

	private ClientProxy server;
	private ClientModel model;
	private ClientView view;
	private static ClientController instance;

	private ClientController(ClientModel model, ClientView view) {
		instance = this;
		server = new ClientProxy();
		server.startConnection("localhost", 7777);
		this.model = model;
		this.view = view;
		this.view.startView();
	}

	public static ClientController getInstance(ClientModel model, ClientView view) {
		if (instance == null)
			instance = new ClientController(model, view);
		return instance;
	}

	public static ClientController getInstance() {
		if (instance == null)
			throw new IllegalStateException("There is no instance");
		return instance;
	}

	public void close() {
		server.close();
	}

	public Post[] getPosts() {
		Post[] posts = new Post[1];
		posts[0] = model.getPost();
		return posts;
	}

	public void login(String login, String pwd) {
		Auth auth = new Auth(login, encryptPwd(pwd));
		Message msg = new Message(), response;

		msg.createAuth(auth);

		try {
			response = server.sendMessage(msg);
			handleMessage(response);
		} catch (ClassNotFoundException | IOException e) {
			e.printStackTrace();
		}
	}

	private void handleMessage(Message msg) {
		switch (msg.getType()) {
		case Login:
			handleLogin(msg);
			break;
		case Fail:
		default:
			System.out.println("Error!!!");
			break;
		}
	}

	private void handleLogin(Message msg) {
		Login login = msg.getLogin();
		switch (login.getLoginStatus()) {
		case SUCCESS:
			WelcomingData data = login.getData();
			model.saveData(data);
			view.showPosts(login.getUserType());
			break;
		case FAILURE_LOGIN:
			break;
		case FAILURE_PWD:
			break;
		}
	}

	private String encryptPwd(String pwd) {
		MessageDigest dig;
		String encrypted = "";
		try {
			dig = MessageDigest.getInstance("SHA-256");
			dig.update("pwd".getBytes());
			encrypted = toHex(dig.digest());
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		return encrypted;
	}

	private String toHex(byte[] byteData) {
		StringBuffer hexString = new StringBuffer();
		for (int i = 0; i < byteData.length; i++) {
			String hex = Integer.toHexString(0xff & byteData[i]);
			if (hex.length() == 1)
				hexString.append('0');
			hexString.append(hex);
		}
		return hexString.toString();
	}

	public void addTeacher(String name, String email, String password,Boolean admin) {
		// TODO Auto-generated method stub
		
	}
	public void addStudent(String name, String email, String password) {
		// TODO Auto-generated method stub
		
	}

	public void deleteUser(Boolean user) {
		// TODO Auto-generated method stub
		
	}

	public void addParent(String name, String email, String password) {
		// TODO Auto-generated method stub
		
	}

	public String[][] getParentsForView() {
		String[][] parents = {{"Michal Pompa", "pompa@polska.pl", "Luke Pompa", "false"}};
		return parents;
	}

}
