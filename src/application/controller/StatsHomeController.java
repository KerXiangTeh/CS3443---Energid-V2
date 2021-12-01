package application.controller;

import java.util.Calendar;
import java.util.Date;
import java.util.Map;

import application.model.MealHistory;
import application.model.MealIntake;
import application.model.Profile;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.XYChart;
import javafx.scene.chart.XYChart.Data;
import javafx.scene.control.Button;
import javafx.scene.control.ProgressBar;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Text;

/**
 * Controller class for managing the home statistics page
 *
 */
public class StatsHomeController extends BaseController{

    @FXML
    private ProgressBar prgFat;

    @FXML
    private Text lblCalories;

    @FXML
    private Button btnLogout;

    @FXML
    private ProgressBar prgCarbs;

    @FXML
    private BarChart<?, ?> chrtWeek;

    @FXML
    private GridPane grdPanel;

    @FXML
    private Text txtUsername;

    @FXML
    private Button btnAdd;

    @FXML
    private Text lblProtein;

    @FXML
    private ProgressBar prgCalories;

    @FXML
    private Text lblCarbs;

    @FXML
    private Text lblFat;
    
    @FXML
    private Button btnSettings;
    
    @FXML
    private ProgressBar prgProtein;
    

    @FXML
    private Button btnDaily;
    

    @FXML
    void onDailyClicked(ActionEvent event) {
    	ControllerUtils.openDailyStatsPage(primaryStage);
    }
    
    @FXML
    void onAddClicked(ActionEvent event) {
    	ControllerUtils.openNewEntryPage(primaryStage);
    }

    @FXML
    void onLogoutClicked(ActionEvent event) {
    	ControllerUtils.openLoginPage(primaryStage);
    }

    @FXML
    void onSettingsClicked(ActionEvent event) {
    	ControllerUtils.openSettingsPage(primaryStage);
    }
    
	@FXML
	public void initialize() {
		// show the username on the header
		txtUsername.setText(Profile.get().getCurrentUser().getRealName());
		
		// update the bar chart with the current weeks calory statistics
		updateChart(Profile.get().getMealHistory().getCurrentWeek());
		
		// retrieve the meal statistics for today
		MealIntake todayStats = Profile.get().getMealHistory().getTodayStats();
		
		// calculate the data related to showing the todays summarized meal stats
		int cals = todayStats.calculateCalories();
		int totCals = Profile.get().getCurrentUser().getTotalCalories();
		int proteins = todayStats.getProteins();
		int totProteins = Profile.get().getCurrentUser().getTotalProteins();
		int carbs = todayStats.getCarbs();
		int totCarbs = Profile.get().getCurrentUser().getTotalCarbs();
		int fat = todayStats.getFat();
		int totFat = Profile.get().getCurrentUser().getTotalFat();
		
		// show the todays summarized meal stats on the ui controls
		lblCalories.setText(cals +"/"+totCals);
		lblProtein.setText(proteins +"/"+totProteins);
		lblCarbs.setText(carbs+"/"+totCarbs);
		lblFat.setText(fat +"/"+totFat);
		
		prgCalories.setProgress(1.0 * cals / totCals);
		prgProtein.setProgress(1.0 * proteins / totProteins);
		prgCarbs.setProgress(1.0 * carbs / totCarbs);
		prgFat.setProgress(1.0 * fat/ totFat);
		
		// set the ui styles
		setButtonStyles1(btnAdd);
    	setButtonStyles1(btnSettings);
    	setButtonStyles1(btnDaily);
    	setButtonStyles2(btnLogout);
    	setBackgroundStyle(pnlBackground);
	}

	private void updateChart(int weekNo) {
		// load the stats for the given week
		Map<String, MealIntake> currentWeekStats = Profile.get().getMealHistory().getWeekStats(weekNo);
		
		// remove any existing data series from the chart
		chrtWeek.getData().clear();
		
		// create a new data series
		XYChart.Series weekStats = new XYChart.Series();
		
		// add data points to the data series based on weekly stats
        for(int i = 0; i < MealHistory.DAYS.length; i++) {
        	String day = MealHistory.DAYS[i]; // sunday/monday/tuesday/etc.
        	// y - value is the amount of calories for that day
        	Data<String, Integer> data = new XYChart.Data<String, Integer>(day, currentWeekStats.get(day).calculateCalories());
			weekStats.getData().add(data);
        }
        
        chrtWeek.setLegendVisible(false);
        chrtWeek.getData().add(weekStats);
        
        // incase the week corresponds to current week we'll hightlight the bar that corresponds to today
        if (weekNo == Profile.get().getMealHistory().getCurrentWeek()) {
            Calendar cal = Calendar.getInstance();
            cal.setTime(new Date());
            int todayDayOfWeek = cal.get(Calendar.DAY_OF_WEEK) - 1;
            Node n = chrtWeek.lookup(".data"+todayDayOfWeek+".chart-bar");
	        n.setStyle("-fx-bar-fill: blue");
        }
	}
	
}
