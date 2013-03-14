package com.candroidsample;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.caldroid.CaldroidFragment;
import com.caldroid.CaldroidListener;

@SuppressLint("SimpleDateFormat")
public class CaldroidSampleActivity extends FragmentActivity {
	private boolean undo = false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		final SimpleDateFormat formatter = new SimpleDateFormat("dd MMM yyyy");

		// Setup caldroid fragment
		// **** If you want normal CaldroidFragment, use below line ****
		final CaldroidFragment caldroidFragment = new CaldroidFragment();

		// This is to show customized fragment
		// **** If you want customized version, uncomment below line ****
		// final CaldroidSampleCustomFragment caldroidFragment = new CaldroidSampleCustomFragment();
		
		// Setup arguments
		Bundle args = new Bundle();
		Calendar cal = Calendar.getInstance();
		args.putInt("month", cal.get(Calendar.MONTH) + 1);
		args.putInt("year", cal.get(Calendar.YEAR));
		caldroidFragment.setArguments(args);

		FragmentTransaction t = getSupportFragmentManager().beginTransaction();
		t.replace(R.id.calendar1, caldroidFragment);
		t.commit();

		// Setup listener
		final CaldroidListener listener = new CaldroidListener() {

			@Override
			public void onSelectDate(Date date, View view) {
				Toast.makeText(getApplicationContext(), formatter.format(date),
						Toast.LENGTH_LONG).show();

			}

			@Override
			public void onChangeMonth(int month, int year) {
				String text = "month: " + month + " year: " + year;
				Toast.makeText(getApplicationContext(), text, Toast.LENGTH_LONG)
						.show();
			}

		};

		// Setup Caldroid
		caldroidFragment.setCaldroidListener(listener);

		final TextView textView = (TextView) findViewById(R.id.textview);

		final Button customizeButton = (Button) findViewById(R.id.customize_button);

		// Customize the calendar
		customizeButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (undo) {
					customizeButton.setText(getString(R.string.customize));
					textView.setText("");

					// Reset calendar
					caldroidFragment.clearDisableDates();
					caldroidFragment.clearSelectedDates();
					caldroidFragment.setMinDate(null);
					caldroidFragment.setMaxDate(null);
					caldroidFragment.setShowNavigationArrows(true);
					caldroidFragment.refreshView();
					undo = false;
					return;
				}

				// Else
				undo = true;
				customizeButton.setText(getString(R.string.undo));
				Calendar cal = Calendar.getInstance();

				// Min date is last 7 days
				cal.add(Calendar.DATE, -7);
				Date minDate = cal.getTime();

				// Max date is next 7 days
				cal = Calendar.getInstance();
				cal.add(Calendar.DATE, 14);
				Date maxDate = cal.getTime();

				// Set selected dates
				// From Date
				cal = Calendar.getInstance();
				cal.add(Calendar.DATE, 2);
				Date fromDate = cal.getTime();

				// To Date
				cal = Calendar.getInstance();
				cal.add(Calendar.DATE, 3);
				Date toDate = cal.getTime();

				// Set disabled dates
				ArrayList<Date> disabledDates = new ArrayList<Date>();
				for (int i = 5; i < 8; i++) {
					cal = Calendar.getInstance();
					cal.add(Calendar.DATE, i);
					disabledDates.add(cal.getTime());
				}

				// Customize
				caldroidFragment.setMinDate(minDate);
				caldroidFragment.setMaxDate(maxDate);
				caldroidFragment.setDisableDates(disabledDates);
				caldroidFragment.setSelectedDates(fromDate, toDate);
				caldroidFragment.setShowNavigationArrows(false);
				caldroidFragment.refreshView();

				String text = "Today: " + formatter.format(new Date()) + "\n";
				text += "Min Date: " + formatter.format(minDate) + "\n";
				text += "Max Date: " + formatter.format(maxDate) + "\n";
				text += "Select From Date: " + formatter.format(fromDate)
						+ "\n";
				text += "Select To Date: " + formatter.format(toDate) + "\n";
				for (Date date : disabledDates) {
					text += "Max Date: " + formatter.format(date) + "\n";
				}

				textView.setText(text);
			}
		});

		Button showDialogButton = (Button) findViewById(R.id.show_dialog_button);
		showDialogButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				CaldroidFragment dialogCaldroidFragment = CaldroidFragment
						.newInstance("Select a date", 3, 2013);
				dialogCaldroidFragment.show(getSupportFragmentManager(),
						"CALDROID_DIALOG_FRAGMENT");

				dialogCaldroidFragment.setCaldroidListener(listener);
			}
		});
	}

}
