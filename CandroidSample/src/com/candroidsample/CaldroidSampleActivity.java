package com.candroidsample;

import java.util.Calendar;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;

import com.caldroid.CaldroidFragment;

public class CaldroidSampleActivity extends FragmentActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        CaldroidFragment caldroidFragment = new CaldroidFragment();
        Bundle args = new Bundle();
        Calendar cal = Calendar.getInstance();
		args.putInt("month", cal.get(Calendar.MONTH) + 1);
		args.putInt("year", cal.get(Calendar.YEAR));
		caldroidFragment.setArguments(args);
				
		FragmentTransaction t = getSupportFragmentManager().beginTransaction();
		t.add(R.id.calendar1, caldroidFragment);
		t.commit();
		
    }
    
}
