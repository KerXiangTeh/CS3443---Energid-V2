package application.model;

/**
 * Profile class will contain all the data related to the user. Whenever a page requires 
 * data of the user, they can refer to this class
 *
 */
public class Profile {
	User currentUser;
	MealHistory mealHistory;
	private static Profile currentProfile = null;
	
	private Profile(User user) {
		setCurrentUser(user);
	}
	
	public User getCurrentUser() {
		return currentUser;
	}
	
	public MealHistory getMealHistory() {
		return mealHistory;
	}
	
	private void setCurrentUser(User currentUser) {
		this.currentUser = currentUser;
		this.mealHistory = MealHistory.getUserMealHistory(currentUser.getUsername());
	}
	
	/**
	 * Set the user to the profile. Hence whenever we ask for the profile, we would get 
	 * a profile specific for this specified user
	 * @param user
	 */
	public static void setUser(User user) {
		if (currentProfile == null || !currentProfile.getCurrentUser().equals(user)) {
			currentProfile = new Profile(user);
		}
	}
	
	/**
	 * Clear the profile. Useful when login out
	 */
	public static void clearProfile() {
		currentProfile = null;
	}
	
	/**
	 * Retrieve the currently logged in users profile
	 * @return
	 */
	public static Profile get() {
		return currentProfile;
	}
}
