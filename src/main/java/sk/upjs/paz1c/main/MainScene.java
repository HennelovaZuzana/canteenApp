package sk.upjs.paz1c.main;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.*;
import javafx.stage.Stage;

public class MainScene extends Application {

	@Override
	public void start(Stage primaryStage) throws Exception {
		MainSceneController controller = new MainSceneController();
		FXMLLoader loader = new FXMLLoader(getClass().getResource("MainScene.fxml")); //vytvorit
		loader.setController(controller);
		Parent parent = loader.load();
		Scene scene = new Scene(parent);
		primaryStage.setScene(scene);
		primaryStage.setTitle("Tvorba obedového menu");
		primaryStage.show();

	}
	public static void main(String[] args) {
		launch(args);
	}

}
