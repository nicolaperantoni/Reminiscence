package it.unitn.vanguard.reminiscence.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.preference.PreferenceManager;

public final class FinalFunctionsUtilities {

	public static final String EMAIL_REGEX = "^[\\w-_\\.+]*[\\w-_\\.]\\@([\\w]+\\.)+[\\w]+[\\w]$";

	/**
	 * Validates an email address
	 * 
	 * @param email
	 *            an email address to check
	 * @return true if email address is valid otherwise return false.
	 */
	public static boolean isValidEmailAddress(String email) {
		return email.matches(EMAIL_REGEX);
	}

	// Leap year
	public static boolean is_leap_year(int year) {
		boolean result;

		if ((year % 4) != 0)
			result = false;
		else if ((year % 400) == 0)
			result = true;
		else if ((year % 100) == 0)
			result = false;
		else
			result = true;

		return result;
	}

	// Date validation
	public static int valiDate(int day, int month, int year) {
		int month_length[] = { 31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31 };

		if (is_leap_year(year))
			month_length[1] = 29;
		if (day > month_length[month])
			day = 1;
		if (day < 1)
			day = month_length[month];

		return day;
	}

	// control on current date
	public static boolean isOverCurrentDate(int day, int month, int year,
			int maxDay, int maxMonth, int maxYear) {
		if (year > maxYear)
			return true;
		else if (month > maxMonth && year == maxYear)
			return true;
		else if (day > maxDay && month == maxMonth && year == maxYear)
			return true;
		else
			return false;
	}

	public static boolean isLoggedIn(Context context) {
		String token = getSharedPreferences("token", context);
		return !token.trim().equals("");
	}

	public static void setSharedPreferences(String key, String value,
			Context context) {
		SharedPreferences prefs = PreferenceManager
				.getDefaultSharedPreferences(context);
		SharedPreferences.Editor editor = prefs.edit();
		editor.putString(key, value);
		editor.commit();
	}

	public static String getSharedPreferences(String key, Context context) {
		SharedPreferences prefs = PreferenceManager
				.getDefaultSharedPreferences(context);
		return prefs.getString(key, "");
	}

	/**
	 * Controlla se e' presente la connessione ad internet, sia mobile che wi-fi
	 * 
	 * @param context
	 *            il context dell'applicazione
	 * @return true se il dispositivo e' connesso ad internet (wi-fi o mobile),
	 *         false altrimenti
	 */
	public static boolean isDeviceConnected(Context context) {

		ConnectivityManager connectivityManager = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);

		NetworkInfo mobileDataInfo = connectivityManager
				.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
		NetworkInfo wifiInfo = connectivityManager
				.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
		
		if (mobileDataInfo != null && mobileDataInfo.getState()==NetworkInfo.State.CONNECTED)
			return true;
		return wifiInfo.getState() == NetworkInfo.State.CONNECTED;
	}
}