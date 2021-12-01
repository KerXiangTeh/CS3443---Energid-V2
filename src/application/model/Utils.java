package application.model;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Utils {
	
	/**
	 * This helper function will strip off the time portions of the date
	 * @param d
	 * @return
	 */
	public static Date convertToStartofDate(Date d) {
		DateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
		try {
			return formatter.parse(formatter.format(d));
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * Helper function to get a valid integer using a string
	 * @param s
	 * @return
	 */
	public static double getValue(String s) {
		try {
			double v = Double.parseDouble(s);
			if (v < 0)
				return 0;
			return v;
		} catch (NumberFormatException e) {
			return 0;
		}
	}
}
