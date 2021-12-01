package application.model;

/**
 * Class to contain meal intake data
 *
 */
public class MealIntake {
	public static final int PROTEIN_CALORY = 4;
	public static final int CARB_CALORY = 4;
	public static final int FAT_CALORY = 9;
	int proteins;
	int carbs;
	int fat;
	public MealIntake(int proteins, int carbs, int fat) {
		this.proteins = proteins;
		this.carbs = carbs;
		this.fat = fat;
	}
	public int getProteins() {
		return proteins;
	}
	public void setProteins(int proteins) {
		this.proteins = proteins;
	}
	public int getCarbs() {
		return carbs;
	}
	public void setCarbs(int carbs) {
		this.carbs = carbs;
	}
	public int getFat() {
		return fat;
	}
	public void setFat(int fat) {
		this.fat = fat;
	}
	
	public void addProteins(int proteins) {
		this.proteins += proteins;
	}
	public void addCarbs(int carbs) {
		this.carbs += carbs;
	}
	public void addFat(int fat) {
		this.fat += fat;
	}
	
	/**
	 * Create a string label of the total weight of the food 
	 * (useful for the daily stats page where we show weight)
	 * @return
	 */
	public String getTotalWeight() {
		return (getProteins()+getCarbs()+getFat()) + " g";
	}
	
	/**
	 * Calculate the number of calories represented by the meal counts
	 * @return
	 */
	public int calculateCalories() {
		return carbs * CARB_CALORY + proteins * PROTEIN_CALORY + fat * FAT_CALORY;
	}
	
	@Override
	public String toString() {
		return "MealIntake [proteins=" + proteins + ", carbs=" + carbs + ", fat=" + fat + "]";
	}

}
