package com.caldroid;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.joda.time.DateTime;
import org.joda.time.DateTimeConstants;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.TextView;

/**
 * Caldroid is a fragment that display calendar with dates in a month. Caldroid
 * can be used as embedded fragment, or as dialog fragment.
 * 
 * Caldroid fragment includes 4 main parts:
 * 
 * 1) Month title view: show the month and year (e.g MARCH, 2013)
 * 
 * 2) Navigation arrows: to navigate to next month or previous month
 * 
 * 3) Weekday gridview: contains only 1 row and 7 columns. To display
 * "SUN, MON, TUE, WED, THU, FRI, SAT"
 * 
 * 4) Dates gridview: contains dates within a month, and any dates in previous/
 * next month. This dates gridview is main component of this library.
 * 
 * Caldroid fragment supports setting min/max date, selecting dates in a range,
 * setting disabled dates, highlighting today. It includes convenient methods to
 * work with date and string, enable or disable the navigation arrows.
 * 
 * Caldroid code is simple and clean partly because of powerful JODA DateTime
 * library!
 * 
 * @author thomasdao
 * 
 */

@SuppressLint("DefaultLocale")
public class CaldroidFragment extends DialogFragment {
	/**
	 * To customize the selected background drawable and text color
	 */
	public static int selectedBackgroundDrawable = -1;
	public static int selectedTextColor = Color.BLACK;

	/**
	 * To customize the disabled background drawable and text color
	 */
	public static int disabledBackgroundDrawable = -1;
	public static int disabledTextColor = Color.GRAY;

	/**
	 * Caldroid view components
	 */
	private Button leftArrowButton;
	private Button rightArrowButton;
	private TextView monthTitleTextView;
	private GridView weekdayGridView;
	private GridView datesGridView;

	/**
	 * Adapter to dates gridview
	 */
	private CaldroidGridAdapter datesGridAdapter;

	/**
	 * Initial data
	 */
	protected int month = -1;
	protected int year = -1;
	protected ArrayList<DateTime> disableDates = new ArrayList<DateTime>();
	protected ArrayList<DateTime> selectedDates = new ArrayList<DateTime>();
	protected DateTime minDateTime;
	protected DateTime maxDateTime;
	protected boolean showNavigationArrows = true;

	/**
	 * For Listener
	 */
	private OnItemClickListener dateItemClickListener;
	private CaldroidListener caldroidListener;

	/**
	 * Return the adapter of the dates gridview
	 * 
	 * @return
	 */
	public CaldroidGridAdapter getDatesGridAdapter() {
		return datesGridAdapter;
	}
	
	/**
	 * Meant to be subclassed. User who wants to provide custom view,
	 * need to provide custom adapter here
	 */
	public CaldroidGridAdapter getNewDatesGridAdapter() {
		return new CaldroidGridAdapter(getActivity(), month, year,
				disableDates, selectedDates, minDateTime, maxDateTime);
	}
	
	
	/**
	 * For client to customize the date gridview
	 * 
	 * @return
	 */
	public GridView getDatesGridView() {
		return datesGridView;
	}

	/**
	 * For client to customize the weekDayGridView
	 * 
	 * @return
	 */
	public GridView getWeekdayGridView() {
		return weekdayGridView;
	}

	/**
	 * Set month and year for the calendar. This is to avoid naive
	 * implementation of manipulating month and year. All dates within same
	 * month/year give same result
	 * 
	 * @param date
	 */
	public void setMonthYearFromDate(Date date) {
		DateTime dateTime = new DateTime(date);
		month = dateTime.getMonthOfYear();
		year = dateTime.getYear();
	}

	/**
	 * Set calendar to previous month
	 */
	private void prevMonth() {
		DateTime dateTime = new DateTime(year, month, 1, 0, 0);
		dateTime = dateTime.minusMonths(1);
		month = dateTime.getMonthOfYear();
		year = dateTime.getYear();
		refreshView();
	}

	/**
	 * Set calendar to next month
	 */
	private void nextMonth() {
		DateTime dateTime = new DateTime(year, month, 1, 0, 0);
		dateTime = dateTime.plusMonths(1);
		month = dateTime.getMonthOfYear();
		year = dateTime.getYear();
		refreshView();
	}

