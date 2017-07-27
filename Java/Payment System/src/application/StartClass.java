package application;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
/**
 * 
 * The main class of the client
 *
 */
public class StartClass extends Application {

	public static void main(String[] args) {
		Application.launch(args);
	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		AnchorPane root = FXMLLoader.load(getClass().getResource("/fxml/HomePage.fxml"));
		primaryStage.getIcons().add(new Image("/Images/pie-chart.png"));
		primaryStage.setTitle("Payment");
		Scene scene=new Scene(root);
		scene.getStylesheets().add("/Images/design.css");
		primaryStage.setScene(scene);
		primaryStage.show();

	}
}
