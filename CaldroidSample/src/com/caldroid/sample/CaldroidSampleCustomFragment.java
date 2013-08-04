package com.caldroid.sample;

import com.caldroid.CaldroidFragment;
import com.caldroid.CaldroidGridAdapter;

public class CaldroidSampleCustomFragment extends CaldroidFragment {

	@Override
	public CaldroidGridAdapter getNewDatesGridAdapter(int month, int year) {
		// TODO Auto-generated method stub
		return new com.caldroid.sample.CaldroidSampleCustomAdapter(getActivity(), month, year, getCaldroidData(), extraData);
	}

}
