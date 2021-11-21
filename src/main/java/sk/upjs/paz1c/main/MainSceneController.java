package sk.upjs.paz1c.main;

import java.io.IOException;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class MainSceneController {

	    @FXML
	    private Button viewFoodsButton;

	    @FXML
	    private Label numOfToBuyLabel;

	    @FXML
	    private Label numOfOrdersLabel;

	    @FXML
	    private Button createOrderButton;

	    @FXML
	    private Button createIngredientButton;

	    @FXML
	    private Button viewOrdersButton;

	    @FXML
	    private Button viewIngredientsButton;

	    @FXML
	    private Button createFoodButton;

	    @FXML
	    private Button shoppingListButton;

	    @FXML
	    void initialize() {
	    	viewFoodsButton.setOnAction(new EventHandler<ActionEvent>() {
				@Override
				public void handle(ActionEvent event) {
					ViewFoodsSceneController controller = new ViewFoodsSceneController();
					FXMLLoader loader = new FXMLLoader(getClass().getResource("viewFoodsScene.fxml"));				
					loader.setController(controller);
					openWindow("Foods", loader);	
				}
			});
	    	viewIngredientsButton.setOnAction(new EventHandler<ActionEvent>() {
				@Override
				public void handle(ActionEvent event) {
					ViewIngredientsSceneController controller = new ViewIngredientsSceneController();
					FXMLLoader loader = new FXMLLoader(getClass().getResource("viewIngredientsScene.fxml"));				
					loader.setController(controller);
					openWindow("Ingredients", loader);	
					
				}
			});
	    	viewOrdersButton.setOnAction(new EventHandler<ActionEvent>() {
				@Override
				public void handle(ActionEvent event) {
					ViewOrdersSceneController controller = new ViewOrdersSceneController();
					FXMLLoader loader = new FXMLLoader(getClass().getResource("viewOrdersScene.fxml"));				
					loader.setController(controller);
					openWindow("Orders", loader);	
					
				}
			});
	    	createIngredientButton.setOnAction(new EventHandler<ActionEvent>() {
				@Override
				public void handle(ActionEvent event) {
					CreateIngredientSceneController c = new CreateIngredientSceneController();
					FXMLLoader loader = new FXMLLoader(getClass().getResource("createIngredientScene.fxml"));				
					loader.setController(c);
					Stage stage = openWindow("Create ingredient", loader);
					//ak chceme nastavit modalitu, tak cele open window manualne...
					//stage.initModality(Modality.APPLICATION_MODAL);
				}
			});
	    	
	    	createFoodButton.setOnAction(new EventHandler<ActionEvent>() {

				@Override
				public void handle(ActionEvent event) {
					// TODO Auto-generated method stub
					
				}
			});
	    	
	    	createOrderButton.setOnAction(new EventHandler<ActionEvent>() {

				@Override
				public void handle(ActionEvent event) {
					// TODO Auto-generated method stub
					
				}
			});
	    	shoppingListButton.setOnAction(new EventHandler<ActionEvent>() {

				@Override
				public void handle(ActionEvent event) {
					// TODO Auto-generated method stub
					
				}
			});
	    	
	    	//TODO vyhrat sa s Lables - pocet objednavok a veci na nakup - maybe - one day 
	    }
	    
	    private Stage openWindow(String title, FXMLLoader loader) {
			try {
				Parent parent = loader.load();
				Scene scene = new Scene(parent);
				Stage stage = new Stage();
				stage.setScene(scene);
				stage.setTitle(title);
				stage.show();
				return stage;
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return null;
	    }

}
