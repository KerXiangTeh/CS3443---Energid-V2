package application.model;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * This will contain the model for an entry from the user for a meal  
 *
 */
public class MealEntry extends MealIntake {

	// we only concern with breakfast, lunch and dinner and meal times
	public enum MealTime {
		BREAKFAST,
		LUNCH,
		DINNER
	}
	String user;
	Date date;
	String foodName;

	MealTime mealTime;
	public MealEntry(String user, Date date, String foodName, int proteins, int carbs, int fat, MealTime mealTime) {
		super(proteins, carbs, fat);
		this.user = user;
		this.date = date;
		this.foodName = foodName;
		this.mealTime = mealTime;
	}
	public String getUser() {
		return user;
	}
	public void setUser(String user) {
		this.user = user;
	}
	public Date getDate() {
		return date;
	}
	public void setDate(Date date) {
		this.date = date;
	}
	public String getFoodName() {
		return foodName;
	}
	public void setFoodName(String name) {
		this.foodName = name;
	}
	public MealTime getMealTime() {
		return mealTime;
	}
	public void setMealTime(MealTime mealTime) {
		this.mealTime = mealTime;
	}
	
	@Override
	public String toString() {
		return "MealEntry [user=" + user + ", date=" + date + ", foodName=" + foodName + ", proteins=" + proteins
				+ ", carbs=" + carbs + ", fat=" + fat + ", mealTime=" + mealTime + "]";
	}
	
	/**
	 * Helper function to check if this meal entry is within the given date range
	 * @param fromDate
	 * @param toDate
	 * @return
	 */
	public boolean isWithinDays(Date fromDate, Date toDate) {
		Date d = Utils.convertToStartofDate(date);
		Date from = Utils.convertToStartofDate(fromDate);
		Date to = Utils.convertToStartofDate(fromDate);
		return !(d.before(from) || d.after(to));
	}
	
	/**
	 * Helper function to check if this meal entry is on the given date
	 * @param onDate
	 * @return
	 */
	public boolean isOnDay(Date onDate) {
		Date d = Utils.convertToStartofDate(date);
		Date on = Utils.convertToStartofDate(onDate);
		return d.equals(on);
	}
	
}
