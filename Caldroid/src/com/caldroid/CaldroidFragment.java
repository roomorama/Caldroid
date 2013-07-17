package com.caldroid;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import org.joda.time.DateTime;
import org.joda.time.DateTimeConstants;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.GridView;
import android.widget.TextView;

import com.antonyt.infiniteviewpager.InfinitePagerAdapter;
import com.antonyt.infiniteviewpager.InfiniteViewPager;

/**
 * Caldroid is a fragment that display calendar with dates in a month. Caldroid
 * can be used as embedded fragment, or as dialog fragment. <br/>
 * <br/>
 * Caldroid fragment includes 4 main parts:<br/>
 * <br/>
 * 1) Month title view: show the month and year (e.g MARCH, 2013) <br/>
 * <br/>
 * 2) Navigation arrows: to navigate to next month or previous month <br/>
 * <br/>
 * 3) Weekday gridview: contains only 1 row and 7 columns. To display
 * "SUN, MON, TUE, WED, THU, FRI, SAT" <br/>
 * <br/>
 * 4) An infinite view pager that allow user to swipe left/right to change
 * month. This library is taken from
 * https://github.com/antonyt/InfiniteViewPager <br/>
 * <br/>
 * This infinite view pager recycles 4 fragment, each fragment contains a grid
 * view with 7 columns to display the dates in month. Whenever user swipes
 * different screen, the date grid views are updated. <br/>
 * <br/>
 * Caldroid fragment supports setting min/max date, selecting dates in a range,
 * setting disabled dates, highlighting today. It includes convenient methods to
 * work with date and string, enable or disable the navigation arrows. User can
 * also swipe left/right to change months.<br/>
 * <br/>
 * Caldroid code is simple and clean partly because of powerful JODA DateTime
 * library!
 * 
 * @author thomasdao
 * 
 */

@SuppressLint("DefaultLocale")
public class CaldroidFragment extends DialogFragment {
	public String TAG = "CaldroidFragment";
	/**
	 * To customize the selected background drawable and text color
	 */
	public static int selectedBackgroundDrawable = -1;
	public static int selectedTextColor = Color.BLACK;

	public final static int NUMBER_OF_PAGES = 4;

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
	private InfiniteViewPager dateViewPager;
	private DatePageChangeListener pageChangeListener;
	private ArrayList<DateGridFragment> fragments;

	/**
	 * Initial params key
	 */
	public final static String DIALOG_TITLE = "dialogTitle";
	public final static String MONTH = "month";
	public final static String YEAR = "year";
	public final static String SHOW_NAVIGATION_ARROWS = "showNavigationArrows";
	public final static String DISABLE_DATES = "disableDates";
	public final static String SELECTED_DATES = "selectedDates";
	public final static String MIN_DATE = "minDate";
	public final static String MAX_DATE = "maxDate";
	public final static String ENABLE_SWIPE = "enableSwipe";
	public final static String START_DAY_OF_WEEK = "startDayOfWeek";
	public final static String FIT_ALL_MONTHS = "fitAllMonths";

	/**
	 * For internal use
	 */
	public final static String _MIN_DATE_TIME = "_minDateTime";
	public final static String _MAX_DATE_TIME = "_maxDateTime";
	public final static String _BACKGROUND_FOR_DATETIME_MAP = "_backgroundForDateTimeMap";
	public final static String _TEXT_COLOR_FOR_DATETIME_MAP = "_textColorForDateTimeMap";

	/**
	 * Initial data
	 */
	protected String dialogTitle;
	protected int month = -1;
	protected int year = -1;
	protected ArrayList<DateTime> disableDates = new ArrayList<DateTime>();
	protected ArrayList<DateTime> selectedDates = new ArrayList<DateTime>();	
	protected DateTime minDateTime;
	protected DateTime maxDateTime;
	protected ArrayList<DateTime> dateInMonthsList;

