package application.model;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import application.model.MealEntry.MealTime;

/**
 * This class will manage a users meal history
 *
 */
public class MealHistory {
	String user;
	List<MealEntry> mealEntries;
	DateFormat df;
	
	public static final String[] DAYS = new String[]{"Sunday", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday"};
	
	
	private MealHistory(String user) {
		this.user = user;
		mealEntries = new ArrayList<>();
		df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.S");
	}
	
	/**
	 * Load users meal history from the data file
	 * @param filename
	 */
	private void load(String filename) {
	    try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
	      String line;
	      mealEntries.clear();
	      while ((line = br.readLine()) != null) {
	        String[] data = line.trim().split(",");
	        String user= data[0].trim();
	        Date date = df.parse(data[1].trim());
	        String foodName= data[2].trim();
	        if (user.equals(this.user)) {
		        int proteins = Integer.valueOf(data[3].trim());
		        int carbs = Integer.valueOf(data[4].trim());
		        int fat= Integer.valueOf(data[5].trim());
		        MealTime mealTime = MealTime.valueOf(data[6].trim());
		        MealEntry entry= new MealEntry(user, date, foodName, proteins, carbs, fat, mealTime);
		        mealEntries.add(entry);
	        }
	      }
	    } catch (FileNotFoundException e) {
	      e.printStackTrace();
	    } catch (IOException e) {
	      e.printStackTrace();
	    } catch (ParseException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Save the users meal history to the data file
	 * @param filename
	 */
	private void save(String filename) {
	    List<MealEntry> entries = new ArrayList<>();
	    try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
	      String line;
	      while ((line = br.readLine()) != null) {
	        String[] data = line.trim().split(",");
	        String user= data[0].trim();
	        Date date = df.parse(data[1].trim());
	        String foodName= data[2].trim();
	        if (!user.equals(this.user)) {
		        int proteins = Integer.valueOf(data[3].trim());
		        int carbs = Integer.valueOf(data[4].trim());
		        int fat= Integer.valueOf(data[5].trim());
		        MealTime mealTime = MealTime.valueOf(data[6].trim());
		        MealEntry entry= new MealEntry(user, date, foodName, proteins, carbs, fat, mealTime);
		        entries.add(entry);
	        }
	      }
	    } catch (FileNotFoundException e) {
	      e.printStackTrace();
	    } catch (IOException e) {
	      e.printStackTrace();
	    } catch (ParseException e) {
			e.printStackTrace();
		}
	    entries.addAll(mealEntries);
	    try {
	    	FileWriter writer = new FileWriter(filename);
		    for(MealEntry entry: entries) {
		    	writer.append(entry.getUser()+",");
		    	writer.append(df.format(entry.getDate())+",");
		    	writer.append(entry.getFoodName()+",");
		    	writer.append(entry.getProteins()+",");
		    	writer.append(entry.getCarbs()+",");
		    	writer.append(entry.getFat()+",");
		    	writer.append(entry.getMealTime().toString()+"\n");
		    }
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	    
	}
	
	/**
	 * Add a meal entry to users history
	 * @param entry
	 */
	public void add(MealEntry entry) {
		mealEntries.add(entry);
		// we will also update the file
		save("data/meal_history.csv");
	}
	
	/**
	 * Get a given users meal history
	 * @param user
	 * @return
	 */
	public static MealHistory getUserMealHistory(String user) {
		MealHistory mealHistory = new MealHistory(user);
		mealHistory.load("data/meal_history.csv");
		return mealHistory;
	}
	
	/**
	 * Return a list of meal entries during the given time period
	 * @param fromDate
	 * @param toDate
	 * @return
	 */
	public List<MealEntry> getEntries(Date fromDate, Date toDate){
		List<MealEntry> result = new ArrayList<>();
		for(MealEntry entry: mealEntries) {
			if (entry.isWithinDays(fromDate, toDate)){
				result.add(entry);
			}
		}
		return result;
	}
	
	/**
	 * Return a list of meal entries for the given date
	 * @param onDate
	 * @return
	 */
	public List<MealEntry> getEntries(Date onDate){
		List<MealEntry> result = new ArrayList<>();
		for(MealEntry entry: mealEntries) {
			if (entry.isOnDay(onDate)){
				result.add(entry);
			}
		}
		return result;
	}
	
	/**
	 * Return a summarised meal statistics for today
	 * @return
	 */
	public MealIntake getTodayStats(){
		MealIntake result = new MealIntake(0,0,0);
		List<MealEntry> entries = getEntries(new Date());
    	for(MealEntry entry: entries) {
    		result.addProteins(entry.getProteins());
    		result.addCarbs(entry.getCarbs());
    		result.addFat(entry.getFat());
    	}
		return result;
	}
	
	/**
	 * Return meal statistics for the given week
	 * @param weekNo
	 * @return
	 */
	public Map<String, MealIntake> getWeekStats(int weekNo){
		Map<String, MealIntake> result = new HashMap<>();
		// we first have to identify what is the date range that corresponds to the given weekNo
		Date today = new Date();
		Calendar cal = Calendar.getInstance();
	    cal.setTime(today);
	    int towardStartDay = cal.get(Calendar.DAY_OF_WEEK) - cal.getFirstDayOfWeek();
	    int thisWeekNo = cal.get(Calendar.WEEK_OF_YEAR);
	    int changeOfDays = (weekNo - thisWeekNo) * 7;
	    cal.add(Calendar.DATE, changeOfDays - towardStartDay); // this will now contain the first day of that given weekNo
	    for(int i = 0; i < DAYS.length; i++) {
	    	Date d = cal.getTime();
	    	// we get the meal entries for the given date
	    	List<MealEntry> entries = getEntries(d);
	    	MealIntake intake = new MealIntake(0, 0, 0);
	    	// summarise all the meal data
	    	for(MealEntry entry: entries) {
	    		intake.addProteins(entry.getProteins());
	    		intake.addCarbs(entry.getCarbs());
	    		intake.addFat(entry.getFat());
	    	}
	    	result.put(DAYS[i], intake);
	    	// increment to the next date
		    cal.add(Calendar.DATE, 1);
	    }
		return result;
	}
	
	/**
	 * Get the stats for the current week
	 * @return
	 */
	public Map<String, MealIntake> getCurrentWeekStats(){
		int weekNo = getCurrentWeek();
		return getWeekStats(weekNo);
	}

	/**
	 * get the current weekNo
	 * @return
	 */
	public int getCurrentWeek() {
		Date today = new Date();
		Calendar cal = Calendar.getInstance();
	    cal.setTime(today);
	    int weekNo = cal.get(Calendar.WEEK_OF_YEAR);
		return weekNo;
	}
}
