package application.controller;


import javafx.fxml.FXML;
import javafx.scene.Cursor;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

/**
 * Base Controller class for managing all the pages base functionalities
 *
 */
public class BaseController {
	Stage primaryStage;
	
    @FXML
    protected AnchorPane pnlBackground;
    
    /**
     * Save the primary stage so that this page can load other pages if needed
     * @param primaryStage
     */
	public void setPrimaryStage(Stage primaryStage) {
		this.primaryStage = primaryStage;
	}

	/**
	 * Apply a set of styles to a button
	 * @param button
	 */
	protected void setButtonStyles1(Button button) {
		// define styles 
		final String IDLE_BUTTON_STYLE = "-fx-background-color:  rgba(160, 213,148);-fx-background-radius:30";
		final String HOVERED_BUTTON_STYLE = "-fx-background-color:  rgba(148, 211,146);-fx-background-radius:30";;
		
		// set button dimensions
		button.setPrefSize(60, 60);
		
		// set the css style and mouse cursor for the buttons and changes to them based on mouse hover
		button.setStyle(IDLE_BUTTON_STYLE);
		button.setOnMouseEntered(e -> {
			button.setStyle(HOVERED_BUTTON_STYLE);
			primaryStage.getScene().setCursor(Cursor.HAND);
		});
		button.setOnMouseExited(e -> {
			button.setStyle(IDLE_BUTTON_STYLE);
			primaryStage.getScene().setCursor(Cursor.DEFAULT);
		});
		
	}
	
	/**
	 * Apply a set of styles to a button
	 * @param button
	 */
	protected void setButtonStyles2(Button button) {
		// define styles 
		final String IDLE_BUTTON_STYLE = "-fx-background-color:  rgba(237, 152,146);-fx-background-radius:30";
		final String HOVERED_BUTTON_STYLE = "-fx-background-color:  rgba(201, 106,99);-fx-background-radius:30";;
		
		// set button dimensions
		button.setPrefSize(40, 40);
		
		//set the css style for the buttons and changes to them based on mouse hover
		button.setStyle(IDLE_BUTTON_STYLE);
		button.setOnMouseEntered(e -> {
			button.setStyle(HOVERED_BUTTON_STYLE);
			primaryStage.getScene().setCursor(Cursor.HAND);
		});
		button.setOnMouseExited(e -> {
			button.setStyle(IDLE_BUTTON_STYLE);
			primaryStage.getScene().setCursor(Cursor.DEFAULT);
		});
		
	}
	
	/**
	 * Set background style for the pane
	 * @param pane
	 */
	protected void setBackgroundStyle(Pane pane) {
		pane.setStyle("-fx-background-image: url('file:images/background.png');-fx-background-size: cover,auto;fx-background-repeat: no-repeat");
	}
}