	/**
	 * caldroidData belongs to Caldroid
	 */
	protected HashMap<String, Object> caldroidData = new HashMap<String, Object>();

	/**
	 * extraData belongs to client
	 */
	protected HashMap<String, Object> extraData = new HashMap<String, Object>();

	/**
	 * backgroundForDateMap holds background resource for each date
	 */
	protected HashMap<DateTime, Integer> backgroundForDateTimeMap = new HashMap<DateTime, Integer>();

	/**
	 * textColorForDateMap holds color for text for each date
	 */
	protected HashMap<DateTime, Integer> textColorForDateTimeMap = new HashMap<DateTime, Integer>();;

	/**
	 * First column of calendar is Sunday
	 */
	protected int startDayOfWeek = DateTimeConstants.SUNDAY;

	/**
	 * A calendar height is not fixed, it may have 5 or 6 rows. Set fitAllMonths
	 * to true so that the calendar will always have 6 rows
	 */
	private boolean fitAllMonths = true;

	/**
	 * datePagerAdapters hold 4 adapters, meant to be reused
	 */
	protected ArrayList<CaldroidGridAdapter> datePagerAdapters = new ArrayList<CaldroidGridAdapter>();

	/**
	 * To control the navigation
	 */
	protected boolean enableSwipe = true;
	protected boolean showNavigationArrows = true;

	/**
	 * dateItemClickListener is fired when user click on the date cell
	 */
	private OnItemClickListener dateItemClickListener;

	/**
	 * caldroidListener inform library client of the event happens inside
	 * Caldroid
	 */
	private CaldroidListener caldroidListener;

