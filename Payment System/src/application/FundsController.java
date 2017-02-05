package application;

import java.io.IOException;

import java.net.URL;

import java.util.ResourceBundle;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import model.Client;
import server.Message;
/***
 * 
 * Controller for the funds scene.
 *
 */
public class FundsController implements Initializable {

	@FXML
	private Label lb_fonduri;

	@FXML
	private TextField tf_money;

	@FXML
	private Label lb_incorrect;

	private static Client cl = LoginController.takeClient();

	@FXML
	void onFonduri(ActionEvent event) {
		lb_fonduri.setText(String.valueOf(cl.getAccountValue()));
		if (!tf_money.getText().isEmpty()) {

			Stage window = new Stage();
			window.initModality(Modality.APPLICATION_MODAL);
			window.setTitle("Type password !");
			window.setMinWidth(250);

			Label label = new Label();
			label.setText("Incorrect password");
			label.setVisible(false);
			Button okButton = new Button("Introduce password");
			PasswordField tf = new PasswordField();

			okButton.setOnAction(new EventHandler<ActionEvent>() {
				@Override
				public void handle(ActionEvent event) {
					if (new Encrypter(tf.getText()).encrypt().compareTo(cl.getPassword()) == 0) {
						try {
							Client client = new Client();
							client.setAccountValue(cl.getAccountValue() + Integer.parseInt(tf_money.getText()));
							client.setIdClient(cl.getIdClient());
							Message ms = new Message(SavedItems.ADDFUNDS, client);
							ClientThread clTh = new ClientThread(ms);
							ExecutorService ex = Executors.newSingleThreadExecutor();
							Future<Message> future = ex.submit(clTh);
							Message message = (Message) future.get();
							ex.shutdown();
							Client response = (Client) message.getData();
							cl = response;
							lb_incorrect.setVisible(false);
							window.close();
							label.setVisible(false);

							lb_fonduri.setText(String.valueOf(cl.getAccountValue()));
							if (message.getAction() == 1) {

								AlertBox.alertBox("Succes!", "Funds have beed added!");
								tf_money.setText(null);
							}
						} catch (InterruptedException | ExecutionException ex) {
							System.out.println("Error talking to " + SavedItems.HOST + ":" + SavedItems.PORT);
						}
					} else
						label.setVisible(true);
				}
			});

			Button closeButton = new Button("Cancel");
			closeButton.setOnAction(e -> window.close());

			VBox layout = new VBox(10);
			layout.getChildren().addAll(okButton, tf, label, closeButton);
			layout.setAlignment(Pos.CENTER);

			Scene scene = new Scene(layout);
			scene.getStylesheets().add("/Images/design.css");
			window.setScene(scene);
			window.showAndWait();

		} else
			lb_incorrect.setVisible(true);

	}

	@FXML
	void onBack(ActionEvent event) throws IOException {

		Parent root = FXMLLoader.load(getClass().getResource("/fxml/MainFrame.fxml"));
		Scene scene = new Scene(root);
		Stage stg = (Stage) ((Node) event.getSource()).getScene().getWindow();
		stg.hide();
		scene.getStylesheets().add("/Images/design.css");
		stg.setScene(scene);
		stg.show();
	}

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		lb_fonduri.setText(String.valueOf(cl.getAccountValue()));
		try {
			String email = cl.getEmail();
			String pass = cl.getPassword();
			Client cl = new Client();
			cl.setEmail(email);
			cl.setPassword(pass);
			Message ms = new Message(SavedItems.LOGIN, cl);
			ClientThread clTh = new ClientThread(ms);
			ExecutorService ex = Executors.newSingleThreadExecutor();
			Future<Message> future = ex.submit(clTh);
			Message resp = (Message) future.get();
			ex.shutdown();
			Client respCl = (Client) resp.getData();
			int result = resp.getAction();
			if (result == 1)
				lb_fonduri.setText(String.valueOf(respCl.getAccountValue()));
		} catch (ExecutionException | InterruptedException ex) {
			System.out.println("Error talking to " + SavedItems.HOST + ":" + SavedItems.PORT);
		}

	}

}
