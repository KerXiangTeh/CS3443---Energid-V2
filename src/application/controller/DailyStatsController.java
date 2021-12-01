package application.controller;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import application.model.MealEntry;
import application.model.MealEntry.MealTime;
import application.model.MealIntake;
import application.model.Profile;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.chart.PieChart;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;

/**
 * Controller class for managing the daily statistics view
 *
 */
public class DailyStatsController extends BaseController {
	Date showingDate;

	@FXML
    private Button btnNext;

    @FXML
    private Text txtShowDate;

    @FXML
    private Button btnPre;
    
	
    @FXML
    private TableView<MealEntry> tblLunch;

    @FXML
    private Text txtLunchDetails;
    
    @FXML
    private TableView<MealEntry> tblDinner;

    @FXML
    private Text txtDinnerDetails;
    
	@FXML
    private PieChart chrtTotal;

    @FXML
    private Label lblMessage;

    @FXML
    private Text txtBreakfastDetails;

    @FXML
    private Text txtUsername;

    @FXML
    private Button btnLogout;

    @FXML
    private Button btnAdd;

    @FXML
    private TableView<MealEntry> tblBreakfast;

    @FXML
    private Button btnHome;

    @FXML
    void onAddClicked(ActionEvent event) {
    	ControllerUtils.openNewEntryPage(primaryStage);
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
	public void initialize() {
    	// show the logged in username
    	txtUsername.setText(Profile.get().getCurrentUser().getRealName());
    	
    	// set the current showing date as the todays date
    	showingDate = new Date();
    	showingDateChanged();
    	
    	// set the styles for the various UI controls
    	setButtonStyles1(btnAdd);
    	setButtonStyles1(btnHome);
    	setButtonStyles2(btnLogout);
    	setBackgroundStyle(pnlBackground);
	}

    /**
     * Update all the UI controls based on the showing date
     */
	private void showingDateChanged() {
		// show the showing date on the text control
		SimpleDateFormat df = new SimpleDateFormat("dd, MMM yyyy");
		txtShowDate.setText(df.format(showingDate));
		
		// fill each of the tables and detail text boxes with meal data for breakfast, lunch and dinner 
		int breakfastCalories = fillTable(tblBreakfast, txtBreakfastDetails, MealTime.BREAKFAST);
    	int lunchCalories = fillTable(tblLunch, txtLunchDetails, MealTime.LUNCH);
    	int dinnerCalories = fillTable(tblDinner, txtDinnerDetails, MealTime.DINNER);
    	
    	// show the pie chart the calorie intake for the showing date for each meal time
		int totalCalGoal = Profile.get().getCurrentUser().getTotalCalories();
		PieChart.Data dataBreakfast = new PieChart.Data("Breakfast", 1.0 * breakfastCalories / totalCalGoal);
		PieChart.Data dataLunch = new PieChart.Data("Lunch", 1.0 * lunchCalories / totalCalGoal);
		PieChart.Data dataDinner = new PieChart.Data("Dinner", 1.0 * dinnerCalories / totalCalGoal);
		PieChart.Data dataOther = new PieChart.Data("Other", 1 - 1.0 * (breakfastCalories + lunchCalories + dinnerCalories)/totalCalGoal);
		ObservableList<PieChart.Data> pieChartData =
                FXCollections.observableArrayList(dataBreakfast,dataLunch,dataDinner,dataOther);
		
		chrtTotal.setData(pieChartData);
		chrtTotal.setLegendVisible(false);
		
		// set the color of the "other" slice to gray
		dataOther.getNode().setStyle("-fx-pie-color: gray;-fx-opacity: 0.1;");
	}

	/**
	 * This helper function will populate the table with the correct meal time type data for 
	 * showing date and update the details text box with the time and total calories
	 * @param table
	 * @param txtMealDetails
	 * @param mtype
	 * @return
	 */
	private int fillTable(final TableView<MealEntry> table, Text txtMealDetails, MealTime mtype) {
		// remove any existing settings in the table
		table.getColumns().clear();
		
		// add 2 columns for the table (food name and total weight)
		TableColumn nameCol = new TableColumn("name");
		nameCol.prefWidthProperty().bind(table.widthProperty().multiply(0.6));
		nameCol.setResizable(false);
		nameCol.setCellValueFactory(new PropertyValueFactory<MealEntry, String>("foodName"));
		
		TableColumn countCol = new TableColumn("count");
		countCol.setCellValueFactory(new PropertyValueFactory<MealEntry, String>("totalWeight"));
		countCol.prefWidthProperty().bind(table.widthProperty().multiply(0.25));
		countCol.setResizable(false);
		countCol.setStyle("-fx-alignment: center-right;");
		
		table.getColumns().addAll(nameCol, countCol);

		// get all the meal entries for the showing date
		List<MealEntry> entries = Profile.get().getMealHistory().getEntries(showingDate);
		
		// filter the meal entries and get only the data related to the given meal time type
		List<MealEntry> filteredEntries = new ArrayList<>();
		MealEntry minTime = null; 							// also keep track of the meal entry with the minimum timestamp
		MealIntake totalIntake = new MealIntake(0, 0, 0);	// also keep track of total proteins, carbs and fat
		for(MealEntry entry: entries) {
			if (entry.getMealTime() == mtype) {
				filteredEntries.add(entry);
				if (minTime == null || minTime.getDate().after(entry.getDate())) {
					minTime = entry;
				}
				totalIntake.addProteins(entry.getProteins());
				totalIntake.addCarbs(entry.getCarbs());
				totalIntake.addFat(entry.getFat());
			}
		}
		// create an observable data list for the table
		ObservableList<MealEntry> data = FXCollections.observableArrayList(filteredEntries);
		// set data in the table
		table.setItems(data);
		//set styles for the table
		table.setStyle("-fx-table-cell-border-color: transparent;-fx-background-radius:10;");
		
		// remove the header row from the table
		table.widthProperty().addListener(new ChangeListener<Number>(){
            @Override
            public void changed(ObservableValue<? extends Number> source, Number oldWidth, Number newWidth){
                Pane header = (Pane) table.lookup("TableHeaderRow");
                if (header.isVisible()){
                    header.setMaxHeight(0);
                    header.setMinHeight(0);
                    header.setPrefHeight(0);
                    header.setVisible(false);
                }
            }
        });
		
		// update the details text box with the time and total calorie count
		SimpleDateFormat df = new SimpleDateFormat("hh:mm aa");
		if (minTime == null) {
			txtMealDetails.setText("No data\navailable");
		} else {
			txtMealDetails.setText(df.format(minTime.getDate())+"\n\n"+totalIntake.calculateCalories()+" calories");
		}
		return totalIntake.calculateCalories();
	}

	/**
	 * Change the showing date with given number of days
	 * @param d
	 */
	private void changeDate(int d) {
		Calendar cal = Calendar.getInstance();
        cal.setTime(showingDate);
        cal.add(Calendar.DATE, d);
        showingDate = cal.getTime();
        showingDateChanged();
	}
	
    @FXML
    void onPreClicked(ActionEvent event) {
    	// subtract a day from the showing day
    	changeDate(-1);
    }

    @FXML
    void onNextClicked(ActionEvent event) {
    	// add a day from teh showing day
    	changeDate(1);
    }
    
}