	/**
	 * Clear all disable dates. Notice this does not refresh the calendar, need
	 * to explicitly call refreshView()
	 */
	public void clearDisableDates() {
		disableDates.clear();
	}
	
	/**
	 * Set disableDates from ArrayList of Date
	 * @param disableDateList
	 */
	public void setDisableDates(ArrayList<Date> disableDateList) {
		disableDates.clear();
		if (disableDateList == null || disableDateList.size() == 0) {
			return;
		}

		for (Date date : disableDateList) {
			DateTime dateTime = CalendarHelper.convertDateToDateTime(date);
			disableDates.add(dateTime);
		}

		if (datesGridAdapter != null) {
			datesGridAdapter.notifyDataSetChanged();
		}
	}

	/**
	 * Set disableDates from ArrayList of String. By default, the date formatter
	 * is yyyy-MM-dd. For e.g 2013-12-24
	 * 
	 * @param disableDateStrings
	 */
	public void setDisableDatesFromString(ArrayList<String> disableDateStrings) {
		setDisableDatesFromString(disableDateStrings, null);
	}

	/**
	 * Set disableDates from ArrayList of String with custom date format. For
	 * example, if the date string is 06-Jan-2013, use date format dd-MMM-yyyy.
	 * This method will refresh the calendar, it's not necessary to call
	 * refreshView()
	 * 
	 * @param disableDateStrings
	 * @param dateFormat
	 */
	public void setDisableDatesFromString(ArrayList<String> disableDateStrings,
			String dateFormat) {
		disableDates.clear();
		if (disableDateStrings == null) {
			return;
		}

		for (String dateString : disableDateStrings) {
			DateTime dateTime = CalendarHelper.getDateTimeFromString(
					dateString, dateFormat);
			disableDates.add(dateTime);
		}

		if (datesGridAdapter != null) {
			datesGridAdapter.notifyDataSetChanged();
		}
	}

	/**
	 * To clear selectedDates. This method does not refresh view, need to
	 * explicitly call refreshView()
	 */
	public void clearSelectedDates() {
		selectedDates.clear();
	}

	/**
	 * Select the dates from fromDate to toDate. By default the background color
	 * is holo_blue_light, and the text color is black. You can customize the
	 * background by changing CaldroidFragment.selectedBackgroundDrawable, and
	 * change the text color CaldroidFragment.selectedTextColor before call this
	 * method. This method does not refresh view, need to call refreshView()
	 * 
	 * @param fromDate
	 * @param toDate
	 */
	public void setSelectedDates(Date fromDate, Date toDate) {
		// Ensure fromDate is before toDate
		if (fromDate == null || toDate == null || fromDate.after(toDate)) {
			return;
		}

		selectedDates.clear();

		DateTime fromDateTime = CalendarHelper.convertDateToDateTime(fromDate);
		DateTime toDateTime = CalendarHelper.convertDateToDateTime(toDate);

		DateTime dateTime = fromDateTime;
		while (dateTime.isBefore(toDateTime)) {
			selectedDates.add(dateTime);
			dateTime = dateTime.plusDays(1);
		}
		selectedDates.add(toDateTime);
	}

	/**
	 * Convenient method to select dates from String
	 * 
	 * @param fromDateString
	 * @param toDateString
	 * @param dateFormat
	 * @throws ParseException
	 */
	public void setSelectedDateStrings(String fromDateString,
			String toDateString, String dateFormat) throws ParseException {

		Date fromDate = CalendarHelper.getDateFromString(fromDateString,
				dateFormat);
		Date toDate = CalendarHelper
				.getDateFromString(toDateString, dateFormat);
		setSelectedDates(fromDate, toDate);
	}

	/**
	 * Check if the navigation arrow is shown
	 * 
	 * @return
	 */
	public boolean isShowNavigationArrows() {
		return showNavigationArrows;
	}

	/**
	 * Show or hide the navigation arrows
	 * 
	 * @param showNavigationArrows
	 */
	public void setShowNavigationArrows(boolean showNavigationArrows) {
		this.showNavigationArrows = showNavigationArrows;
		if (showNavigationArrows) {
			leftArrowButton.setVisibility(View.VISIBLE);
			rightArrowButton.setVisibility(View.VISIBLE);
		} else {
			leftArrowButton.setVisibility(View.INVISIBLE);
			rightArrowButton.setVisibility(View.INVISIBLE);
		}
	}

