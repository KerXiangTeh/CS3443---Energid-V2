package application;

import application.controller.LoginController;
import application.model.User;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
    	// load the users from the data file
        User.load("data/users.csv");
        
        FXMLLoader loader = new FXMLLoader(getClass().getResource("Login.fxml"));
        Parent root = loader.load();
        LoginController controller = (LoginController) loader.getController();
        
        // we will save the primary stage so that other views can also be loaded in the primary stage
        controller.setPrimaryStage(primaryStage);
        primaryStage.setTitle("Login");
        primaryStage.setScene(new Scene(root));
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}