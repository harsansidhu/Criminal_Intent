package com.bignerdranch.android.criminalintent;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.View;
import android.widget.DatePicker;
import android.widget.DatePicker.OnDateChangedListener;

public class DatePickerFragment extends DialogFragment {
	public static final String EXTRA_DATE = 
			"com.bignerdranch.android.criminalintent.date";
	
	private Date mDate; 
	
	// Bundle up the date in the fragment by setting its argument 
	public static DatePickerFragment newInstance(Date date) {
		// Store the date in this bundle as an EXTRA 
		Bundle args = new Bundle(); 
		args.putSerializable(EXTRA_DATE, date);
		
		DatePickerFragment fragment = new DatePickerFragment(); 
		fragment.setArguments(args);
		
		return fragment;
	}
	
	// Sending the date to CrimeFragment via a Target Fragment
	private void sendResult(int resultCode) {
		if (getTargetFragment() == null)
			return; 
		
		Intent i = new Intent(); 
		i.putExtra(EXTRA_DATE, mDate);
		
		getTargetFragment()
		.onActivityResult(getTargetRequestCode(), resultCode, i);
	}
	
	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		mDate = (Date)getArguments().getSerializable(EXTRA_DATE);
		
		// Create a Calendar to get the year, month, and day 
		Calendar calendar = Calendar.getInstance(); 
		calendar.setTime(mDate);
		int year  = calendar.get(Calendar.YEAR);
		int month = calendar.get(Calendar.MONTH);
		int day   = calendar.get(Calendar.DAY_OF_MONTH);
		
		// Inflate the View from dialog_date 
		View v = getActivity().getLayoutInflater()
				.inflate(R.layout.dialog_date, null);
		
		DatePicker datePicker = (DatePicker)v.findViewById(R.id.dialog_date_datePicker);
		datePicker.init(year, month, day, new OnDateChangedListener() {
			public void onDateChanged(DatePicker view, int year, int month, int day) {
				// Translate year, month, day, into a Date object using a calendar 
				mDate = new GregorianCalendar(year, month, day).getTime(); 
				
				// Update argument to preserve selected value on rotation
				getArguments().putSerializable(EXTRA_DATE, mDate); 
			}
		});
		
		
		// Passing the Context then return an instance of AlertDialog.Builder
		// Call two AlertDialog methods to configure the dialog
		// Set the View of the Date Picker defined in the dialog_date.xml file
		return new AlertDialog.Builder(getActivity())
		.setView(v)
		.setTitle(R.string.date_picker_title)
		.setPositiveButton(
				android.R.string.ok, 
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						sendResult(Activity.RESULT_OK);
					}
				})
				.create();
	}
}
