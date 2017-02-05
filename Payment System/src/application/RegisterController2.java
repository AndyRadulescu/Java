package application;

import java.io.IOException;
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
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import model.Client;
import server.Message;
/***
 * 
 * Controller class for the second register frame.
 *
 */
public class RegisterController2 implements SavedItems {

	@FXML
	private Label lb_CNP;

	@FXML
	private Label lb_nume;

	@FXML
	private TextField tf_nume;

	@FXML
	private TextField tf_CNP;

	@FXML
	private TextField tf_Adresa;

	@FXML
	private Label lb_Adresa;

	static String pass, email;

	public static void val(String email1, String pass1) {
		pass = pass1;
		email = email1;
	}

	@FXML
	void OnSubmit(ActionEvent event) throws IOException, InterruptedException, ExecutionException {

		boolean ok1 = false, ok2 = false, ok3 = false;

		if (this.tf_nume.getText().isEmpty()) {
			ok1 = true;
			this.lb_nume.setVisible(ok1);
		} else {
			ok1 = false;
			this.lb_nume.setVisible(ok1);
		}

		if (this.tf_CNP.getText().length() != 13) {
			ok2 = true;
			lb_CNP.setVisible(ok2);
		} else {
			ok2 = false;
			lb_CNP.setVisible(ok2);
		}

		if (this.tf_Adresa.getText().isEmpty()) {
			ok3 = true;
			lb_Adresa.setVisible(ok3);
		} else {
			ok3 = false;
			lb_Adresa.setVisible(ok3);
		}

		if (ok1 == false && ok2 == false && ok3 == false) {

			Client cl = new Client();
			cl.setAddress(tf_Adresa.getText());
			cl.setCnp(tf_CNP.getText());
			cl.setEmail(email);
			cl.setName(tf_nume.getText());
			cl.setPassword(pass);
			Message msg = new Message(REGISTER, cl);
			ClientThread clTh = new ClientThread(msg);
			ExecutorService ex = Executors.newSingleThreadExecutor();
			Future<Message> future = ex.submit(clTh);
			Message resp = (Message) future.get();
			String str = (String) resp.getData();
			ex.shutdown();
			if (str.compareTo("Signup succesful!") == 0) {
				Parent reg_parent = FXMLLoader.load(getClass().getResource("/fxml/HomePage.fxml"));
				Scene reg_scene = new Scene(reg_parent);
				Stage stg = (Stage) ((Node) event.getSource()).getScene().getWindow();
				stg.hide();
				reg_scene.getStylesheets().add("/Images/design.css");
				stg.setScene(reg_scene);
				stg.show();
				AlertBox.alertBox("Well done!", "Account creation successful!");
			} else {
				AlertBox.alertBox("Server problem!", "Can't connect to the db!");
			}

		}

	}

	@FXML
	void OnCancel(ActionEvent event) throws IOException {
		Parent reg_parent = FXMLLoader.load(getClass().getResource("/fxml/HomePage.fxml"));
		Scene reg_scene = new Scene(reg_parent);
		Stage stg = (Stage) ((Node) event.getSource()).getScene().getWindow();
		stg.hide();
		reg_scene.getStylesheets().add("/Images/design.css");
		stg.setScene(reg_scene);
		stg.show();
	}

	@FXML
	void OnBack(ActionEvent event) throws IOException {
		Parent reg_parent = FXMLLoader.load(getClass().getResource("/fxml/RegisterFirstFrame.fxml"));
		Scene reg_scene = new Scene(reg_parent);
		Stage stg = (Stage) ((Node) event.getSource()).getScene().getWindow();
		stg.hide();
		reg_scene.getStylesheets().add("/Images/design.css");
		stg.setScene(reg_scene);
		stg.show();
	}

}
