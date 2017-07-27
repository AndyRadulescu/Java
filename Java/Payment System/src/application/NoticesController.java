package application;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.PasswordField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import model.Client;
import model.Bill;
import model.Notice;
import server.Message;
/***
 * 
 * Controller for the notices frame.
 *
 */

public class NoticesController implements Initializable {
	@FXML
	ListView<Notice> listView;
	ListView<Notice> archive = new ListView<>();
	Client cl = LoginController.takeClient();

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		Message out = new Message();
		out.setData(cl);
		out.setAction(SavedItems.GETNOTICES);
		ArrayList<Notice> response = new ArrayList<>();
		ArrayList<Notice> unarchived = new ArrayList<>();
		try {
			ClientThread clTh = new ClientThread(out);
			ExecutorService ex = Executors.newSingleThreadExecutor();
			Future<Message> future = ex.submit(clTh);
			Message resp = (Message) future.get();
			response = (ArrayList) resp.getData();
			System.out.println(response);
			for (Notice ins : response) {
				if (ins.getStatus() == 0)
					unarchived.add(ins);
				else
					archive.getItems().add(ins);
			}
		} catch (InterruptedException | ExecutionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		ObservableList<Notice> obList = FXCollections.observableArrayList();
		obList.addAll(unarchived);
		listView.getItems().addAll(obList);
		listView.setOnMouseClicked(new EventHandler<MouseEvent>() {

			@Override
			public void handle(MouseEvent event) {
				if (event.getClickCount() == 2) {
					manualPayment();
				}
			}
		});

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

	@FXML
	void onDelete(ActionEvent e) {
		Notice item = listView.getSelectionModel().getSelectedItem();
		listView.getItems().remove(item);
		archive.getItems().add(item);
		Message out = new Message();
		out.setData(item);
		out.setAction(SavedItems.ARCHIVENOTICE);
		ClientThread clTh = new ClientThread(out);
		ExecutorService ex = Executors.newSingleThreadExecutor();
		Future<Message> future = ex.submit(clTh);
		try {
			future.get();
		} catch (InterruptedException | ExecutionException e1) {
			e1.printStackTrace();
		}
		ex.shutdown();
	}

	@FXML
	void onArchive(ActionEvent e) {
		Stage window = new Stage();
		Scene scene;
		VBox layout = new VBox(10);
		layout.setPadding(new Insets(10, 10, 10, 10));
		layout.getChildren().add(archive);
		window.initModality(Modality.APPLICATION_MODAL);
		window.setTitle("Archived notices");
		scene = new Scene(layout, 550, 300);
		scene.getStylesheets().add("/Images/design.css");
		window.setScene(scene);
		window.showAndWait();
		;

	}

	void manualPayment() {
		Stage window = new Stage();
		window.initModality(Modality.APPLICATION_MODAL);
		window.setTitle("Type password in!");
		window.setMinWidth(250);
		Label label = new Label();
		label.setText("Incorrect password");
		label.setVisible(false);
		Button okButton = new Button("Introduce password");
		PasswordField tf = new PasswordField();
		okButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				if (tf.getText().compareTo(cl.getPassword()) == 0) {
					try {
						Notice currentItem = listView.getSelectionModel().getSelectedItem();
						Bill fac = new Bill();
						fac.setIdBill(currentItem.getBill().getIdBill());
						Message msg = new Message(SavedItems.MANUALPAYMENT, fac);
						ClientThread clTh = new ClientThread(msg);
						ExecutorService ex = Executors.newSingleThreadExecutor();
						Future<Message> future = ex.submit(clTh);
						Message response = (Message) future.get();
						ex.shutdown();
						if (response.getAction() == 0) {
							AlertBox.alertBox("Succes!", "The manual paymend was successful !");
						}
					} catch (Exception e) {
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

	}

}
