package com.candroidsample;

import java.util.ArrayList;

import org.joda.time.DateTime;

import android.content.Context;
import android.content.res.Resources;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.caldroid.CaldroidFragment;
import com.caldroid.CaldroidGridAdapter;

public class CaldroidSampleCustomAdapter extends CaldroidGridAdapter {

	public CaldroidSampleCustomAdapter(Context context, int month, int year,
			ArrayList<DateTime> disableDates,
			ArrayList<DateTime> selectedDates, DateTime minDateTime,
			DateTime maxDateTime) {
		super(context, month, year, disableDates, selectedDates, minDateTime,
				maxDateTime);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View cellView = convertView;

		// For reuse
		if (convertView == null) {
			cellView = inflater.inflate(R.layout.custom_cell, null);
		}

		int topPadding = cellView.getPaddingTop();
		int leftPadding = cellView.getPaddingLeft();
		int bottomPadding = cellView.getPaddingBottom();
		int rightPadding = cellView.getPaddingRight();

		TextView tv1 = (TextView) cellView.findViewById(R.id.tv1);
		TextView tv2 = (TextView) cellView.findViewById(R.id.tv2);

		// Get dateTime of this cell
		DateTime dateTime = this.datetimeList.get(position);
		Resources resources = context.getResources();

		// Set color of the dates in previous / next month
		if (dateTime.getMonthOfYear() != month) {
			tv1.setTextColor(resources
					.getColor(com.caldroid.R.color.caldroid_darker_gray));
		}

		// Customize for today
		if (dateTime.equals(getToday())) {
			cellView.setBackgroundResource(com.caldroid.R.drawable.red_border);
		}

		// Customize for disabled dates and date outside min/max dates
		if ((minDateTime != null && dateTime.isBefore(minDateTime))
				|| (maxDateTime != null && dateTime.isAfter(maxDateTime))
				|| (disableDates != null && disableDates.indexOf(dateTime) != -1)) {

			tv1.setTextColor(CaldroidFragment.disabledTextColor);
			if (CaldroidFragment.disabledBackgroundDrawable == -1) {
				cellView.setBackgroundResource(com.caldroid.R.drawable.disable_cell);
			} else {
				cellView.setBackgroundResource(CaldroidFragment.disabledBackgroundDrawable);
			}

			if (dateTime.equals(getToday())) {
				cellView.setBackgroundResource(com.caldroid.R.drawable.red_border_gray_bg);
			}

		}

		// Customize for selected dates
		if (selectedDates != null && selectedDates.indexOf(dateTime) != -1) {
			if (CaldroidFragment.selectedBackgroundDrawable != -1) {
				cellView.setBackgroundResource(CaldroidFragment.selectedBackgroundDrawable);
			} else {
				cellView.setBackgroundColor(resources
						.getColor(com.caldroid.R.color.caldroid_sky_blue));
			}

			tv1.setTextColor(CaldroidFragment.selectedTextColor);

		}

		tv1.setText("" + dateTime.getDayOfMonth());
		tv2.setText("Hi");

		// Somehow after setBackgroundResource, the padding collapse.
		// This is to recover the padding
		cellView.setPadding(leftPadding, topPadding, rightPadding,
				bottomPadding);

		return cellView;
	}

}
