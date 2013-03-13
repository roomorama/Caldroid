package com.caldroid;

import java.util.ArrayList;
import java.util.Date;

import org.joda.time.DateTime;

import android.content.Context;
import android.content.res.Resources;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

/**
 * The CaldroidGridAdapter provides customized view for the dates gridview
 * 
 * @author thomasdao
 * 
 */
public class CaldroidGridAdapter extends BaseAdapter {
	private ArrayList<DateTime> datetimeList;
	private int month;
	private int year;
	private Context context;
	private ArrayList<DateTime> disableDates;
	private ArrayList<DateTime> selectedDates;
	private DateTime minDateTime;
	private DateTime maxDateTime;

	private DateTime today;

	public CaldroidGridAdapter(Context context, int month, int year,
			ArrayList<DateTime> disableDates,
			ArrayList<DateTime> selectedDates, DateTime minDateTime,
			DateTime maxDateTime) {
		super();
		this.month = month;
		this.year = year;
		this.context = context;
		this.disableDates = disableDates;
		this.selectedDates = selectedDates;
		this.minDateTime = minDateTime;
		this.maxDateTime = maxDateTime;
		this.datetimeList = CalendarHelper.getFullWeeks(this.month, this.year);
	}

	private DateTime getToday() {
		if (today == null) {
			today = CalendarHelper.convertDateToDateTime(new Date());
		}
		return today;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return this.datetimeList.size();
	}

	@Override
	public Object getItem(int arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public long getItemId(int arg0) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		TextView cellView = (TextView) convertView;

		// For reuse
		if (convertView == null) {
			cellView = (TextView) inflater.inflate(R.layout.date_cell, null);
		}

		DateTime dateTime = this.datetimeList.get(position);
		Resources resources = context.getResources();

		// Set color of the dates in previous / next month
		if (dateTime.getMonthOfYear() != month) {
			cellView.setTextColor(resources
					.getColor(R.color.caldroid_darker_gray));
		}

		// Customize for today
		if (dateTime.equals(getToday())) {
			cellView.setBackgroundResource(R.drawable.red_border);
		}

		// Customize for disabled dates and date outside min/max dates
		if ((minDateTime != null && dateTime.isBefore(minDateTime))
				|| (maxDateTime != null && dateTime.isAfter(maxDateTime))
				|| (disableDates != null && disableDates.indexOf(dateTime) != -1)) {

			cellView.setTextColor(CaldroidFragment.disabledTextColor);
			if (CaldroidFragment.disabledBackgroundDrawable == -1) {
				cellView.setBackgroundResource(R.drawable.disable_cell);
			} else {
				cellView.setBackgroundResource(CaldroidFragment.disabledBackgroundDrawable);
			}

			if (dateTime.equals(getToday())) {
				cellView.setBackgroundResource(R.drawable.red_border_gray_bg);
			}

		}

		// Customize for selected dates
		if (selectedDates != null && selectedDates.indexOf(dateTime) != -1) {
			if (CaldroidFragment.selectedBackgroundDrawable != -1) {
				cellView.setBackgroundResource(CaldroidFragment.selectedBackgroundDrawable);
			} else {
				cellView.setBackgroundColor(resources
						.getColor(R.color.caldroid_sky_blue));
			}

			cellView.setTextColor(CaldroidFragment.selectedTextColor);

		}

		cellView.setText("" + dateTime.getDayOfMonth());

		return cellView;
	}

}
