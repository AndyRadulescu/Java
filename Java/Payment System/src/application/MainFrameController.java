package application;

import java.io.IOException;


import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
/***
 * 
 * Controller for the main frame.
 *
 */

public class MainFrameController {

	@FXML
	void onAdaugaFonduri(ActionEvent event) throws IOException {

		Parent root = FXMLLoader.load(getClass().getResource("/fxml/FundsScene.fxml"));
		Scene scene = new Scene(root);
		scene.getStylesheets().add("/Images/design.css");
		Stage stg = (Stage) ((Node) event.getSource()).getScene().getWindow();
		stg.hide();
		stg.setScene(scene);
		stg.show();
	}

	@FXML
	void onFacturi(ActionEvent event) throws IOException {

		Parent root = FXMLLoader.load(getClass().getResource("/fxml/BillsScene.fxml"));
		Scene scene = new Scene(root);
		scene.getStylesheets().add("/Images/design.css");
		Stage stg = (Stage) ((Node) event.getSource()).getScene().getWindow();
		stg.hide();
		stg.setScene(scene);
		stg.show();
	}

	@FXML
	void onFurnizori(ActionEvent event) throws IOException {

		Parent root = FXMLLoader.load(getClass().getResource("/fxml/ProvidersScene.fxml"));
		Scene scene = new Scene(root);
		scene.getStylesheets().add("/Images/design.css");
		Stage stg = (Stage) ((Node) event.getSource()).getScene().getWindow();
		stg.hide();
		stg.setScene(scene);
		stg.show();
	}
	/*
	 * @FXML void onAdaugaFonduri(ActionEvent event) { EntityManagerFactory emf
	 * = Persistence.createEntityManagerFactory("Sist_bancar"); EntityManager em
	 * = emf.createEntityManager(); Client client
	 * =(Client)em.find(Client.class,cl.getIdClient());
	 * em.getTransaction().begin();
	 * client.setValoareCont(client.getValoareCont()+Integer.parseInt(tf_fond.
	 * getText())); em.getTransaction().commit();
	 * System.out.println(client.getValoareCont()); }
	 */

	@FXML
	void onLogOut(ActionEvent event) throws IOException {

		Parent root = FXMLLoader.load(getClass().getResource("/fxml/HomePage.fxml"));
		Scene scene = new Scene(root);
		scene.getStylesheets().add("/Images/design.css");
		Stage stg = (Stage) ((Node) event.getSource()).getScene().getWindow();
		LoginController.reset();
		stg.hide();
		stg.setScene(scene);
		stg.show();

	}

	@FXML
	void onInstiintari(ActionEvent event) throws IOException {
		Parent root = FXMLLoader.load(getClass().getResource("/fxml/NoticesScene.fxml"));
		Scene scene = new Scene(root);
		scene.getStylesheets().add("/Images/design.css");
		Stage stg = (Stage) ((Node) event.getSource()).getScene().getWindow();
		stg.hide();
		stg.setScene(scene);
		stg.show();
	}
}
