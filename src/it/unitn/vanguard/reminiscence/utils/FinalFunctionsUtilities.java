package it.unitn.vanguard.reminiscence.utils;

public final class FinalFunctionsUtilities {

	public static final String EMAIL_REGEX = "^[\\w-_\\.+]*[\\w-_\\.]\\@([\\w]+\\.)+[\\w]+[\\w]$";

	/**
	 * Validates an email address
	 * 
	 * @param email an email address to check
	 * @return true if email address is valid otherwise return false.
	 */
	public static boolean isValidEmailAddress(String email) {
		return email.matches(EMAIL_REGEX);
	}
}