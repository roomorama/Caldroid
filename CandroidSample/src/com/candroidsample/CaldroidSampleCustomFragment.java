package com.candroidsample;

import com.caldroid.CaldroidFragment;
import com.caldroid.CaldroidGridAdapter;

public class CaldroidSampleCustomFragment extends CaldroidFragment {

	@Override
	public CaldroidGridAdapter getNewDatesGridAdapter() {
		// TODO Auto-generated method stub
		return new CaldroidSampleCustomAdapter(getActivity(), month, year, disableDates, selectedDates, minDateTime, maxDateTime);
	}

}