	/**
	 * Set min date. This method does not refresh view
	 * 
	 * @param minDate
	 */
	public void setMinDate(Date minDate) {
		if (minDate == null) {
			minDateTime = null;
			return;
		}
		this.minDateTime = CalendarHelper.convertDateToDateTime(minDate);
	}

	/**
	 * Convenient method to set min date from String. If dateFormat is null,
	 * default format is yyyy-MM-dd
	 * 
	 * @param minDateString
	 * @param dateFormat
	 */
	public void setMinDateFromString(String minDateString, String dateFormat) {
		if (minDateString == null) {
			minDateTime = null;
			return;
		}
		this.minDateTime = CalendarHelper.getDateTimeFromString(minDateString,
				dateFormat);
	}

	/**
	 * Set max date. This method does not refresh view
	 * 
	 * @param maxDate
	 */
	public void setMaxDate(Date maxDate) {
		if (maxDate == null) {
			maxDateTime = null;
			return;
		}
		this.maxDateTime = CalendarHelper.convertDateToDateTime(maxDate);
	}

	/**
	 * Convenient method to set max date from String. If dateFormat is null,
	 * default format is yyyy-MM-dd
	 * 
	 * @param maxDateString
	 * @param dateFormat
	 */
	public void setMaxDateFromString(String maxDateString, String dateFormat) {
		if (maxDateString == null) {
			maxDateTime = null;
		}
		this.maxDateTime = CalendarHelper.getDateTimeFromString(maxDateString,
				dateFormat);
	}

	/**
	 * Set caldroid listener when user click on a date
	 * 
	 * @param caldroidListener
	 */
	public void setCaldroidListener(CaldroidListener caldroidListener) {
		this.caldroidListener = caldroidListener;
	}

