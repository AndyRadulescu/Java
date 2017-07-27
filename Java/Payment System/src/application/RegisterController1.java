package application;

import java.io.IOException;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
/***
 * 
 * Controller class for the first register frame.
 *
 */

public class RegisterController1 {

	@FXML
	private Label incorrectRepetPass;

	@FXML
	private Label incorrectEmail;

	@FXML
	private TextField tf_password;

	@FXML
	private Label incorrectUser;

	@FXML
	private TextField tf_email;

	@FXML
	private Label incorrectPass;

	@FXML
	private TextField tf_repetpassword;

	@FXML
	void OnSubmit(ActionEvent event) throws IOException, ClassNotFoundException, SQLException {

		int ok1 = 1, ok2 = 1, ok3 = 1;
		if (this.tf_email.getText().isEmpty() || !this.tf_email.getText().contains("@")) {
			ok1 = 1;
			incorrectEmail.setVisible(true);
		} else {
			incorrectEmail.setVisible(false);
			ok1 = 0;
		}

		if (this.tf_password.getText().isEmpty() || this.tf_password.getText().length() < 6) {

			incorrectPass.setVisible(true);
			ok2 = 1;
		} else {

			incorrectPass.setVisible(false);
			ok2 = 0;
		}
		if (this.tf_repetpassword.getText().isEmpty()
				|| this.tf_repetpassword.getText().compareTo(this.tf_password.getText()) != 0) {
			ok3 = 1;
			incorrectRepetPass.setVisible(true);
		} else {
			incorrectRepetPass.setVisible(false);
			ok3 = 0;
		}

		if (ok1 == 0 && ok2 == 0 && ok3 == 0) {
			Encrypter encrypter=new Encrypter(this.tf_password.getText());
			RegisterController2.val(this.tf_email.getText(), encrypter.encrypt());
			Parent root = FXMLLoader.load(getClass().getResource("/fxml/RegisterSecondFrame.fxml"));
			Scene scene = new Scene(root);
			Stage stg = (Stage) ((Node) event.getSource()).getScene().getWindow();
			stg.hide();
			scene.getStylesheets().add("/Images/design.css");
			stg.setScene(scene);
			stg.show();
		}

	}

	@FXML
	void onCancel(ActionEvent event) throws IOException {
		Parent reg_parent = FXMLLoader.load(getClass().getResource("/fxml/HomePage.fxml"));
		Scene reg_scene = new Scene(reg_parent);
		Stage stg = (Stage) ((Node) event.getSource()).getScene().getWindow();
		stg.hide();
		stg.setScene(reg_scene);
		stg.show();
	}
	private String encrypt(String input){
		String result=null;
		try {
			MessageDigest md=MessageDigest.getInstance("MD5");
			md.update(input.getBytes());
			result=new BigInteger(1,md.digest()).toString(16);
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		return result;
	}

}
