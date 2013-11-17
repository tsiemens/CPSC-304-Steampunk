package org.steampunk.mist.util;

import java.util.Calendar;

public class MistTextFormatter {

	private static final String NULL_STRING = "(null)";
	
	public static String formatDateString(Calendar cal) {
		if (cal == null) return NULL_STRING;
		return ""+(cal.get(Calendar.MONTH)+1)+"/"+cal.get(Calendar.DAY_OF_MONTH)+"/"+cal.get(Calendar.YEAR);
	}
	
	public static String formatDateTimeString(Calendar cal) {
		if (cal == null) return NULL_STRING;
		return formatDateString(cal)+" at "+cal.get(Calendar.HOUR_OF_DAY)+":"+cal.get(Calendar.MINUTE);
	}
}
