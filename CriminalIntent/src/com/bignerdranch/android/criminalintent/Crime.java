package com.bignerdranch.android.criminalintent;

import java.util.Date;
import java.util.UUID;

import org.json.JSONException;
import org.json.JSONObject;


public class Crime {
	
	// Variables for JSON Serialization
	private static final String JSON_ID = "id";
	private static final String JSON_TITLE = "title";
	private static final String JSON_SOLVED = "solved";
	private static final String JSON_DATE = "date";

	private UUID mId;        // Unique Id of the Crime
	private String mTitle;   // Title of Crime
	private Date mDate;      // Date of Crime
	private boolean mSolved; // Solved or Not? 
	
	public Crime() {
		// Generate unique identifier 
		mId = UUID.randomUUID(); 
		// Sets the date, default constructor sets current date
		mDate = new Date();  
	}
	
	// Read and get the values from the json object  
	public Crime(JSONObject json) throws JSONException {
		mId = UUID.fromString(json.getString(JSON_ID));
		if (json.has(JSON_TITLE)) {
			mTitle = json.getString(JSON_TITLE);
		}
		mSolved = json.getBoolean(JSON_SOLVED);
		mDate   = new Date(json.getLong(JSON_DATE));
	}
	
	// Store the private instance variables of crime as JSON Objects for later retrieval
	public JSONObject toJSON() throws JSONException {
		JSONObject json = new JSONObject(); 
		json.put(JSON_ID, mId.toString());
		json.put(JSON_TITLE, mTitle);
		json.put(JSON_SOLVED, mSolved);
		json.put(JSON_DATE, mDate.getTime());
		return json;
	}
	
	// Overriding the toString method 
	@Override 
	public String toString() {
		return mTitle; 
	}

	public UUID getId() {
		return mId;
	}
	
	public String getTitle() {
		return mTitle;
	}
	
	public void setTitle(String Title) {
		this.mTitle = Title;
	}

	public Date getDate() {
		return mDate;
	}

	public void setDate(Date date) {
		this.mDate = date;
	}

	public boolean isSolved() {
		return mSolved;
	}

	public void setSolved(boolean solved) {
		this.mSolved = solved;
	}
}