	/**
	 * Callback to listener when date is valid (not disable, not outside of
	 * min/max date)
	 * 
	 * @return
	 */
	private OnItemClickListener getDateItemClickListener() {
		final ArrayList<DateTime> dateTimes = CalendarHelper.getFullWeeks(
				month, year);
		dateItemClickListener = new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				DateTime dateTime = dateTimes.get(position);

				if (caldroidListener != null) {
					if ((minDateTime != null && dateTime.isBefore(minDateTime))
							|| (maxDateTime != null && dateTime
									.isAfter(maxDateTime))
							|| (disableDates != null && disableDates
									.indexOf(dateTime) != -1)) {
						return;
					}

					caldroidListener.onSelectDate(dateTime.toDate(), view);
				}
			}

		};
		return dateItemClickListener;
	}

	/**
	 * Refresh view when parameter changes. You should always change all
	 * parameters first, then call this method.
	 */
	public void refreshView() {
		monthTitleTextView.setText(new DateTime(year, month, 1, 0, 0)
				.monthOfYear().getAsText().toUpperCase()
				+ " " + year);
		datesGridAdapter = getNewDatesGridAdapter();

		datesGridView.setAdapter(datesGridAdapter);
		datesGridView.setOnItemClickListener(getDateItemClickListener());
	}

	/**
	 * Retrieve initial arguments to the fragment Data can include: month, year,
	 * dialogTitle, showNavigationArrows,(String) disableDates, selectedDates,
	 * minDate, maxDate
	 */
	private void retrieveInitialArgs() {
		// Get arguments
		Bundle args = getArguments();
		if (args != null) {
			// Get month, year
			month = args.getInt("month", -1);
			year = args.getInt("year", -1);
			String dialogTitle = args.getString("dialogTitle");
			if (dialogTitle != null) {
				getDialog().setTitle(dialogTitle);
			}

			// Should show arrow
			showNavigationArrows = args
					.getBoolean("showNavigationArrows", true);

			DateTimeFormatter formatter = DateTimeFormat
					.forPattern("yyyy-MM-dd");

			// Get disable dates
			ArrayList<String> disableDateStrings = args
					.getStringArrayList("disableDates");
			if (disableDateStrings != null && disableDateStrings.size() > 0) {
				for (String dateString : disableDateStrings) {
					DateTime dt = formatter.parseDateTime(dateString);
					disableDates.add(dt);
				}
			}

			// Get selected dates
			ArrayList<String> selectedDateStrings = args
					.getStringArrayList("selectedDates");
			if (selectedDateStrings != null && selectedDateStrings.size() > 0) {
				for (String dateString : selectedDateStrings) {
					DateTime dt = formatter.parseDateTime(dateString);
					selectedDates.add(dt);
				}
			}

			// Get min date and max date
			String minDateTimeString = args.getString("minDate");
			if (minDateTimeString != null) {
				minDateTime = CalendarHelper.getDateTimeFromString(
						minDateTimeString, null);
			}

			String maxDateTimeString = args.getString("maxDate");
			if (maxDateTimeString != null) {
				maxDateTime = CalendarHelper.getDateTimeFromString(
						maxDateTimeString, null);
			}

		}
		if (month == -1 || year == -1) {
			DateTime dateTime = new DateTime();
			month = dateTime.getMonthOfYear();
			year = dateTime.getYear();
		}
	}

	/**
	 * To support faster init
	 * 
	 * @param dialogTitle
	 * @param month
	 * @param year
	 * @return
	 */
	public static CaldroidFragment newInstance(String dialogTitle, int month,
			int year) {
		CaldroidFragment f = new CaldroidFragment();

		// Supply num input as an argument.
		Bundle args = new Bundle();
		args.putString("dialogTitle", dialogTitle);
		args.putInt("month", month);
		args.putInt("year", year);

		f.setArguments(args);

		return f;
	}

	/**
	 * Setup view
	 */
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		retrieveInitialArgs();

		View view = inflater.inflate(R.layout.calendar_view, container, false);

		// For the monthTitleTextView
		monthTitleTextView = (TextView) view
				.findViewById(R.id.calendar_month_year_textview);

		// For the left arrow button
		leftArrowButton = (Button) view.findViewById(R.id.calendar_left_arrow);
		rightArrowButton = (Button) view
				.findViewById(R.id.calendar_right_arrow);

		leftArrowButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				prevMonth();
				if (caldroidListener != null) {
					caldroidListener.onChangeMonth(month, year);
				}
			}
		});

		rightArrowButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				nextMonth();
				if (caldroidListener != null) {
					caldroidListener.onChangeMonth(month, year);
				}
			}
		});

		setShowNavigationArrows(showNavigationArrows);

		// For the weekday gridview
		weekdayGridView = (GridView) view.findViewById(R.id.weekday_gridview);
		WeekdayArrayAdapter weekdaysAdapter = new WeekdayArrayAdapter(
				getActivity(), android.R.layout.simple_list_item_1,
				getDaysOfWeek());
		weekdayGridView.setAdapter(weekdaysAdapter);

		// For the dates gridview
		datesGridView = (GridView) view.findViewById(R.id.calendar_gridview);
		refreshView();

		return view;
	}

	/**
	 * To display the week day title
	 * 
	 * @return "SUN, MON, TUE, WED, THU, FRI, SAT"
	 */
	private ArrayList<String> getDaysOfWeek() {
		ArrayList<String> list = new ArrayList<String>();

		// 17 Feb 2013 is Sunday
		DateTime sunday = new DateTime(2013, 2, 17, 0, 0);
		DateTime nextDay = sunday;
		while (true) {
			list.add(nextDay.dayOfWeek().getAsShortText().toUpperCase());
			nextDay = nextDay.plusDays(1);
			if (nextDay.getDayOfWeek() == DateTimeConstants.SUNDAY) {
				break;
			}
		}
		return list;
	}

	/**
	 * Customize the weekday gridview
	 */
	private class WeekdayArrayAdapter extends ArrayAdapter<String> {

		public WeekdayArrayAdapter(Context context, int textViewResourceId,
				List<String> objects) {
			super(context, textViewResourceId, objects);
		}

		// To prevent cell highlighted when clicked
		@Override
		public boolean areAllItemsEnabled() {
			return false;
		}

		@Override
		public boolean isEnabled(int position) {
			return false;
		}

		// Set color to gray and text size to 12sp
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			// To customize text size and color
			TextView textView = (TextView) super.getView(position, convertView,
					parent);
			textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 12);
			textView.setTextColor(getResources()
					.getColor(R.color.caldroid_gray));
			textView.setGravity(Gravity.CENTER);
			return textView;
		}

	}

}
