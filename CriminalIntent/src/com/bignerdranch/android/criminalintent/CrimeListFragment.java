package com.bignerdranch.android.criminalintent;

import java.util.ArrayList;

import android.annotation.TargetApi;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.ActionMode;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView.MultiChoiceModeListener;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.TextView;

public class CrimeListFragment extends ListFragment {
	private ArrayList<Crime> mCrimes; 
	private boolean mSubtitleVisible; 
	private static final String TAG = "CrimeListFragment";
	
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		// Explicitly tell the FragmentManager that the fragment should receive
		// a call to onCreateOptionsMenu
		setHasOptionsMenu(true); 
		
		// Returns the hosting activity and allows a fragment to 
		// handle more of the responsibilities of an activity 
		getActivity().setTitle(R.string.crimes_title);
		
		// Get the Array of Crimes from the model, 
		mCrimes = CrimeLab.get(getActivity()).getCrimes(); 
		
		// Adapter will create the nesscary view object
		// Populate it with data from the model layer 
		// returning the view object to ListView, displaying it 
		CrimeAdapter adapter = new CrimeAdapter(mCrimes);  
		setListAdapter(adapter);		
		
		setRetainInstance(true);
		mSubtitleVisible = false; 
	}
	
	// Reloads the list to reflect changes in the data for the Crime object
	// aka when the CheckBox is checked, or title of the Crime is changed 
	@Override 
	public void onResume() {
		super.onResume();
		((CrimeAdapter)getListAdapter()).notifyDataSetChanged(); 
	}
	
	// Inflating the Options Menu
	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		super.onCreateOptionsMenu(menu, inflater);
		inflater.inflate(R.menu.fragment_crime_list, menu);
		MenuItem showSubtitle = menu.findItem(R.id.menu_item_show_subtitle);
		if (mSubtitleVisible && showSubtitle != null) {
            showSubtitle.setTitle(R.string.hide_subtitle);
        }
	}
	
	// Add a new Crime when the New Crime options menu is selected
	@TargetApi(11)
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		// Which menu item was clicked on 
			case R.id.menu_item_new_crime:
				Crime crime = new Crime(); 
				CrimeLab.get(getActivity()).addCrime(crime); 
				Intent i = new Intent(getActivity(), CrimePagerActivity.class);
				startActivityForResult(i, 0);
				return true;
			case R.id.menu_item_show_subtitle:
				if (getActivity().getActionBar().getSubtitle() == null) {
					getActivity().getActionBar().setSubtitle(R.string.subtitle);
					mSubtitleVisible = true;
					item.setTitle(R.string.hide_subtitle);
				} else {
					getActivity().getActionBar().setSubtitle(null);
					mSubtitleVisible = false; 
					item.setTitle(R.string.show_subtitle);
				}
				return true;
			default:
				return super.onOptionsItemSelected(item);
		}
	}
	
	// Inflate a context Menu using the crime_list...xml file 
	@Override 
	public void onCreateContextMenu(ContextMenu menu, View v, ContextMenuInfo menuinfo) {
		getActivity().getMenuInflater().inflate(R.menu.crime_list_item_context, menu);
	}
	
	// Listen for the Context Menu selection and respond accordingly 
	@Override
	public boolean onContextItemSelected(MenuItem item) {
		AdapterContextMenuInfo info = (AdapterContextMenuInfo)item.getMenuInfo();
		int position = info.position; 
		CrimeAdapter adapter = (CrimeAdapter)getListAdapter(); 
		Crime crime = adapter.getItem(position);
		
		switch (item.getItemId()) {
		case R.id.menu_item_delete_crime:
			CrimeLab.get(getActivity()).deleteCrime(crime);
			adapter.notifyDataSetChanged();
			return true; 
		}
		return super.onContextItemSelected(item);
	}
	
	@Override 
	public void onListItemClick(ListView l, View v, int position, long id) {
		// Get the Crime from the adapter 
		Crime c = ((CrimeAdapter)getListAdapter()).getItem(position);
		
		// Start CrimeActivity 
		// getActivity() gets the Context needed for the Intent constructor 
		//Intent i = new Intent(getActivity(), CrimeActivity.class);
		
		// Start CrimePagerActivity with this crime
		Intent i = new Intent(getActivity(), CrimePagerActivity.class);
		
		// Create and add an extra containing the UUID of a crime 
		i.putExtra(CrimeFragment.EXTRA_CRIME_ID, c.getId());
		startActivity(i); 
	}
	
	// Custom adapter to set the views for the TextViews and CheckBox 
	// by using the model layer to update the views 
	private class CrimeAdapter extends ArrayAdapter<Crime> {
		
		public CrimeAdapter(ArrayList<Crime> crimes) {
			super(getActivity(), 0, crimes);
		}
		
		@Override 
	    public View getView(int position, View convertView, ViewGroup parent) {
	        // If we weren't given a view, inflate one
	        if (convertView == null) {
	            convertView = getActivity().getLayoutInflater()
	                .inflate(R.layout.list_item_crime, null);
	}
	        // Configure the view for this Crime
	        Crime c = getItem(position); // Get the crime in the current position in the list
	        TextView titleTextView =
	            (TextView)convertView.findViewById(R.id.crime_list_item_titleTextView);
	        titleTextView.setText(c.getTitle());
	        TextView dateTextView =
	            (TextView)convertView.findViewById(R.id.crime_list_item_dateTextView);
	        dateTextView.setText(c.getDate().toString());
	        CheckBox solvedCheckBox =
	            (CheckBox)convertView.findViewById(R.id.crime_list_item_solvedCheckBox);
	        solvedCheckBox.setChecked(c.isSolved());
	        return convertView;
	    }
	}
	
	@TargetApi(11)
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup parent,
			Bundle savedInstanceState) {
		View v = super.onCreateView(inflater, parent, savedInstanceState);
		
		// Wrap the Action Bar for higher API's 
	    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
	         if (mSubtitleVisible) {
	            getActivity().getActionBar().setSubtitle(R.string.subtitle);
	         }
	}
		 
		// R.id.list is used to retrieve the ListView managed by ListFragment 
		ListView listView = (ListView)v.findViewById(android.R.id.list);
		
		if (Build.VERSION.SDK_INT < Build.VERSION_CODES.HONEYCOMB) {
			// User floating context menus on Froyo and Gingerbread
			registerForContextMenu(listView);	
		} else {
			// Use contextual action bar on Honeycomb and higher 
			listView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE_MODAL);
			// Implement MultiChoice selection 
			listView.setMultiChoiceModeListener(new MultiChoiceModeListener() {
				 public void onItemCheckedStateChanged(ActionMode mode, int position,
		                    long id, boolean checked) {
		                // Required, but not used in this implementation
		            }
				    // ActionMode.Callback methods
		            public boolean onCreateActionMode(ActionMode mode, Menu menu) {
		                MenuInflater inflater = mode.getMenuInflater();
		                inflater.inflate(R.menu.crime_list_item_context, menu);
		                return true;
		            }
		            public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
		                return false;
		                // Required, but not used in this implementation
		            }
		            public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
		            	switch (item.getItemId()) {
		            	case R.id.menu_item_delete_crime:
		            		CrimeAdapter adapter = (CrimeAdapter)getListAdapter();
		            		CrimeLab crimeLab = CrimeLab.get(getActivity());
		            		for (int i = adapter.getCount() - 1; i >= 0; i--) {
		            			if (getListView().isItemChecked(i)) {
		            				crimeLab.deleteCrime(adapter.getItem(i));
		            			}
		            		}
		            		mode.finish();
		            		adapter.notifyDataSetChanged();
		            		return true;
		            	default:
		            		return false;
		            	} 
		            }
		            public void onDestroyActionMode(ActionMode mode) {
		                // Required, but not used in this implementation
		            }
			}); 
		}
		
		return v; 
	}
}