package application;

import java.io.IOException;

import java.sql.SQLException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import model.Client;
import server.Message;
/***
 * 
 * Controller for the login frame.
 *
 */
public class LoginController {

	@FXML
	private PasswordField password_field;

	@FXML
	private TextField tf_user;

	private static Client cl;

	public static Client takeClient() {
		return cl;
	}

	public static void reset() {
		cl = null;
	}

	public static void save(Client client) {
		cl = client;
	}

	@FXML
	void onLogIn(ActionEvent event) throws SQLException, IOException, InterruptedException, ExecutionException {
		String email = tf_user.getText();
		String password = new Encrypter(password_field.getText()).encrypt();
		boolean result = false;
		Client client = new Client();
		client.setEmail(email);
		client.setPassword(password);
		Message ms = new Message(SavedItems.LOGIN, client);
		ClientThread clTh = new ClientThread(ms);
		ExecutorService ex = Executors.newSingleThreadExecutor();
		Future<Message> future = ex.submit(clTh);
		Message resp = future.get();
		Client respCl = (Client) resp.getData();
		save(respCl);
		if (respCl != null && respCl.getEmail().compareTo(email) == 0
				&& respCl.getPassword().compareTo(password) == 0) {
			result = true;
			Parent root = FXMLLoader.load(getClass().getResource("/fxml/MainFrame.fxml"));
			Scene scene = new Scene(root);
			Stage stg = (Stage) ((Node) event.getSource()).getScene().getWindow();
			stg.hide();
			scene.getStylesheets().add("/Images/design.css");
			stg.setScene(scene);

			stg.show();
		}
		if (result == false)
			AlertBox.alertBox("Error !", " Incorrect Username or Password");

	}

	@FXML
	void onRegister(ActionEvent event) throws IOException {

		Parent root = FXMLLoader.load(getClass().getResource("/fxml/RegisterFirstFrame.fxml"));
		Scene scene = new Scene(root);
		scene.getStylesheets().add("/Images/design.css");
		Stage stg = (Stage) ((Node) event.getSource()).getScene().getWindow();
		stg.hide();
		stg.setScene(scene);
		stg.show();
	}

}
