package com.bignerdranch.android.criminalintent;

import java.util.ArrayList;
import java.util.UUID;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;

public class CrimePagerActivity extends FragmentActivity {
	private ViewPager mViewPager;
	private ArrayList<Crime> mCrimes; 

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// Instantiate ViewPager and set its content view 
		// using the resource id from ids.xml
		mViewPager = new ViewPager(this);
		mViewPager.setId(R.id.viewPager);
		setContentView(mViewPager);

		// Get the list of Crimes from CrimeLab 
		mCrimes = CrimeLab.get(this).getCrimes();

		// Get the Activity's instance of FragmentManager 
		FragmentManager fm = getSupportFragmentManager();
		// Need to set the adapter for the ViewPager in order to update the View
		mViewPager.setAdapter(new FragmentStatePagerAdapter(fm) {
			// Need the size of Array 
			@Override
			public int getCount() {
				return mCrimes.size();
			}

			// Which Crime are we looking at, then return it
			@Override
			public Fragment getItem(int pos) {
				Crime crime = mCrimes.get(pos);
				return CrimeFragment.newInstance(crime.getId());
			}
		});

		// Set the title of the page viewer by getting the title from the Crime model
		mViewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
			// Whether the page animation is being actively dragged, settling to a 
			// steady state, or idling 
			public void onPageScrollStateChanged(int state) { }
			// Exactly where your page is going to be 
			public void onPageScrolled(int pos, float posOffset, int posOffsetPixels) { }
			// The current page selected 
			public void onPageSelected(int pos) {
				Crime crime = mCrimes.get(pos);
				if (crime.getTitle() != null) {
					setTitle(crime.getTitle());
				}
			} });

		UUID crimeId = (UUID)getIntent()
				.getSerializableExtra(CrimeFragment.EXTRA_CRIME_ID);
		for (int i = 0; i < mCrimes.size(); i++) {
			// Find the Crime instance whose mId matches the crimeId in the intent extra
			if (mCrimes.get(i).getId().equals(crimeId)) {
				mViewPager.setCurrentItem(i);
				break;
			}
		}
	}
}
