package it.unitn.vanguard.reminiscence.utils;

import java.util.LinkedList;
import java.util.Locale;

import android.app.NotificationManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.preference.PreferenceManager;
import android.support.v4.app.NotificationCompat;

public final class FinalFunctionsUtilities {

	public static final String EMAIL_REGEX = "^[\\w-_\\.+]*[\\w-_\\.]\\@([\\w]+\\.)+[\\w]+[\\w]$";

	public static LinkedList<Story> stories = new LinkedList<Story>();

	/**
	 * Validates an email address
	 * 
	 * @param email
	 *            an email address to check
	 * @return true if email address is valid otherwise return false.
	 */
	public static boolean isValidEmailAddress(String email) {
		return email.matches(EMAIL_REGEX) && !email.trim().equals("")
				&& !email.startsWith(" ") && !email.endsWith(" ");
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
		String retVal = "";
		SharedPreferences prefs = PreferenceManager
				.getDefaultSharedPreferences(context);

		if (key.equals(Constants.DAY_KEY)) {
			retVal = "1";
		}
		if (key.equals(Constants.MONTH_KEY)) {
			retVal = "Gennaio";
		}
		if (key.equals(Constants.YEAR_KEY)) {
			retVal = "1940";
		}
		if (key.equals(Constants.NAME_KEY)) {
			retVal = "";
		}
		if (key.equals(Constants.SURNAME_KEY)) {
			retVal = "";
		}
		if (key.equals(Constants.MAIL_KEY)) {
			retVal = "";
		}
		if (key.equals(Constants.PASSWORD_KEY)) {
			retVal = "";
		}
		if (key.equals(Constants.LOUGO_DI_NASCITA_PREFERENCES_KEY)) {
			retVal = "Trento";
		}
		if (key.equals("language")) {
			Configuration config = context.getResources().getConfiguration();
			retVal = config.locale.getLanguage();
		}
		String r = prefs.getString(key, retVal);
		return r;
	}
	
	//pulisce le shared preferences
	public static void clearSharedPreferences(Context context)
	{
	     SharedPreferences prefs = PreferenceManager
					.getDefaultSharedPreferences(context);
	     SharedPreferences.Editor editor = prefs.edit();
	     editor.clear();
	     editor.commit();
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

		if (mobileDataInfo != null
				&& mobileDataInfo.getState() == NetworkInfo.State.CONNECTED)
			return true;
		return wifiInfo.getState() == NetworkInfo.State.CONNECTED;
	}

	public static boolean switchLanguage(Locale locale, Context context) {

		// Ottengo la configurazione attuale e controllo se ������ uguale a quella
		// delle
		// SharedPreferences. Se non sono uguali cambio la lingua altrimenti
		// non faccio nulla..

		Configuration config = context.getResources().getConfiguration();
		Locale oldLanguage = config.locale;

		if (!oldLanguage.getLanguage().equals(locale.getLanguage())) {
			FinalFunctionsUtilities.setSharedPreferences("language",
					locale.getLanguage(), context);
			config.locale = locale;
			context.getResources().updateConfiguration(config,
					context.getResources().getDisplayMetrics());
			return true;
		}
		return false;
	}

}