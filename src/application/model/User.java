package application.model;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Model class to manage user data
 */
public class User {

	private String username;
	private String realName;
	private String password;
	private int dobYear;
	private int dobMonth;
	private int dobDay;

	private int totalCalories;
	private int totalProteins;
	private int totalCarbs;
	private int totalFat;

	// list of users will be kept inside this class
	private static List<User> users = new ArrayList<>();

	private User(String username, String password, String realName, int dobYear, int dobMonth, int dobDay
			, int totalCalories, int totalProteins, int totalCarbs, int totalFat) {
		this.username = username;
		this.password = password;
		this.realName = realName;
		this.dobYear = dobYear;
		this.dobMonth = dobMonth;
		this.dobDay = dobDay;
		this.totalCalories = totalCalories;
		this.totalProteins = totalProteins;
		this.totalCarbs = totalCarbs;
		this.totalFat = totalFat;
	}
	
	public int getTotalCalories() {
		return totalCalories;
	}

	public void setTotalCalores(int totalCalories) {
		this.totalCalories = totalCalories;
	}

	public int getTotalProteins() {
		return totalProteins;
	}

	public void setTotalProteins(int totalProteins) {
		this.totalProteins = totalProteins;
	}

	public int getTotalCarbs() {
		return totalCarbs;
	}

	public void setTotalCarbs(int totalCarbs) {
		this.totalCarbs = totalCarbs;
	}

	public int getTotalFat() {
		return totalFat;
	}

	public void setTotalFat(int totalFat) {
		this.totalFat = totalFat;
	}

	/**
	 * Get the read name of the user
	 * 
	 * @return
	 */
	public String getRealName() {
		return realName;
	}

	/**
	 * Update the real name of the user
	 * 
	 * @param realName
	 */
	public void setRealName(String realName) {
		this.realName = realName;
	}

	/**
	 * Get the username of the user
	 * 
	 * @return
	 */
	public String getUsername() {
		return username;
	}

	/**
	 * Update the username of the user
	 * 
	 * @param username
	 */
	public void setUsername(String username) {
		this.username = username;
	}

	/**
	 * Get the password associated with the user
	 * 
	 * @return
	 */
	public String getPassword() {
		return password;
	}

	/**
	 * Update the password associated with the user
	 * 
	 * @param password
	 */
	public void setPassword(String password) {
		this.password = password;
	}

	public int getDobYear() {
		return dobYear;
	}

	public void setDobYear(int dobYear) {
		this.dobYear = dobYear;
	}

	public int getDobMonth() {
		return dobMonth;
	}

	public void setDobMonth(int dobMonth) {
		this.dobMonth = dobMonth;
	}

	public int getDobDay() {
		return dobDay;
	}

	public void setDobDay(int dobDay) {
		this.dobDay = dobDay;
	}

	/**
	 * Check to see if the given username and password matches with any of the
	 * users. if so return that user object
	 * 
	 * @param username
	 * @param password
	 * @return returns null if no user is matched
	 */
	public static User validate(String username, String password) {
		for (User user : users) {
			if (user.getUsername().equals(username) && user.getPassword().equals(password)) {
				return user;
			}
		}
		return null;
	}

	@Override
	public int hashCode() {
		return Objects.hash(username);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		User other = (User) obj;
		return Objects.equals(username, other.username);
	}

	/**
	 * Load a list of user data from a file
	 * 
	 * @param filename
	 */
	public static void load(String filename) {
		try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
			String line;
			while ((line = br.readLine()) != null) {
				String[] data = line.trim().split(",");
				String username = data[0].trim();
				String password = data[1].trim();
				String name = data[2].trim();
				int year = Integer.valueOf(data[3].trim());
				int month = Integer.valueOf(data[4].trim());
				int day = Integer.valueOf(data[5].trim());

				int totCal = Integer.valueOf(data[6].trim());
				int totP = Integer.valueOf(data[7].trim());
				int totC = Integer.valueOf(data[8].trim());
				int totF = Integer.valueOf(data[9].trim());
				
				User user = new User(username, password, name, year, month, day, totCal, totP, totC, totF);
				users.add(user);
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static void save(String filename) {
		try {
	    	FileWriter writer = new FileWriter(filename);
		    for(User user: users) {
		    	writer.append(user.getUsername()+",");
		    	writer.append(user.getPassword()+",");
		    	writer.append(user.getRealName()+",");
		    	writer.append(user.getDobYear()+",");
		    	writer.append(user.getDobMonth()+",");
		    	writer.append(user.getDobDay()+",");
		    	writer.append(user.getTotalCalories()+",");
		    	writer.append(user.getTotalProteins()+",");
		    	writer.append(user.getTotalCarbs()+",");
		    	writer.append(user.getTotalFat()+"\n");
		    }
			writer.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