	/**
	 * Meant to be subclassed. User who wants to provide custom view, need to
	 * provide custom adapter here
	 */
	public CaldroidGridAdapter getNewDatesGridAdapter(int month, int year) {
		return new CaldroidGridAdapter(getActivity(), month, year,
				getCaldroidData(), extraData);
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
	 * To let user customize the navigation buttons
	 */
	public Button getLeftArrowButton() {
		return leftArrowButton;
	}

	public Button getRightArrowButton() {
		return rightArrowButton;
	}

	/**
	 * To let client customize month title textview
	 */
	public TextView getMonthTitleTextView() {
		return monthTitleTextView;
	}

	public void setMonthTitleTextView(TextView monthTitleTextView) {
		this.monthTitleTextView = monthTitleTextView;
	}

	/**
	 * Get 4 adapters of the date grid views. Useful to set custom data and
	 * refresh date grid view
	 * 
	 * @return
	 */
	public ArrayList<CaldroidGridAdapter> getDatePagerAdapters() {
		return datePagerAdapters;
	}

	/**
	 * caldroidData return data belong to Caldroid
	 * 
	 * @return
	 */
	public HashMap<String, Object> getCaldroidData() {
		caldroidData.clear();
		caldroidData.put(DISABLE_DATES, disableDates);
		caldroidData.put(SELECTED_DATES, selectedDates);
		caldroidData.put(_MIN_DATE_TIME, minDateTime);
		caldroidData.put(_MAX_DATE_TIME, maxDateTime);
		caldroidData.put(START_DAY_OF_WEEK, Integer.valueOf(startDayOfWeek));
		
		// For internal use
		caldroidData.put(_BACKGROUND_FOR_DATETIME_MAP, backgroundForDateTimeMap);
		caldroidData.put(_TEXT_COLOR_FOR_DATETIME_MAP, textColorForDateTimeMap);
		
		return caldroidData;
	}

	/**
	 * Extra data is data belong to Client
	 * 
	 * @return
	 */
	public HashMap<String, Object> getExtraData() {
		return extraData;
	}

	/**
	 * Client can set custom data in this HashMap
	 * 
	 * @param extraData
	 */
	public void setExtraData(HashMap<String, Object> extraData) {
		this.extraData = extraData;
	}

	/**
	 * Set backgroundForDateMap
	 */
	public void setBackgroundResourceForDates(
			HashMap<Date, Integer> backgroundForDateMap) {
		// Clear first
		backgroundForDateTimeMap.clear();

		if (backgroundForDateMap == null || backgroundForDateMap.size() == 0) {
			return;
		}

		for (Date date : backgroundForDateMap.keySet()) {
			Integer resource = backgroundForDateMap.get(date);
			DateTime dateTime = CalendarHelper.convertDateToDateTime(date);
			backgroundForDateTimeMap.put(dateTime, resource);
		}
	}

	public void setBackgroundResourceForDateTimes(
			HashMap<DateTime, Integer> backgroundForDateTimeMap) {
		this.backgroundForDateTimeMap.putAll(backgroundForDateTimeMap);
	}

	public void setBackgroundResourceForDate(int backgroundRes, Date date) {
		DateTime dateTime = CalendarHelper.convertDateToDateTime(date);
		backgroundForDateTimeMap.put(dateTime, Integer.valueOf(backgroundRes));
	}

	public void setBackgroundResourceForDateTime(int backgroundRes,
			DateTime dateTime) {
		backgroundForDateTimeMap.put(dateTime, Integer.valueOf(backgroundRes));
	}

	/**
	 * Set textColorForDateMap
	 * 
	 * @return
	 */
	public void setTextColorForDates(HashMap<Date, Integer> textColorForDateMap) {
		// Clear first
		textColorForDateTimeMap.clear();

		if (textColorForDateMap == null || textColorForDateMap.size() == 0) {
			return;
		}

		for (Date date : textColorForDateMap.keySet()) {
			Integer resource = textColorForDateMap.get(date);
			DateTime dateTime = CalendarHelper.convertDateToDateTime(date);
			textColorForDateTimeMap.put(dateTime, resource);
		}
	}

	public void setTextColorForDateTimes(
			HashMap<DateTime, Integer> textColorForDateTimeMap) {
		this.textColorForDateTimeMap.putAll(textColorForDateTimeMap);
	}

	public void setTextColorForDate(int textColorRes, Date date) {
		DateTime dateTime = CalendarHelper.convertDateToDateTime(date);
		textColorForDateTimeMap.put(dateTime, Integer.valueOf(textColorRes));
	}

	public void setTextColorForDateTime(int textColorRes, DateTime dateTime) {
		textColorForDateTimeMap.put(dateTime, Integer.valueOf(textColorRes));
	}

	/**
	 * Get current saved sates of the Caldroid. Useful for handling rotation
	 */
	public Bundle getSavedStates() {
		Bundle bundle = new Bundle();
		bundle.putInt(MONTH, month);
		bundle.putInt(YEAR, year);

		if (dialogTitle != null) {
			bundle.putString(DIALOG_TITLE, dialogTitle);
		}

		if (selectedDates != null && selectedDates.size() > 0) {
			bundle.putStringArrayList(SELECTED_DATES,
					CalendarHelper.convertToStringList(selectedDates));
		}

		if (disableDates != null && disableDates.size() > 0) {
			bundle.putStringArrayList(DISABLE_DATES,
					CalendarHelper.convertToStringList(disableDates));
		}

		if (minDateTime != null) {
			bundle.putString(MIN_DATE, minDateTime.toString("yyyy-MM-dd"));
		}

		if (maxDateTime != null) {
			bundle.putString(MAX_DATE, maxDateTime.toString("yyyy-MM-dd"));
		}

		bundle.putBoolean(SHOW_NAVIGATION_ARROWS, showNavigationArrows);
		bundle.putBoolean(ENABLE_SWIPE, enableSwipe);
		bundle.putInt(START_DAY_OF_WEEK, startDayOfWeek);
		bundle.putBoolean(FIT_ALL_MONTHS, fitAllMonths);

		return bundle;
	}

	/**
	 * Save current state to bundle outState
	 * 
	 * @param outState
	 * @param key
	 */
	public void saveStatesToKey(Bundle outState, String key) {
		outState.putBundle(key, getSavedStates());
	}

	/**
	 * Restore current states from savedInstanceState
	 * 
	 * @param savedInstanceState
	 * @param key
	 */
	public void restoreStatesFromKey(Bundle savedInstanceState, String key) {
		if (savedInstanceState != null && savedInstanceState.containsKey(key)) {
			Bundle caldroidSavedState = savedInstanceState.getBundle(key);
			setArguments(caldroidSavedState);
		}
	}

	/**
	 * Restore state for dialog
	 * 
	 * @param savedInstanceState
	 * @param key
	 * @param dialogTag
	 */
	public void restoreDialogStatesFromKey(FragmentManager manager,
			Bundle savedInstanceState, String key, String dialogTag) {
		restoreStatesFromKey(savedInstanceState, key);

		CaldroidFragment existingDialog = (CaldroidFragment) manager
				.findFragmentByTag(dialogTag);
		if (existingDialog != null) {
			existingDialog.dismiss();
			show(manager, dialogTag);
		}
	}

	/**
	 * Get current virtual position of the month being viewed
	 */
	public int getCurrentVirtualPosition() {
		int currentPage = dateViewPager.getCurrentItem();
		return pageChangeListener.getCurrent(currentPage);
	}

	/**
	 * Move calendar to the specified date
	 * 
	 * @param date
	 */
	public void moveToDate(Date date) {
		moveToDateTime(CalendarHelper.convertDateToDateTime(date));
	}

	/**
	 * Move calendar to specified dateTime, with animation
	 * 
	 * @param dateTime
	 */
	public void moveToDateTime(DateTime dateTime) {

		DateTime firstOfMonth = new DateTime(year, month, 1, 0, 0);
		DateTime lastOfMonth = firstOfMonth.dayOfMonth().withMaximumValue();

		// To create a swipe effect
		// Do nothing if the dateTime is in current month

		// Calendar swipe left when dateTime is in the past
		if (dateTime.isBefore(firstOfMonth)) {
			// Get next month of dateTime. When swipe left, month will
			// decrease
			DateTime firstDayNextMonth = dateTime.plusMonths(1);

			// Refresh adapters
			pageChangeListener.setCurrentDateTime(firstDayNextMonth);
			int currentItem = dateViewPager.getCurrentItem();
			pageChangeListener.refreshAdapters(currentItem);

			// Swipe left
			dateViewPager.setCurrentItem(currentItem - 1);
		}

		// Calendar swipe right when dateTime is in the future
		else if (dateTime.isAfter(lastOfMonth)) {
			// Get last month of dateTime. When swipe right, the month will
			// increase
			DateTime firstDayLastMonth = dateTime.minusMonths(1);

			// Refresh adapters
			pageChangeListener.setCurrentDateTime(firstDayLastMonth);
			int currentItem = dateViewPager.getCurrentItem();
			pageChangeListener.refreshAdapters(currentItem);

			// Swipe right
			dateViewPager.setCurrentItem(currentItem + 1);
		}

	}

	/**
	 * Set month and year for the calendar. This is to avoid naive
	 * implementation of manipulating month and year. All dates within same
	 * month/year give same result
	 * 
	 * @param date
	 */
	public void setCalendarDate(Date date) {
		setCalendarDateTime(new DateTime(date));
	}

	public void setCalendarDateTime(DateTime dateTime) {
		month = dateTime.getMonthOfYear();
		year = dateTime.getYear();

		// Notify listener
		if (caldroidListener != null) {
			caldroidListener.onChangeMonth(month, year);
		}

		refreshView();
	}

	/**
	 * Set calendar to previous month
	 */
	public void prevMonth() {
		dateViewPager.setCurrentItem(pageChangeListener.getCurrentPage() - 1);
	}

	/**
	 * Set calendar to next month
	 */
	public void nextMonth() {
		dateViewPager.setCurrentItem(pageChangeListener.getCurrentPage() + 1);
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
	 * 
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
	 * Enable / Disable swipe to navigate different months
	 * 
	 * @return
	 */
	public boolean isEnableSwipe() {
		return enableSwipe;
	}

	public void setEnableSwipe(boolean enableSwipe) {
		this.enableSwipe = enableSwipe;
		dateViewPager.setEnabled(enableSwipe);
	}

	/**
	 * Set min date. This method does not refresh view
	 * 
	 * @param minDate
	 */
	public void setMinDate(Date minDate) {
		if (minDate == null) {
			minDateTime = null;
		} else {
			minDateTime = CalendarHelper.convertDateToDateTime(minDate);
		}
	}

	public boolean isFitAllMonths() {
		return fitAllMonths;
	}

	/**
	 * A calendar height is not fixed, it may have 5 or 6 rows. Set fitAllMonths
	 * to true so that the calendar will always have 6 rows
	 */
	public void setFitAllMonths(boolean fitAllMonths) {
		this.fitAllMonths = fitAllMonths;
		dateViewPager.setFitAllMonths(fitAllMonths);
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
			setMinDate(null);
		} else {
			minDateTime = CalendarHelper.getDateTimeFromString(minDateString,
					dateFormat);
		}
	}

	/**
	 * Set max date. This method does not refresh view
	 * 
	 * @param maxDate
	 */
	public void setMaxDate(Date maxDate) {
		if (maxDate == null) {
			maxDateTime = null;
		} else {
			maxDateTime = CalendarHelper.convertDateToDateTime(maxDate);
		}
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
			setMaxDate(null);
		} else {
			maxDateTime = CalendarHelper.getDateTimeFromString(maxDateString,
					dateFormat);
		}
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
		dateItemClickListener = new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {

				DateTime dateTime = dateInMonthsList.get(position);

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
		// Refresh title view
		monthTitleTextView.setText(new DateTime(year, month, 1, 0, 0, 0, 0)
				.monthOfYear().getAsText().toUpperCase()
				+ " " + year);

		// Refresh the date grid views
		for (CaldroidGridAdapter adapter : datePagerAdapters) {
			// Reset caldroid data
			adapter.setCaldroidData(getCaldroidData());

			// Reset extra data
			adapter.setExtraData(extraData);

			// Refresh view
			adapter.notifyDataSetChanged();
		}
	}

	/**
	 * Retrieve initial arguments to the fragment Data can include: month, year,
	 * dialogTitle, showNavigationArrows,(String) disableDates, selectedDates,
	 * minDate, maxDate
	 */
	private void retrieveInitialArgs(Bundle savedInstanceState) {
		// Get arguments
		Bundle args = getArguments();
		if (args != null) {
			// Get month, year
			month = args.getInt(MONTH, -1);
			year = args.getInt(YEAR, -1);
			dialogTitle = args.getString(DIALOG_TITLE);
			Dialog dialog = getDialog();
			if (dialog != null) {
				if (dialogTitle != null) {
					dialog.setTitle(dialogTitle);
				} else {
					// Don't display title bar if user did not supply
					// dialogTitle
					dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
				}
			}

			// Get start day of Week. Default calendar first column is SUNDAY
			startDayOfWeek = args.getInt(START_DAY_OF_WEEK,
					DateTimeConstants.SUNDAY);
			if (startDayOfWeek > 7) {
				startDayOfWeek = startDayOfWeek % 7;
			}

			// Should show arrow
			showNavigationArrows = args
					.getBoolean(SHOW_NAVIGATION_ARROWS, true);

			// Should enable swipe to change month
			enableSwipe = args.getBoolean(ENABLE_SWIPE, true);

			// Get fitAllMonths
			fitAllMonths = args.getBoolean(FIT_ALL_MONTHS, true);

			DateTimeFormatter formatter = DateTimeFormat
					.forPattern("yyyy-MM-dd");

			// Get disable dates
			ArrayList<String> disableDateStrings = args
					.getStringArrayList(DISABLE_DATES);
			if (disableDateStrings != null && disableDateStrings.size() > 0) {
				for (String dateString : disableDateStrings) {
					DateTime dt = formatter.parseDateTime(dateString);
					disableDates.add(dt);
				}
			}

			// Get selected dates
			ArrayList<String> selectedDateStrings = args
					.getStringArrayList(SELECTED_DATES);
			if (selectedDateStrings != null && selectedDateStrings.size() > 0) {
				for (String dateString : selectedDateStrings) {
					DateTime dt = formatter.parseDateTime(dateString);
					selectedDates.add(dt);
				}
			}

			// Get min date and max date
			String minDateTimeString = args.getString(MIN_DATE);
			if (minDateTimeString != null) {
				minDateTime = CalendarHelper.getDateTimeFromString(
						minDateTimeString, null);
			}

			String maxDateTimeString = args.getString(MAX_DATE);
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
		args.putString(DIALOG_TITLE, dialogTitle);
		args.putInt(MONTH, month);
		args.putInt(YEAR, year);

		f.setArguments(args);

		return f;
	}

	/**
	 * Below code fixed the issue viewpager disappears in dialog mode on
	 * orientation change
	 * 
	 * Code taken from Andy Dennie and Zsombor Erdody-Nagy
	 * http://stackoverflow.com/questions/8235080/fragments-dialogfragment
	 * -and-screen-rotation
	 */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setRetainInstance(true);
	}

	@Override
	public void onDestroyView() {
		if (getDialog() != null && getRetainInstance())
			getDialog().setDismissMessage(null);
		super.onDestroyView();
	}

	/**
	 * Setup view
	 */
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		retrieveInitialArgs(savedInstanceState);

		View view = inflater.inflate(R.layout.calendar_view, container, false);

		// For the monthTitleTextView
		monthTitleTextView = (TextView) view
				.findViewById(R.id.calendar_month_year_textview);

		// For the left arrow button
		leftArrowButton = (Button) view.findViewById(R.id.calendar_left_arrow);
		rightArrowButton = (Button) view
				.findViewById(R.id.calendar_right_arrow);

		// Navigate to previous month when user click
		leftArrowButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				prevMonth();
			}
		});

		// Navigate to next month when user click
		rightArrowButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				nextMonth();
			}
		});

		// Show navigation arrows depend on initial arguments
		setShowNavigationArrows(showNavigationArrows);

		// For the weekday gridview ("SUN, MON, TUE, WED, THU, FRI, SAT")
		weekdayGridView = (GridView) view.findViewById(R.id.weekday_gridview);
		WeekdayArrayAdapter weekdaysAdapter = new WeekdayArrayAdapter(
				getActivity(), android.R.layout.simple_list_item_1,
				getDaysOfWeek());
		weekdayGridView.setAdapter(weekdaysAdapter);

		// Setup all the pages of date grid views. These pages are recycled
		setupDateGridPages(view);

		// Refresh view
		refreshView();

		return view;
	}

	/**
	 * Setup 4 pages contain date grid views. These pages are recycled to use
	 * memory efficient
	 * 
	 * @param view
	 */
	private void setupDateGridPages(View view) {
		// Get current date time
		DateTime currentDateTime = new DateTime(year, month, 1, 0, 0, 0);
		dateInMonthsList = CalendarHelper.getFullWeeks(month, year,
				startDayOfWeek);

		// Set to pageChangeListener
		pageChangeListener = new DatePageChangeListener();
		pageChangeListener.setCurrentDateTime(currentDateTime);

		// Setup adapters for the grid views
		// Current month
		CaldroidGridAdapter adapter0 = getNewDatesGridAdapter(
				currentDateTime.getMonthOfYear(), currentDateTime.getYear());

		// Next month
		DateTime nextDateTime = currentDateTime.plusMonths(1);
		CaldroidGridAdapter adapter1 = getNewDatesGridAdapter(
				nextDateTime.getMonthOfYear(), nextDateTime.getYear());

		// Next 2 month
		DateTime next2DateTime = nextDateTime.plusMonths(1);
		CaldroidGridAdapter adapter2 = getNewDatesGridAdapter(
				next2DateTime.getMonthOfYear(), next2DateTime.getYear());

		// Previous month
		DateTime prevDateTime = currentDateTime.minusMonths(1);
		CaldroidGridAdapter adapter3 = getNewDatesGridAdapter(
				prevDateTime.getMonthOfYear(), prevDateTime.getYear());

		// Add to the array of adapters
		datePagerAdapters.add(adapter0);
		datePagerAdapters.add(adapter1);
		datePagerAdapters.add(adapter2);
		datePagerAdapters.add(adapter3);

		// Set adapters to the pageChangeListener so it can refresh the adapter
		// when page change
		pageChangeListener.setCaldroidGridAdapters(datePagerAdapters);

		// Setup InfiniteViewPager and InfinitePagerAdapter. The
		// InfinitePagerAdapter is responsible
		// for reuse the fragments
		dateViewPager = (InfiniteViewPager) view
				.findViewById(R.id.months_infinite_pager);

		// Set enable swipe
		dateViewPager.setEnabled(enableSwipe);

		// Set if viewpager wrap around particular month or all months (6 rows)
		dateViewPager.setFitAllMonths(fitAllMonths);

		// Set the dateInMonthsList to dateViewPager so it can calculate the
		// height correctly
		dateViewPager.setDateInMonthsList(dateInMonthsList);

		// MonthPagerAdapter actually provides 4 real fragments. The
		// InfinitePagerAdapter only recycles fragment provided by this
		// MonthPagerAdapter
		final MonthPagerAdapter pagerAdapter = new MonthPagerAdapter(
				getChildFragmentManager());

		// Provide initial data to the fragments, before they are attached to
		// view.
		fragments = pagerAdapter.getFragments();
		for (int i = 0; i < NUMBER_OF_PAGES; i++) {
			DateGridFragment dateGridFragment = fragments.get(i);
			CaldroidGridAdapter adapter = datePagerAdapters.get(i);
			dateGridFragment.setGridAdapter(adapter);
			dateGridFragment.setOnItemClickListener(getDateItemClickListener());
		}

		// Setup InfinitePagerAdapter to wrap around MonthPagerAdapter
		InfinitePagerAdapter infinitePagerAdapter = new InfinitePagerAdapter(
				pagerAdapter);

		// Use the infinitePagerAdapter to provide data for dateViewPager
		dateViewPager.setAdapter(infinitePagerAdapter);

		// Setup pageChangeListener
		dateViewPager.setOnPageChangeListener(pageChangeListener);
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

		if (startDayOfWeek != DateTimeConstants.SUNDAY) {
			nextDay = sunday.plusDays(startDayOfWeek);
		}

		for (int i = 0; i < 7; i++) {
			list.add(nextDay.dayOfWeek().getAsShortText().toUpperCase());
			nextDay = nextDay.plusDays(1);
		}

		return list;
	}

	/**
	 * DatePageChangeListener refresh the date grid views when user swipe the
	 * calendar
	 * 
	 * @author thomasdao
	 * 
	 */
	public class DatePageChangeListener implements OnPageChangeListener {
		private int currentPage = InfiniteViewPager.OFFSET;
		private DateTime currentDateTime;
		private ArrayList<CaldroidGridAdapter> caldroidGridAdapters;

		/**
		 * Return currentPage of the dateViewPager
		 * 
		 * @return
		 */
		public int getCurrentPage() {
			return currentPage;
		}

		public void setCurrentPage(int currentPage) {
			this.currentPage = currentPage;
		}

		/**
		 * Return currentDateTime of the selected page
		 * 
		 * @return
		 */
		public DateTime getCurrentDateTime() {
			return currentDateTime;
		}

		public void setCurrentDateTime(DateTime dateTime) {
			this.currentDateTime = dateTime;
			setCalendarDateTime(currentDateTime);
		}

		/**
		 * Return 4 adapters
		 * 
		 * @return
		 */
		public ArrayList<CaldroidGridAdapter> getCaldroidGridAdapters() {
			return caldroidGridAdapters;
		}

		public void setCaldroidGridAdapters(
				ArrayList<CaldroidGridAdapter> caldroidGridAdapters) {
			this.caldroidGridAdapters = caldroidGridAdapters;
		}

		/**
		 * Return virtual next position
		 * 
		 * @param position
		 * @return
		 */
		private int getNext(int position) {
			return (position + 1) % CaldroidFragment.NUMBER_OF_PAGES;
		}

		/**
		 * Return virtual previous position
		 * 
		 * @param position
		 * @return
		 */
		private int getPrevious(int position) {
			return (position + 3) % CaldroidFragment.NUMBER_OF_PAGES;
		}

		/**
		 * Return virtual current position
		 * 
		 * @param position
		 * @return
		 */
		public int getCurrent(int position) {
			return position % CaldroidFragment.NUMBER_OF_PAGES;
		}

		@Override
		public void onPageScrollStateChanged(int position) {
		}

		@Override
		public void onPageScrolled(int arg0, float arg1, int arg2) {
		}

		public void refreshAdapters(int position) {
			// Get adapters to refresh
			CaldroidGridAdapter currentAdapter = caldroidGridAdapters
					.get(getCurrent(position));
			CaldroidGridAdapter prevAdapter = caldroidGridAdapters
					.get(getPrevious(position));
			CaldroidGridAdapter nextAdapter = caldroidGridAdapters
					.get(getNext(position));

			if (position == currentPage) {
				// Refresh current adapter

				currentAdapter.setAdapterDateTime(currentDateTime);
				currentAdapter.notifyDataSetChanged();

				// Refresh previous adapter
				prevAdapter.setAdapterDateTime(currentDateTime.minusMonths(1));
				prevAdapter.notifyDataSetChanged();

				// Refresh next adapter
				nextAdapter.setAdapterDateTime(currentDateTime.plusMonths(1));
				nextAdapter.notifyDataSetChanged();
			}
			// Detect if swipe right or swipe left
			// Swipe right
			else if (position > currentPage) {
				// Update current date time to next month
				currentDateTime = currentDateTime.plusMonths(1);

				// Refresh the adapter of next gridview
				nextAdapter.setAdapterDateTime(currentDateTime.plusMonths(1));
				nextAdapter.notifyDataSetChanged();

			}
			// Swipe left
			else {
				// Update current date time to previous month
				currentDateTime = currentDateTime.minusMonths(1);

				// Refresh the adapter of previous gridview
				prevAdapter.setAdapterDateTime(currentDateTime.minusMonths(1));
				prevAdapter.notifyDataSetChanged();
			}

			// Update current page
			currentPage = position;
		}

		/**
		 * Refresh the fragments
		 */
		@Override
		public void onPageSelected(int position) {
			refreshAdapters(position);

			// Update current date time of the selected page
			setCalendarDateTime(currentDateTime);

			// Update all the dates inside current month
			CaldroidGridAdapter currentAdapter = caldroidGridAdapters
					.get(position % CaldroidFragment.NUMBER_OF_PAGES);

			// Refresh dateInMonthsList
			dateInMonthsList.clear();
			dateInMonthsList.addAll(currentAdapter.getDatetimeList());
		}

	}

}
