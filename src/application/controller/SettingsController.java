package application.controller;

import application.model.MealIntake;
import application.model.Profile;
import application.model.User;
import application.model.Utils;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.chart.PieChart;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.text.Text;

/**
 * Controller class for managing the user login
 *
 */
public class SettingsController extends BaseController{
	int totalCaloryCount;
	int totalProtein;
	int totalCarbs;
	int totalFat;

	PieChart.Data proteinData;
	PieChart.Data carbsData;
	PieChart.Data fatData;
	PieChart.Data otherData;
	
	
	@FXML
	private PieChart chrtTotal;

	@FXML
	private Label lblMessage;

	@FXML
	private TextField txtCarbs;

	@FXML
	private TextField txtProtein;

	@FXML
	private Text txtUsername;

	@FXML
	private Button btnSave;

	@FXML
	private Button btnLogout;

	@FXML
	private TextField txtTotalCaloryCount;

	@FXML
	private TextField txtFat;

	@FXML
	private Button btnHome;

	@FXML
	void onSaveClicked(ActionEvent event) {
		// update the user goals and save everything back to users csv file
		User user = Profile.get().getCurrentUser();
		user.setTotalCalores(totalCaloryCount);
		user.setTotalProteins(totalProtein);
		user.setTotalCarbs(totalCarbs);
		user.setTotalFat(totalFat);
		User.save("data/users.csv");
		ControllerUtils.openStatsHomePage(primaryStage);
	}

	@FXML
	void onHomeClicked(ActionEvent event) {
		ControllerUtils.openStatsHomePage(primaryStage);
	}

	@FXML
	void onLogoutClicked(ActionEvent event) {
		ControllerUtils.openLoginPage(primaryStage);
	}
	
	/**
	 * Helper function that calculated and updates the pie chart based on the goals set
	 */
	private void updateChart() {
		// calculate percentages
		double pPerc = calculatePercentage(totalProtein, MealIntake.PROTEIN_CALORY);
		double cPerc = calculatePercentage(totalCarbs, MealIntake.CARB_CALORY);
		double fPerc = calculatePercentage(totalFat, MealIntake.FAT_CALORY);
		double oPerc = 100 - (pPerc + cPerc + fPerc);
		// if the other percentage is negative that mean the calories set for proteins, 
		// carbs and fat are over the calorie goal. to resolve this problem we'll 
		// update the calorie goal
		if (oPerc < 0) {
			MealIntake intake = new MealIntake(totalProtein, totalCarbs, totalFat);
			totalCaloryCount = intake.calculateCalories();
			txtTotalCaloryCount.setText(totalCaloryCount+"");
			updateChart();
			return;
		}
		// set the percentages for each pie data
		proteinData.setPieValue(pPerc);
		carbsData.setPieValue(cPerc);
		fatData.setPieValue(fPerc);
		otherData.setPieValue(oPerc);
		// add tooltips for each slice showing the percentage
		chrtTotal.getData().forEach(data -> {
		    String percentage = String.format("%s: %.2f%%", data.getName(), (data.getPieValue()));
		    Tooltip toolTip = new Tooltip(percentage);
		    Tooltip.install(data.getNode(), toolTip);
		});
	}
	
	/**
	 * Calcualte the percentage of calories based on meal type and total calory count
	 * @param count
	 * @param multiplier
	 * @return
	 */
	private double calculatePercentage(int count, int multiplier) {
		if (totalCaloryCount <= 0) {
			return 0;
		}
		return 100.0 * count * multiplier / totalCaloryCount;
	}
	
	@FXML
	public void initialize() {
		// show the username on the header
		txtUsername.setText(Profile.get().getCurrentUser().getRealName());
		
		// load the existing goals
		totalCaloryCount = Profile.get().getCurrentUser().getTotalCalories();
		totalProtein = Profile.get().getCurrentUser().getTotalProteins();
		totalCarbs = Profile.get().getCurrentUser().getTotalCarbs();
		totalFat = Profile.get().getCurrentUser().getTotalFat();
		
		// show the existing goals
		txtTotalCaloryCount.setText( "" + totalCaloryCount);
		txtProtein.setText( "" + totalProtein);
		txtCarbs.setText( "" + totalCarbs);
		txtFat.setText( "" + totalFat);
		
		// add listeners to the ui controls to update the goals when they change
		txtTotalCaloryCount.focusedProperty().addListener((observable, oldValue, newValue) -> {
			totalCaloryCount = (int) (Utils.getValue(txtTotalCaloryCount.getText()));
			updateChart();
		});
		txtProtein.textProperty().addListener((observable, oldValue, newValue) -> {
			totalProtein = (int) (Utils.getValue(newValue));
			updateChart();
		});
		txtCarbs.textProperty().addListener((observable, oldValue, newValue) -> {
			totalCarbs = (int) (Utils.getValue(newValue));
			updateChart();
		});
		txtFat.textProperty().addListener((observable, oldValue, newValue) -> {
			totalFat = (int) (Utils.getValue(newValue));
			updateChart();
		});

		// configure the pie chart
		proteinData = new PieChart.Data("Protein", 0);
		carbsData = new PieChart.Data("Carbs", 0);
		fatData = new PieChart.Data("Fat", 0);
		otherData = new PieChart.Data("Other", 0);
		
		ObservableList<PieChart.Data> pieChartData =
                FXCollections.observableArrayList(proteinData,carbsData,fatData,otherData);
		chrtTotal.setData(pieChartData);
		otherData.getNode().setStyle("-fx-pie-color: gray;-fx-opacity: 0.1;");
		chrtTotal.setLabelsVisible(false);
		
		updateChart();
		
		// set UI control styles
		setButtonStyles1(btnSave);
    	setButtonStyles1(btnHome);
    	setButtonStyles2(btnLogout);
    	setBackgroundStyle(pnlBackground);
	}
}
