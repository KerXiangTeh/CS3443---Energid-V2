package application.controller;

import java.io.IOException;

import application.Main;
import application.model.Profile;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * Helper class for functions useful for Controller classes
 *
 */
public class ControllerUtils {
	
	/**
	 * Open the given view on the given stage
	 * @param primaryStage
	 * @param fxmlFile
	 * @param title
	 */
	private static void openPage(Stage primaryStage, String fxmlFile, String title) {
		// create the fxml loader for the fxml view file
		FXMLLoader loader = new FXMLLoader(Main.class.getResource(fxmlFile));
		try {
			// load the view
			Parent root = loader.load();
			// get the controller associated with the view and save the primary stage
			Object controller = loader.getController();
			if (controller!=null && BaseController.class.isInstance(controller)) {
				((BaseController )controller).setPrimaryStage(primaryStage);
			}

			Scene secondScene = new Scene(root);
			primaryStage.setTitle(title);
			primaryStage.setScene(secondScene);
			primaryStage.show();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}
	
	/**
	 * Open the new entry page
	 * @param primaryStage
	 */
	public static void openNewEntryPage(Stage primaryStage) {
		openPage(primaryStage, "NewEntry.fxml", "New Entry");
	}
	
	/**
	 * Open the login page
	 * @param primaryStage
	 */
	public static void openLoginPage(Stage primaryStage) {
		Profile.clearProfile();
		openPage(primaryStage, "Login.fxml", "Login");
	}
	
	/**
	 * Open the settings page
	 * @param primaryStage
	 */
	public static void openSettingsPage(Stage primaryStage) {
		openPage(primaryStage, "Settings.fxml", "Settings");
	}

	/**
	 * Open the home page
	 * @param primaryStage
	 */
	public static void openStatsHomePage(Stage primaryStage) {
		openPage(primaryStage, "StatsHome.fxml", "Home");
	}
	
	/**
	 * Open the daily statistics page
	 * @param primaryStage
	 */
	public static void openDailyStatsPage(Stage primaryStage) {
		openPage(primaryStage, "DailyStats.fxml", "Daily Stats");
	}
}
