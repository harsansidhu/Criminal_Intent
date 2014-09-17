package com.bignerdranch.android.criminalintent;

import java.util.UUID;

import android.support.v4.app.Fragment;


public class CrimeActivity extends SingleFragmentActivity {

	@Override 
	protected Fragment createFragment() {
		// return new CrimeFragment(); Calling the CrimeFragment constructor directly
		
		// Retrieve the UUID from CrimeFragment 
		UUID crimeId = (UUID)getIntent()
				.getSerializableExtra(CrimeFragment.EXTRA_CRIME_ID);
		
		return CrimeFragment.newInstance(crimeId);
		
		// Activities need to know about their fragments, not vice versa 
	}
}
