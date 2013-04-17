package com.caldroid;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

/**
 * Convenient helper to work with date, JODA DateTime and String
 * 
 * @author thomasdao
 * 
 */
public class CalendarHelper {

	/**
	 * Retrieve all the dates for a given calendar month Include previous month,
	 * current month and next month.
	 * 
	 * @param month
	 * @param year
	 * @param startDayOfWeek
	 *            : calendar can start from customized date instead of Sunday
	 * @return
	 */
	public static ArrayList<DateTime> getFullWeeks(int month, int year,
			int startDayOfWeek) {
		ArrayList<DateTime> datetimeList = new ArrayList<DateTime>();

		DateTime firstDateOfMonth = new DateTime(year, month, 1, 0, 0);
		DateTime lastDateOfMonth = firstDateOfMonth.plusMonths(1).minusDays(1);

		// Add dates of first week from previous month
		int weekdayOfFirstDate = firstDateOfMonth.getDayOfWeek();

		// If weekdayOfFirstDate smaller than startDayOfWeek
		// For e.g: weekdayFirstDate is Monday, startDayOfWeek is Tuesday
		// increase the weekday of FirstDate because it's in the future
		if (weekdayOfFirstDate < startDayOfWeek) {
			weekdayOfFirstDate += 7;
		}

		while (weekdayOfFirstDate > 0) {
			DateTime dateTime = firstDateOfMonth.minusDays(weekdayOfFirstDate
					- startDayOfWeek);
			if (!dateTime.isBefore(firstDateOfMonth)) {
				break;
			}

			datetimeList.add(dateTime);
			weekdayOfFirstDate--;
		}

		// Add dates of current month
		for (int i = 0; i < lastDateOfMonth.getDayOfMonth(); i++) {
			datetimeList.add(firstDateOfMonth.plusDays(i));
		}

		// Add dates of last week from next month
		int endDayOfWeek = startDayOfWeek - 1;

		// For startDayOfWeek is Monday (1), endDayOfWeek should be Sunday (7)
		if (endDayOfWeek == 0) {
			endDayOfWeek = 7;
		}

		if (lastDateOfMonth.getDayOfWeek() != endDayOfWeek) {
			int i = 1;
			while (true) {
				DateTime nextDay = lastDateOfMonth.plusDays(i);
				datetimeList.add(nextDay);
				i++;
				if (nextDay.getDayOfWeek() == endDayOfWeek) {
					break;
				}
			}
		}

		return datetimeList;
	}

	/**
	 * Get the DateTime from Date, with hour and min is 0
	 * 
	 * @param date
	 * @return
	 */
	public static DateTime convertDateToDateTime(Date date) {
		DateTime dateTime = new DateTime(date);
		dateTime = new DateTime(dateTime.getYear(), dateTime.getMonthOfYear(),
				dateTime.getDayOfMonth(), 0, 0);
		return dateTime;
	}

	/**
	 * Get the Date from String with custom format. Default format is yyyy-MM-dd
	 * 
	 * @param dateString
	 * @param dateFormat
	 * @return
	 * @throws ParseException
	 */
	public static Date getDateFromString(String dateString, String dateFormat)
			throws ParseException {
		SimpleDateFormat formatter;
		if (dateFormat == null) {
			formatter = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
		} else {
			formatter = new SimpleDateFormat(dateFormat, Locale.ENGLISH);
		}

		return formatter.parse(dateString);
	}

	/**
	 * Get the DateTime from String with custom format. Default format is
	 * yyyy-MM-dd
	 * 
	 * @param dateString
	 * @param dateFormat
	 * @return
	 */
	public static DateTime getDateTimeFromString(String dateString,
			String dateFormat) {
		DateTimeFormatter formatter;
		if (dateFormat == null) {
			formatter = DateTimeFormat.forPattern("yyyy-MM-dd");
		} else {
			formatter = DateTimeFormat.forPattern(dateFormat);
		}
		return formatter.parseDateTime(dateString);
	}
	
	public static ArrayList<String> convertToStringList(ArrayList<DateTime> dateTimes) {
		ArrayList<String> list = new ArrayList<String>();
		DateTimeFormatter formatter = DateTimeFormat.forPattern("yyyy-MM-dd");
		for (DateTime dateTime : dateTimes) {
			list.add(formatter.print(dateTime));
		}
		return list;
	}

}
