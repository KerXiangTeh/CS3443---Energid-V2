package application.controller;

import java.util.Date;

import application.model.MealEntry;
import application.model.MealEntry.MealTime;
import application.model.Profile;
import application.model.Utils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.text.Text;

/**
 * Controller class for managing the user login
 *
 */
public class NewEntryController extends BaseController{
	MealEntry entry;

	@FXML
	private Label lblMessage;

	@FXML
	private TextField txtProtein;

	@FXML
	private RadioButton rdbBreakfast;

	@FXML
	private Button btnLogout;

	@FXML
	private Button btnAdd;	

	@FXML
	private TextField txtFat;

	@FXML
	private RadioButton rdbLunch;

	@FXML
	private TextField txtCarbs;

	@FXML
	private Text lblCaloryCount;

	@FXML
	private Text txtUsername;

	@FXML
	private Button btnLogin;

	@FXML
	private RadioButton rdbDinner;

	@FXML
	private Button btnHome;

	@FXML
	private TextField txtFoodName;

	@FXML
	void onAddClicked(ActionEvent event) {
		// save the meal entry and go to home stats page
		Profile.get().getMealHistory().add(entry);
		ControllerUtils.openStatsHomePage(primaryStage);
	}

	/**
	 * Show the total number of calories for the given food type counts
	 */
	private void recalculateCaloryCount() {
		lblCaloryCount.setText(entry.calculateCalories() + " Calories");
		setAddButtonStatus();
	}

	/**
	 * Update whether the button is enabled or disabled based on whether the 
	 * enough information is specified by the user
	 */
	private void setAddButtonStatus() {
		btnAdd.setDisable(entry.getFoodName() == null || entry.getFoodName().length() == 0 || entry.calculateCalories() == 0);
	}

	@FXML
	void onHomeClicked(ActionEvent event) {
		ControllerUtils.openStatsHomePage(primaryStage);
	}

	@FXML
	void onLogoutClicked(ActionEvent event) {
		ControllerUtils.openLoginPage(primaryStage);
	}
	
    @FXML
    void onRdbSelected(ActionEvent event) {
    	// set the meal time based on which radio button is selected
    	if (rdbBreakfast.isSelected()) entry.setMealTime(MealTime.BREAKFAST);
    	if (rdbLunch.isSelected()) entry.setMealTime(MealTime.LUNCH);
    	if (rdbDinner.isSelected()) entry.setMealTime(MealTime.DINNER);
    }

	@FXML
	public void initialize() {
		// show the username on the head
		txtUsername.setText(Profile.get().getCurrentUser().getRealName());
		
		// create an empty meal entry which will get updated as user specifies data on the UI controls
		entry = new MealEntry(Profile.get().getCurrentUser().getUsername(), new Date(), null, 0, 0, 0, MealTime.BREAKFAST);
		
		// set all the radio button to a single group so that selecting one radio button would deselect others
		ToggleGroup group = new ToggleGroup();
		rdbBreakfast.setToggleGroup(group);
		rdbLunch.setToggleGroup(group);
		rdbDinner.setToggleGroup(group);
		
		// setup listeners so that mean entry data is updated as user type on the text fields
		txtProtein.textProperty().addListener((observable, oldValue, newValue) -> {
			entry.setProteins((int) Utils.getValue(newValue));
			recalculateCaloryCount();
		});
		txtCarbs.textProperty().addListener((observable, oldValue, newValue) -> {
			entry.setCarbs((int) Utils.getValue(newValue));
			recalculateCaloryCount();
		});
		txtFat.textProperty().addListener((observable, oldValue, newValue) -> {
			entry.setFat((int) Utils.getValue(newValue));
			recalculateCaloryCount();
		});
		txtFoodName.textProperty().addListener((observable, oldValue, newValue) -> {
			entry.setFoodName(txtFoodName.getText());
			setAddButtonStatus();
		});
		
		// initialize the add button status (to disabled)
		setAddButtonStatus();
		setButtonStyles1(btnAdd);
    	setButtonStyles1(btnHome);
    	setButtonStyles2(btnLogout);
    	setBackgroundStyle(pnlBackground);
	}
}
